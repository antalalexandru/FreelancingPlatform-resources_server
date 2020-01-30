package resources.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class HttpHeaderHelper {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BASIC = "Basic ";
    private static final String CONTENT = "Content-Type";
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";

    @Value("${authorizationServer.clientId:clientIdPassword}")
    private String clientId;

    @Value("${authorizationServer.clientSecret:secret}")
    private String clientSecret;

    public HttpHeaders getLoginHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT, CONTENT_TYPE);
        headers.setBasicAuth(clientId, clientSecret);

        return headers;
    }
}
