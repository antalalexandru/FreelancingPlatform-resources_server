package resources.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import resources.data.dto.*;
import resources.data.entity.ActivationToken;
import resources.data.entity.ResetToken;
import resources.data.entity.User;
import resources.data.repository.ActivationTokenRepository;
import resources.data.repository.ResetTokenRepository;
import resources.data.repository.UserRepository;
import resources.exceptions.BadRequestException;
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
    private ResetTokenRepository resetTokenRepository;
    private ConversionService conversionService;
    private EmailService emailService;
    private PasswordEncoder passwordEncoder;

    @Value("${AUTH_SERVER_PROTOCOL:http}")
    private String authorizationServerProtocol;

    @Value("${AUTH_SERVER_ADDR:localhost}")
    private String authorizationServerAddress;

    @Value("${AUTH_SERVER_PORT:8020}")
    private int authorizationServerPort;

    @Autowired
    public UserService(UserRepository userRepository, ActivationTokenRepository activationTokenRepository, ResetTokenRepository resetTokenRepository, EmailService emailService, RestTemplate restTemplate, HttpHeaderHelper httpHeaderHelper, PasswordEncoder passwordEncoder, ConversionService conversionService) {
        this.userRepository = userRepository;
        this.activationTokenRepository = activationTokenRepository;
        this.resetTokenRepository = resetTokenRepository;
        this.emailService = emailService;
        this.restTemplate = restTemplate;
        this.httpHeaderHelper = httpHeaderHelper;
        this.conversionService = conversionService;
        this.passwordEncoder = passwordEncoder;
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

    public void forgotPassword(final String keyword) {
        final User user = userRepository.findByUsernameEqualsOrEmailEquals(keyword, keyword)
                .orElseThrow(() -> new BadRequestException("User not found!"));

        final ResetToken resetToken = ResetToken.builder()
                .token(UUID.randomUUID().toString())
                .userId(user.getId())
                .build();

        resetTokenRepository.save(resetToken);
        emailService.sendResetEmail(user.getEmail(), user.getId(), resetToken.getToken());
    }

    public void resetPassword(ResetPasswordRequestDTO passwordRequestDTO) {
        final User user = userRepository.findById(passwordRequestDTO.getUserId())
                .orElseThrow(() -> new BadRequestException("User not found!"));

        final ResetToken resetToken = resetTokenRepository.findByTokenEquals(passwordRequestDTO.getToken())
                .orElseThrow(() -> new BadRequestException("Token not found!"));

        user.setPassword(passwordEncoder.encode(passwordRequestDTO.getPassword()));
        userRepository.save(user);
        resetTokenRepository.delete(resetToken);
    }
}
