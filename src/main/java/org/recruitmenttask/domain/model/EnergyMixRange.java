package org.recruitmenttask.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Domain model representing a time range of energy mix data.
 */
@Getter
@Setter
public class EnergyMixRange {

    /**
     * Initializes the range and sets boundaries based on the provided timestamps.
     *
     * @param timestamps Non-empty list of {@link EnergyMixTimestamp}.
     * @throws IllegalArgumentException if list is empty.
     */
    public EnergyMixRange(List<EnergyMixTimestamp> timestamps) {
        if (timestamps.isEmpty())
            throw new IllegalArgumentException("Range should contain at least one timestamp");
        this.timestamps = timestamps;
        this.from = timestamps.getFirst().getFrom();
        this.to = timestamps.getLast().getTo();
    }

    public final static ZoneId TIME_ZONE = ZoneOffset.UTC;

    private ZonedDateTime from;
    private ZonedDateTime to;

    private List<EnergyMixTimestamp> timestamps;

    /**
     * Extracts a sub-list of timestamps into a new range.
     *
     * @param from Start index (inclusive).
     * @param to   End index (exclusive).
     * @return A new {@link EnergyMixRange} instance.
     */
    public EnergyMixRange subRange(int from, int to) {
        List<EnergyMixTimestamp> newTimestamps = new ArrayList<>(this.timestamps.subList(from, to));
        return new EnergyMixRange(newTimestamps);
    }

    /**
     * Splits the current range into separate ranges grouped by calendar days.
     *
     * @return List of {@link EnergyMixRange} objects, one for each day.
     */
    public List<EnergyMixRange> splitRangeByDays() {
        List<EnergyMixRange> ranges = new ArrayList<>();
        LocalTime lastHour = LocalTime.of(23, 59);

        LocalDate start = from.toLocalDate();
        LocalDate end = to.toLocalDate();
        long numberOfDays = start.until(end, ChronoUnit.DAYS);
        if (this.to.getHour() == 0)
            numberOfDays--;
        for (int i = 0; i <= numberOfDays; i++) {

            LocalDate onlyDate = from.toLocalDate().plusDays(i);
            ZonedDateTime startOfDay = onlyDate.atStartOfDay(TIME_ZONE);
            ZonedDateTime endOfDay = onlyDate.atTime(lastHour).atZone(TIME_ZONE);

            List<EnergyMixTimestamp> timestampsOfSpecificDay = timestamps.stream()
                    .filter(x ->
                            !x.getFrom().isBefore(startOfDay) && x.getFrom().isBefore(endOfDay)
                    ).collect(Collectors.toList());
            ranges.add(new EnergyMixRange(timestampsOfSpecificDay));
        }
        return ranges;
    }

    /**
     * Merges all timestamps in the range into a single average timestamp.
     * Calculates the arithmetic mean for each energy source.
     *
     * @return A single aggregated {@link EnergyMixTimestamp}.
     */
    public EnergyMixTimestamp mergeTimestamps() {

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
        this.countAvg(greenEnergy, countOfGreenSources);

        for (int i = 1; i < timestamps.size(); i++) {
            for (int j = 0; j < countOfOtherSources; j++) {
                double other = this.timestamps.get(i).getOtherEnergyPerc().get(j).perc();
                otherEnergy.set(j, new EnergyMixTimestamp.EnergySource(otherEnergy.get(j).fuel(), otherEnergy.get(j).perc() + other));
            }
        }
        this.countAvg(otherEnergy, countOfOtherSources);
        return new EnergyMixTimestamp(this.from, this.to, greenEnergy, otherEnergy);
    }

    /**
     * Calculates averages for a list of energy sources and rounds them to 2 decimal places.
     *
     * @param energySources       List to process.
     * @param countOfGreenSources Number of energy sources.
     */
    private void countAvg(List<EnergyMixTimestamp.EnergySource> energySources, int countOfGreenSources) {
        for (int j = 0; j < countOfGreenSources; j++) {
            EnergyMixTimestamp.EnergySource current = energySources.get(j);
            double value = current.perc() / timestamps.size();
            energySources.set(j, new EnergyMixTimestamp.EnergySource(current.fuel(), Math.round(value * 100.0) / 100.0));
        }
    }
}
