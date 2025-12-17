package org.recruitmenttask.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.recruitmenttask.application.port.CarbonPort;
import org.recruitmenttask.domain.model.EnergyMixRange;
import org.recruitmenttask.infrastructure.adapter.mapper.CarbonMapper;
import org.recruitmenttask.infrastructure.dto.CarbonDto;
import org.recruitmenttask.infrastructure.repository.CarbonIntensityRepository;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class CarbonAdapter implements CarbonPort {

    private final CarbonIntensityRepository carbonRepository;

    @Override
    public EnergyMixRange createMixRange(ZonedDateTime from, ZonedDateTime to) {
        CarbonDto carbonDto = carbonRepository.fetchGeneration(from, to);
        return CarbonMapper.toDomain(carbonDto);
    }
}
