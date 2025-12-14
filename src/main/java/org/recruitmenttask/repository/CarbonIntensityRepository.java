package org.recruitmenttask.repository;

import lombok.RequiredArgsConstructor;
import org.recruitmenttask.dto.CarbonDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CarbonIntensityRepository {
    private final RestClient carbonClient;
    private final ObjectMapper objMapper;

    public CarbonDto fetchGeneration(LocalDateTime from, LocalDateTime to) {
        return carbonClient.get()
                .uri("/generation/" + from + "/" + to)
                .retrieve().body(CarbonDto.class);
    }

}
