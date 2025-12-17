package org.recruitmenttask.config;

import org.recruitmenttask.exception.RestClientException;
import org.recruitmenttask.exception.ServiceIsUnavailableException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    /**
     * Creates a configured {@link RestClient} for the Carbon Intensity API.
     * Includes a default status handler to translate HTTP error codes into domain-specific exceptions.
     *
     * @throws RestClientException           if a 4xx Client Error occurs during the API call.
     * @throws ServiceIsUnavailableException if a 5xx Server Error occurs during the API call.
     */
    @Bean
    public RestClient carbonClient() {
        return RestClient.builder().baseUrl("https://api.carbonintensity.org.uk")
                .defaultStatusHandler(
                        HttpStatusCode::isError,
                        (request, response) -> {
                            HttpStatusCode statusCode = response.getStatusCode();
                            if (statusCode.is4xxClientError())
                                throw new RestClientException();
                            if (statusCode.is5xxServerError())
                                throw new ServiceIsUnavailableException();
                        }
                )
                .build();
    }
}