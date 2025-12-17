package org.recruitmenttask.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.recruitmenttask.infrastructure.dto.CarbonDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.ZonedDateTime;

/**
 * API Gateway responsible for direct communication with external service.
 * Uses {@link RestClient} to perform HTTP GET requests for energy generation data.
 */
@Service
@RequiredArgsConstructor
public class CarbonIntensityApiGateway {
    private final RestClient carbonClient;

    /**
     * Executes a network call to fetch generation mix data for a specific time interval.
     *
     * @param from Start timestamp in ISO 8601 format.
     * @param to   End timestamp in ISO 8601 format.
     * @return {@link CarbonDto} containing the raw data from the external provider.
     */
    public CarbonDto fetchGeneration(ZonedDateTime from, ZonedDateTime to) {
        return carbonClient.get()
                .uri("/generation/" + from + "/" + to)
                .retrieve().body(CarbonDto.class);
    }

}
