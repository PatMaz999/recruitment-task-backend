package org.recruitmenttask.domain;

import lombok.RequiredArgsConstructor;
import org.recruitmenttask.application.port.CarbonPort;
import org.recruitmenttask.domain.model.EnergyMixRange;
import org.recruitmenttask.domain.model.EnergyMixTimestamp;
import org.recruitmenttask.exception.InvalidHourCountException;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

import static org.recruitmenttask.domain.model.EnergyMixRange.TIME_ZONE;

/**
 * Domain service handling business logic
 */
@Service
@RequiredArgsConstructor
public class EnergyMixService {

    private final CarbonPort carbonPort;
    private final Clock clock;

    /**
     * Fetches energy mix data for the current day and the following two days.
     * Groups data by day and returns a merged average for each.
     *
     * @return List of aggregated {@link EnergyMixTimestamp} objects for three days.
     */
    public List<EnergyMixTimestamp> fetchCurrentThreeDays() {
        LocalDate currentDate = LocalDate.now(clock);
        LocalTime firstHour = LocalTime.of(0, 1);
        ZonedDateTime start = currentDate.atTime(firstHour).atZone(TIME_ZONE);
        ZonedDateTime end = currentDate.plusDays(2)
                .atTime(LocalTime.MAX)
                .atZone(TIME_ZONE);

        return carbonPort.createMixRange(start, end)
                .splitRangeByDays().stream()
                .map(EnergyMixRange::mergeTimestamps).toList();
    }

    /**
     * Finds the continuous time window with the highest green energy percentage.
     *
     * @param hours Duration of the charging window (1-6 hours).
     * @return {@link EnergyMixRange} representing the optimal time window.
     * @throws InvalidHourCountException if hours are outside the 1-6 range.
     */
    public EnergyMixRange calcOptimalChargingTime(int hours) {
        if (hours < 1 || hours > 6)
            throw new InvalidHourCountException(hours);

        ZonedDateTime start = this.calcStartTime();

        LocalDate currentDate = start.toLocalDate();
        LocalTime lastHour = LocalTime.of(23, 59);
        ZonedDateTime end = currentDate.plusDays(2)
                .atTime(lastHour)
                .atZone(TIME_ZONE);

        EnergyMixRange mixRange = carbonPort.createMixRange(start, end);
        int startIndex = findOptimalFirstIndex(mixRange, hours);
        int endIndex = startIndex + (hours * 2);
        return mixRange.subRange(startIndex, endIndex);
    }

    /**
     * Finds the index of the most eco-friendly charging start using sliding window algorithm.
     */
    private int findOptimalFirstIndex(EnergyMixRange mixRange, int hours) {
        List<EnergyMixTimestamp> timestamps = mixRange.getTimestamps();

        int countOfRanges = hours * 2;

        int firstCurrentIndex = 0;

        int lastMaxIndex = countOfRanges - 1;

        double currentGreenPerc = timestamps.stream()
                .limit(countOfRanges)
                .mapToDouble(EnergyMixTimestamp::getTotalGreenPerc)
                .sum();
        double maxGreenPerc = currentGreenPerc;

        for (int i = countOfRanges; i < timestamps.size(); i++) {
            currentGreenPerc -= timestamps.get(firstCurrentIndex).getTotalGreenPerc();
            currentGreenPerc += timestamps.get(i).getTotalGreenPerc();
            firstCurrentIndex++;
            if (currentGreenPerc > maxGreenPerc) {
                maxGreenPerc = currentGreenPerc;
                lastMaxIndex = i;
            }
        }
        return lastMaxIndex - countOfRanges + 1;
    }

    /**
     * Calculates the nearest upcoming 30-minute time slot to start calculations from.
     */
    private ZonedDateTime calcStartTime() {
        ZonedDateTime now = ZonedDateTime.now(clock);

        LocalDate startDate = now.toLocalDate();
        int startHour = now.getHour();
        int startMinute = 0;

        if (now.getMinute() <= 30) {
            startMinute = 30;
        } else {
            if (startHour == 23) {
                startHour = 0;
                startDate = startDate.plusDays(1);
            } else {
                startHour++;
            }
        }

        return startDate.atTime(startHour, startMinute)
                .atZone(TIME_ZONE);
    }
}
