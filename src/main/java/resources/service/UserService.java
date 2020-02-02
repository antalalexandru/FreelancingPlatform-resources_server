package resources.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import resources.data.dto.ActivationTokenRequestDTO;
import resources.data.dto.AuthenticationRequestDTO;
import resources.data.dto.AuthenticationResponseDTO;
import resources.data.dto.UserRequestDTO;
import resources.data.entity.ActivationToken;
import resources.data.entity.User;
import resources.data.repository.ActivationTokenRepository;
import resources.data.repository.UserRepository;
import resources.exceptions.InvalidCredentialsException;
import resources.exceptions.UserAlreadyExistsException;
import resources.util.HttpHeaderHelper;

import java.util.Optional;
import java.util.UUID;

@Service
public final class UserService {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String GRANT_TYPE = "grant_type";
    private static final String LOGIN_PATH = "/oauth/token";

    private RestTemplate restTemplate;
    private HttpHeaderHelper httpHeaderHelper;
    private UserRepository userRepository;
    private ActivationTokenRepository activationTokenRepository;
    private ConversionService conversionService;
    private EmailService emailService;

    @Value("${AUTH_SERVER_PROTOCOL:http}")
    private String authorizationServerProtocol;

    @Value("${AUTH_SERVER_ADDR:localhost}")
    private String authorizationServerAddress;

    @Value("${AUTH_SERVER_PORT:8020}")
    private int authorizationServerPort;

    @Autowired
    public UserService(UserRepository userRepository, ActivationTokenRepository activationTokenRepository, EmailService emailService, RestTemplate restTemplate, HttpHeaderHelper httpHeaderHelper, ConversionService conversionService) {
        this.userRepository = userRepository;
        this.activationTokenRepository = activationTokenRepository;
        this.emailService = emailService;
        this.restTemplate = restTemplate;
        this.httpHeaderHelper = httpHeaderHelper;
        this.conversionService = conversionService;
    }

    private String buildAuthorizationServerURL() {
        return authorizationServerProtocol + "://" + authorizationServerAddress + ":" + authorizationServerPort;
    }

    public AuthenticationResponseDTO handleAuthenticationRequest(final AuthenticationRequestDTO authenticationRequest) {
        ResponseEntity<AuthenticationResponseDTO> responseEntity;
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(buildAuthorizationServerURL())
                .path(LOGIN_PATH)
                .queryParam(GRANT_TYPE, PASSWORD)
                .queryParam(USERNAME, authenticationRequest.getUsername())
                .queryParam(PASSWORD, authenticationRequest.getPassword());

        try {
            responseEntity = restTemplate.exchange(uriComponentsBuilder.toUriString(), HttpMethod.POST, new HttpEntity<>(httpHeaderHelper.getLoginHeaders()), AuthenticationResponseDTO.class);
        } catch (HttpClientErrorException ex) {
            throw new InvalidCredentialsException();
        }

        return responseEntity.getBody();
    }

    public void register(final UserRequestDTO userDTO) {
        Optional<User> userFound = userRepository.findByUsernameEqualsOrEmailEquals(userDTO.getUsername(), userDTO.getEmail());
        final User user = conversionService.convert(userDTO, User.class);

        if (!userFound.isPresent()) {
            User savedUser = userRepository.save(user);
            final ActivationToken activationToken = ActivationToken.builder()
                    .token(UUID.randomUUID().toString())
                    .userId(savedUser.getId())
                    .build();
            activationTokenRepository.save(activationToken);
            this.emailService.sendActivationEmail(savedUser.getEmail(), savedUser.getId(), activationToken.getToken());
        } else {
            throw new UserAlreadyExistsException("Username/Email already exists");
        }
    }

    public void activate(ActivationTokenRequestDTO activationTokenRequestDTO) {
        Optional<ActivationToken> token = activationTokenRepository.findByUserIdEquals(activationTokenRequestDTO.getUserId());

        if (token.isPresent() && token.get().getToken().equals(activationTokenRequestDTO.getToken())) {
            Optional<User> user = userRepository.findById(activationTokenRequestDTO.getUserId());
            user.ifPresent(value -> value.set_enabled(true));
            activationTokenRepository.delete(token.get());
        }
    }
}
