package org.recruitmenttask.application.port;

import org.recruitmenttask.domain.model.EnergyMixRange;
import org.recruitmenttask.domain.model.EnergyMixTimestamp;

import java.time.LocalDateTime;

public interface CarbonPort {
    EnergyMixTimestamp createMixTimestamp(LocalDateTime from, LocalDateTime to);
    EnergyMixRange createMixRange(LocalDateTime from, LocalDateTime to);
}
