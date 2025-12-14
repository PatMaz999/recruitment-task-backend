package org.recruitmenttask.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class EnergyMixTimestamp {

    public EnergyMixTimestamp(LocalDateTime from, LocalDateTime to, List<EnergySource> greenEnergy, List<EnergySource> otherEnergy) {
        this.from = from;
        this.to = to;
        this.timestampRange = Duration.between(from, to);

        this.greenEnergyPerc = greenEnergy;
        this.otherEnergyPerc = otherEnergy;
        this.totalGreenPerc = greenEnergy.stream().mapToDouble(x -> x.perc).sum();
        this.totalOtherPerc = otherEnergy.stream().mapToDouble(x -> x.perc).sum();
    }

    public record EnergySource(String fuel, double perc) {
    }

    private LocalDateTime from;
    private LocalDateTime to;
    private Duration timestampRange;

    private List<EnergySource> greenEnergyPerc;
    private List<EnergySource> otherEnergyPerc;

    private double totalGreenPerc;
    private double totalOtherPerc;
}
