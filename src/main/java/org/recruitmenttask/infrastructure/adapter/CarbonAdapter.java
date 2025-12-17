package org.recruitmenttask.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.recruitmenttask.application.port.CarbonPort;
import org.recruitmenttask.domain.model.EnergyMixRange;
import org.recruitmenttask.infrastructure.adapter.mapper.CarbonMapper;
import org.recruitmenttask.infrastructure.dto.CarbonDto;
import org.recruitmenttask.infrastructure.repository.CarbonIntensityApiGateway;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

/**
 * Infrastructure adapter that implements {@link CarbonPort} to fetch energy data
 * from an external Carbon Intensity API and map it to the domain model.
 */
@Service
@RequiredArgsConstructor
public class CarbonAdapter implements CarbonPort {

    private final CarbonIntensityApiGateway carbonRepository;

    /**
     * Retrieves energy generation data for a specified time range and converts it to domain objects.
     *
     * @param from Start of the requested period.
     * @param to   End of the requested period.
     * @return {@link EnergyMixRange} containing the fetched and mapped data.
     */
    @Override
    public EnergyMixRange createMixRange(ZonedDateTime from, ZonedDateTime to) {
        CarbonDto carbonDto = carbonRepository.fetchGeneration(from, to);
        return CarbonMapper.toDomain(carbonDto);
    }
}
