package org.recruitmenttask.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.recruitmenttask.application.port.CarbonPort;
import org.recruitmenttask.domain.model.EnergyMixRange;
import org.recruitmenttask.domain.model.EnergyMixTimestamp;
import org.recruitmenttask.infrastructure.repository.CarbonIntensityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CarbonAdapter implements CarbonPort {
    private final CarbonIntensityRepository carbonRepository;

    @Override
    public EnergyMixTimestamp createMixTimestamp(LocalDateTime from, LocalDateTime to) {
//        TODO: implement method
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EnergyMixRange createMixRange(LocalDateTime from, LocalDateTime to) {
//        TODO: implement method
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
