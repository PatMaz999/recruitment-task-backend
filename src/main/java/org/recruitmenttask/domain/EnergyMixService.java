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
}
