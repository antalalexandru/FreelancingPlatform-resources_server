package resources.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import resources.data.dto.AuthenticationRequestDTO;
import resources.data.dto.AuthenticationResponseDTO;
import resources.exceptions.InvalidCredentialsException;
import resources.util.HttpHeaderHelper;

@Service
public final class UserService {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String GRANT_TYPE = "grant_type";
    private static final String LOGIN_PATH = "/oauth/token";
    private RestTemplate restTemplate;
    private HttpHeaderHelper httpHeaderHelper;

    @Value("${AUTH_SERVER_PROTOCOL:http}")
    private String authorizationServerProtocol;

    @Value("${AUTH_SERVER_ADDR:localhost}")
    private String authorizationServerAddress;

    @Value("${AUTH_SERVER_PORT:8020}")
    private int authorizationServerPort;

    @Autowired
    public UserService(RestTemplate restTemplate, HttpHeaderHelper httpHeaderHelper) {
        this.restTemplate = restTemplate;
        this.httpHeaderHelper = httpHeaderHelper;
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
}
