package org.recruitmenttask.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.recruitmenttask.dto.CarbonTimestampDto;

import java.time.Duration;
import java.util.List;

public class EnergyMixTimestamp {

    private String from;
    private String to;
    private Duration timeRange;

    private List<Double> greenEnergyPerc;
    private List<Double> otherEnergyPerc;

    private double totalGreenPerc;
    private double totalOtherPerc;

}
