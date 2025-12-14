package org.recruitmenttask.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class OptimalChargingDto {
    private LocalDateTime from;
    private LocalDateTime to;
    private double greenPerc;

}
