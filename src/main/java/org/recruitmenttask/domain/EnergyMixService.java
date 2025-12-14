package org.recruitmenttask.domain;

import lombok.RequiredArgsConstructor;
import org.recruitmenttask.application.port.CarbonPort;
import org.recruitmenttask.domain.model.EnergyMixRange;
import org.recruitmenttask.domain.model.EnergyMixTimestamp;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnergyMixService {

    private final CarbonPort carbonPort;

    //    FIXME: last timestamp missing
    public List<EnergyMixTimestamp> fetchCurrentThreeDays() {
        LocalDate currentDate = LocalDate.now();
        LocalTime firstHour = LocalTime.of(0, 1);
        LocalTime lastHour = LocalTime.of(23, 59);
        LocalDateTime start = LocalDateTime.of(currentDate.minusDays(1), firstHour);
        LocalDateTime end = LocalDateTime.of(currentDate.plusDays(1), lastHour);

        return carbonPort.createMixRange(start, end)
                .splitRangeByDays().stream()
                .map(EnergyMixRange::mergeTimestamps).toList();
    }

    public EnergyMixRange calcOptimalChargingTime(int hours) {
//        TODO: handle exception
        if (hours < 1 || hours > 6)
            throw new IllegalArgumentException("Hours must be between 1 and 6");

        LocalDateTime start = LocalDateTime.now();

        LocalDate currentDate = start.toLocalDate();
        LocalTime lastHour = LocalTime.of(23, 59);
        LocalDateTime end = LocalDateTime.of(currentDate.plusDays(1), lastHour);

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
}
