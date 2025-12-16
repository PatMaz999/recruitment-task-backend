package org.recruitmenttask.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class EnergyMixRange {

    public EnergyMixRange(List<EnergyMixTimestamp> timestamps) {
        this.timestamps = timestamps;
        this.from = timestamps.getFirst().getFrom();
        this.to = timestamps.getLast().getTo();
    }

    private LocalDateTime from;
    private LocalDateTime to;

    private List<EnergyMixTimestamp> timestamps;

    public EnergyMixRange subRange(int from, int to) {
        List<EnergyMixTimestamp> newTimestamps = new ArrayList<>(this.timestamps.subList(from, to));
        return new EnergyMixRange(newTimestamps);
    }

    public List<EnergyMixRange> splitRangeByDays() {
        List<EnergyMixRange> ranges = new ArrayList<>();
        LocalTime lastHour = LocalTime.of(23, 59);
        LocalTime firstHour = LocalTime.of(0, 0);

        LocalDate start = from.toLocalDate();
        LocalDate end = to.toLocalDate();
        long numberOfDays = start.until(end, ChronoUnit.DAYS);
        for (int i = 0; i <= numberOfDays; i++) {

            LocalDate onlyDate = from.plusDays(i).toLocalDate();
            LocalDateTime startOfDay = onlyDate.atTime(firstHour);
            LocalDateTime endOfDay = onlyDate.atTime(lastHour);

            List<EnergyMixTimestamp> timestampsOfSpecificDay = timestamps.stream()
                    .filter(x ->
                            x.getFrom().isBefore(endOfDay) && (x.getFrom().isAfter(startOfDay) || x.getFrom().isEqual(startOfDay))
                    ).collect(Collectors.toList());
            ranges.add(new EnergyMixRange(timestampsOfSpecificDay));
        }
        return ranges;
    }

    //    TODO: need refactor
    public EnergyMixTimestamp mergeTimestamps() {
        LocalDateTime from = this.from;
        LocalDateTime to = this.to;

        List<EnergyMixTimestamp.EnergySource> greenEnergy = new ArrayList<>(this.timestamps.getFirst().getGreenEnergyPerc());
        List<EnergyMixTimestamp.EnergySource> otherEnergy = new ArrayList<>(this.timestamps.getFirst().getOtherEnergyPerc());

        int countOfGreenSources = this.timestamps.getFirst().getGreenEnergyPerc().size();
        int countOfOtherSources = this.timestamps.getFirst().getOtherEnergyPerc().size();

        for (int i = 1; i < timestamps.size(); i++) {
            for (int j = 0; j < countOfGreenSources; j++) {
                double green = this.timestamps.get(i).getGreenEnergyPerc().get(j).perc();
                greenEnergy.set(j, new EnergyMixTimestamp.EnergySource(greenEnergy.get(j).fuel(), greenEnergy.get(j).perc() + green));
            }
        }
        for (int j = 0; j < countOfGreenSources; j++) {
            EnergyMixTimestamp.EnergySource current = greenEnergy.get(j);
            greenEnergy.set(j, new EnergyMixTimestamp.EnergySource(current.fuel(), current.perc() / timestamps.size()));
        }

        for (int i = 1; i < timestamps.size(); i++) {
            for (int j = 0; j < countOfOtherSources; j++) {
                double other = this.timestamps.get(i).getOtherEnergyPerc().get(j).perc();
                otherEnergy.set(j, new EnergyMixTimestamp.EnergySource(otherEnergy.get(j).fuel(), otherEnergy.get(j).perc() + other));
            }
        }
        for (int j = 0; j < countOfOtherSources; j++) {
            EnergyMixTimestamp.EnergySource current = otherEnergy.get(j);
            otherEnergy.set(j, new EnergyMixTimestamp.EnergySource(current.fuel(), current.perc() / timestamps.size()));
        }
        return new EnergyMixTimestamp(from, to, greenEnergy, otherEnergy);
    }
}
