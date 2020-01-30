package resources.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class HttpHeaderHelper {
    @Value("${authorizationServer.clientId:clientIdPassword}")
    private String clientId;

    @Value("${authorizationServer.clientSecret:secret}")
    private String clientSecret;

    public HttpHeaders getLoginHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        return headers;
    }
}
