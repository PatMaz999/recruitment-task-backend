package org.recruitmenttask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Bean
    public RestClient carbonClient() {
        return RestClient.builder().baseUrl("https://api.carbonintensity.org.uk").build();
    }
}