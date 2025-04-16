package org.com.mplayer.player.infra.adapter.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.mplayer.player.domain.ports.out.external.UserExternalIntegrationPort;
import org.com.mplayer.player.domain.ports.out.external.dto.FindUserExternalProfileOutputPort;
import org.com.mplayer.player.infra.adapter.data.exception.ExternalErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserExternalIntegrationAdapter implements UserExternalIntegrationPort {
    private final static ObjectMapper mapper = new ObjectMapper();

    @Value("${services.endpoint.user}")
    private String userServiceEndpoint;

    private final RestTemplate restTemplate;

    @Override
    public FindUserExternalProfileOutputPort findByExternalId() {
        try {
            String url = userServiceEndpoint.concat("/profile");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<String> profileResponse = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

            return mapper.readValue(profileResponse.getBody(), FindUserExternalProfileOutputPort.class);
        } catch (HttpServerErrorException e) {
            String message = "[UserExternalIntegrationAdapter] Error: " + e.getMessage();
            log.error(message);

            throw new ExternalErrorException("Error while fetching user profile...");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
