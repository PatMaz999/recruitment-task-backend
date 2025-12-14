package org.recruitmenttask.infrastructure.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CarbonDto {

    private List<CarbonTimestampDto> data;

}
