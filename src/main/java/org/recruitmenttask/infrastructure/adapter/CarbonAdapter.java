package org.recruitmenttask.infrastructure.adapter;

import org.recruitmenttask.application.port.CarbonPort;
import org.recruitmenttask.domain.model.EnergyMixRange;
import org.recruitmenttask.domain.model.EnergyMixTimestamp;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CarbonAdapter implements CarbonPort {
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
