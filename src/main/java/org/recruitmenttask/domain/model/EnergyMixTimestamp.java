package org.recruitmenttask.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Represents energy mix data for a specific interval.
 * Calculates total percentages for green and non-green energy sources upon initialization.
 */
@Getter
@Setter
public class EnergyMixTimestamp {

    /**
     * Initializes the timestamp and calculates total percentage values.
     *
     * @param from        Starting time of the interval.
     * @param to          Ending time of the interval.
     * @param greenEnergy List of renewable energy sources.
     * @param otherEnergy List of non-renewable energy sources.
     */
    public EnergyMixTimestamp(ZonedDateTime from, ZonedDateTime to, List<EnergySource> greenEnergy, List<EnergySource> otherEnergy) {
        this.from = from;
        this.to = to;

        this.greenEnergyPerc = greenEnergy;
        this.otherEnergyPerc = otherEnergy;
        this.totalGreenPerc = greenEnergy.stream().mapToDouble(x -> x.perc).sum();
        this.totalOtherPerc = otherEnergy.stream().mapToDouble(x -> x.perc).sum();
    }

    /**
     * Record representing a single energy source.
     *
     * @param fuel Name of the fuel type.
     * @param perc Percentage share of the total mix.
     */
    public record EnergySource(String fuel, double perc) {
    }

    private ZonedDateTime from;
    private ZonedDateTime to;

    private List<EnergySource> greenEnergyPerc;
    private List<EnergySource> otherEnergyPerc;

    private double totalGreenPerc;
    private double totalOtherPerc;
}
