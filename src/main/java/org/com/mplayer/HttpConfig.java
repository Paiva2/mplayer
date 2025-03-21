package org.com.mplayer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpConfig {

    @Bean
    public RestTemplate rest() {
        RestTemplate template = new RestTemplate();

        template.getInterceptors().add((request, body, execution) -> {
            if (!request.getURI().getPath().startsWith("/api/v1/")) {
                return execution.execute(request, body);
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                return execution.execute(request, body);
            }

            String token = (String) authentication.getCredentials();

            if (token == null) {
                return execution.execute(request, body);
            }
            request.getHeaders().setBearerAuth(token);
            return execution.execute(request, body);
        });

        template.getMessageConverters().removeIf(c -> c instanceof MappingJackson2XmlHttpMessageConverter);
        template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        return template;
    }
}
