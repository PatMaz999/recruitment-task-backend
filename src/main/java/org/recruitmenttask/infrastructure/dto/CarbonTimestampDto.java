package org.recruitmenttask.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.recruitmenttask.domain.model.EnergyMixTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CarbonTimestampDto {

    private LocalDateTime from;
    private LocalDateTime to;
    @JsonProperty("generationmix")
    private List<EnergyMixTimestamp.EnergySource> generationMix;
}
