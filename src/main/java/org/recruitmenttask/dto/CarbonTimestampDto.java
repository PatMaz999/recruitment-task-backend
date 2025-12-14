package org.recruitmenttask.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CarbonTimestampDto {

    public record EnergySource(String fuel, double perc){}

    private String from;
    private String to;
    @JsonProperty("generationmix")
    private List<EnergySource> generationMix;
}
