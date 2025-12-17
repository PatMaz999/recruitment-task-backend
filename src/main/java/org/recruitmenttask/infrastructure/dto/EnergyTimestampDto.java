package org.recruitmenttask.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.recruitmenttask.domain.model.EnergyMixTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class EnergyTimestampDto {
    private ZonedDateTime from;
    private ZonedDateTime to;

    private List<EnergyMixTimestamp.EnergySource> greenEnergyPerc;
    private List<EnergyMixTimestamp.EnergySource> otherEnergyPerc;

    private BigDecimal totalGreenPerc;
}
