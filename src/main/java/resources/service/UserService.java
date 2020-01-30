package resources.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import resources.data.dto.AuthenticationRequestDTO;
import resources.data.dto.AuthenticationResponseDTO;

@Service
public final class UserService {

    @Value("${AUTH_SERVER_PROTOCOL:http}")
    private String authorizationServerProtocol;

    @Value("${AUTH_SERVER_ADDR:localhost}")
    private String authorizationServerAddress;

    @Value("${AUTH_SERVER_PORT:8020}")
    private int authorizationServerPort;

    private String buildAuthorizationServerURL() {
        return authorizationServerProtocol + "://" + authorizationServerAddress + ":" + authorizationServerPort;
    }

    public AuthenticationResponseDTO handleAuthenticationRequest(final AuthenticationRequestDTO authenticationRequest) {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(buildAuthorizationServerURL())
              .queryParam("grant_type", "password")
              .queryParam("username", authenticationRequest.getUsername())
              .queryParam("password", authenticationRequest.getPassword())
              .queryParam("client_id", "clientIdPassword");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth("clientIdPassword", "secret");

        return restTemplate.postForObject(uriComponentsBuilder.toUriString(), new HttpEntity(headers),
              AuthenticationResponseDTO.class);
    }

}
