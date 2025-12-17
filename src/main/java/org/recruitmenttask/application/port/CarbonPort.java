package org.recruitmenttask.application.port;

import org.recruitmenttask.domain.model.EnergyMixRange;

import java.time.ZonedDateTime;

public interface CarbonPort {
    EnergyMixRange createMixRange(ZonedDateTime from, ZonedDateTime to);
}
