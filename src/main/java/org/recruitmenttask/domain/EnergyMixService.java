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

@Service
@RequiredArgsConstructor
public class EnergyMixService {

    private final CarbonPort carbonPort;

    //    FIXME: last timestamp missing
    public List<EnergyMixTimestamp> fetchCurrentThreeDays() {
        LocalDate currentDate = LocalDate.now(TIME_ZONE);
        LocalTime firstHour = LocalTime.of(0, 1);
        ZonedDateTime start = currentDate.atTime(firstHour).atZone(TIME_ZONE);
        ZonedDateTime end = currentDate.plusDays(2)
                .atTime(LocalTime.MAX)
                .atZone(TIME_ZONE);

        return carbonPort.createMixRange(start, end)
                .splitRangeByDays().stream()
                .map(EnergyMixRange::mergeTimestamps).toList();
    }

    public EnergyMixRange calcOptimalChargingTime(int hours) {
        if (hours < 1 || hours > 6)
            throw new InvalidHourCountException(hours);

        ZonedDateTime start = this.calcStartTime();

        LocalDate currentDate = start.toLocalDate();
        LocalTime lastHour = LocalTime.of(23, 59);
//        FIXME: is timestamp from current to +2 days correct
        ZonedDateTime end = currentDate.plusDays(2)
                .atTime(lastHour)
                .atZone(TIME_ZONE);

        EnergyMixRange mixRange = carbonPort.createMixRange(start, end);
        int endIndex = findOptimalEndTimeIndex(mixRange, hours);
        int startIndex = endIndex - hours * 2;
//        TODO: endIndex +1?
        return mixRange.subRange(startIndex, endIndex);
    }

    private int findOptimalEndTimeIndex(EnergyMixRange mixRange, int hours) {
        List<EnergyMixTimestamp> timestamps = mixRange.getTimestamps();

        int countOfRanges = hours * 2;

        int firstCurrentIndex = 0;

        int lastMaxIndex = countOfRanges - 1;

        double maxGreenPerc = 0;
        double currentGreenPerc = timestamps.stream()
                .limit(countOfRanges)
                .mapToDouble(EnergyMixTimestamp::getTotalGreenPerc)
                .sum();

        for (int i = countOfRanges; i < timestamps.size(); i++) {
            currentGreenPerc -= timestamps.get(firstCurrentIndex).getTotalGreenPerc();
            currentGreenPerc += timestamps.get(i).getTotalGreenPerc();
            firstCurrentIndex++;
            if (currentGreenPerc > maxGreenPerc) {
                maxGreenPerc = currentGreenPerc;
                lastMaxIndex = i;
            }
        }
        return lastMaxIndex;
    }

    private ZonedDateTime calcStartTime() {
        ZonedDateTime now = ZonedDateTime.now(TIME_ZONE);

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
