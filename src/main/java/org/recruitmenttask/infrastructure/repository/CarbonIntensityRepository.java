package org.recruitmenttask.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.recruitmenttask.infrastructure.dto.CarbonDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class CarbonIntensityRepository {
    private final RestClient carbonClient;

    public CarbonDto fetchGeneration(ZonedDateTime from, ZonedDateTime to) {
        return carbonClient.get()
                .uri("/generation/" + from + "/" + to)
                .retrieve().body(CarbonDto.class);
    }

}
