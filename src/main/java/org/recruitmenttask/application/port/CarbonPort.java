package org.recruitmenttask.application.port;

import org.recruitmenttask.domain.model.EnergyMixRange;

import java.time.LocalDateTime;

public interface CarbonPort {
    EnergyMixRange createMixRange(LocalDateTime from, LocalDateTime to);
}
