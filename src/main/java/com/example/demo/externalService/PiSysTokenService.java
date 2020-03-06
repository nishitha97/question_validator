package com.example.demo.externalService;


import com.example.demo.externalService.domain.PiResponse;
import com.example.demo.externalService.domain.PiTokenRequest;
import com.example.demo.util.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Date;

@Service
public class PiSysTokenService {
    private static String sysPiToken = null;
    private static Date sysPiTokenCreatedTIme = new Date();

    //@Inject
    //private ApplicationProperties applicationProperties;

    @Autowired
    private RestClient restClient;

    private static boolean isTokenExpired() {

        long diffInMins = (new Date().getTime() - sysPiTokenCreatedTIme.getTime()) / 60000;
        long expireMins = 120;
        return (diffInMins > (120));

    }

    /**
     * Serves pi system token.
     *
     * @return the token
     * @throws AuthenticationException the authentication failed exception
     */
    public String getPiSysToken() throws AuthenticationException {
        if (sysPiToken == null || isTokenExpired()) {
            getSysTokenFromPiTokenService();
        }
        return sysPiToken;
    }

    /**
     * Gets the sys token from pi token service.
     *
     * @throws AuthenticationException the authentication failed exception
     */
    private synchronized void getSysTokenFromPiTokenService() throws AuthenticationException {

        // get PI key by sending url , headers ,class and map to model class
        ResponseEntity<PiResponse> response = restClient
                .restExchange("https://int-piapi.stg-openclass.com/v1/piapi-int/tokens", null, new PiTokenRequest("groupwork_system",
                        "4zFdEpKj9HJt8gudp5nRxB@oUscFHef1"), HttpMethod.POST, PiResponse.class);

        int statusCode = response.getStatusCode().value();
        int HTTP_STATUS_200 = 200;
        int HTTP_STATUS_300 = 300;

        // FIXME: Why are we checking this instead of 201?
        if (statusCode <= HTTP_STATUS_200
                || statusCode > HTTP_STATUS_300
                || response.getBody() == null) {
            throw new AuthenticationException(sysPiToken);
        }

        sysPiToken = response.getBody().getData();
        sysPiTokenCreatedTIme = new Date();
    }
}