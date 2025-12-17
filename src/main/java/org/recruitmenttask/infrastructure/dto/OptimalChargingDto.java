package org.recruitmenttask.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
public class OptimalChargingDto {
    private ZonedDateTime from;
    private ZonedDateTime to;
    private double greenPerc;

}
