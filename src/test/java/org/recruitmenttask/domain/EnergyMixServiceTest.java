package org.recruitmenttask.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.recruitmenttask.application.port.CarbonPort;
import org.recruitmenttask.domain.model.EnergyMixRange;
import org.recruitmenttask.domain.model.EnergyMixTimestamp;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnergyMixServiceTest {

    @Mock
    private CarbonPort carbonPort;

    @Spy
    private Clock clock = Clock.fixed(MOCK_DATE_TIME.toInstant(), EnergyMixRange.TIME_ZONE);

    @InjectMocks
    private EnergyMixService energyService;

    private final static ZonedDateTime MOCK_DATE_TIME = ZonedDateTime.of(2025, 10, 15, 10, 0, 0, 0, EnergyMixRange.TIME_ZONE);

    @Test
    void shouldFetchDataForThreeDays() {
        // GIVEN
        LocalDate today = MOCK_DATE_TIME.toLocalDate();

        ZonedDateTime start = today.atTime(0, 1).atZone(EnergyMixRange.TIME_ZONE);
        ZonedDateTime end = today.plusDays(2).atTime(LocalTime.MAX).atZone(EnergyMixRange.TIME_ZONE);

        EnergyMixRange mockData = createMockDataForDays(today, 3);
        when(carbonPort.createMixRange(any(ZonedDateTime.class), any(ZonedDateTime.class))).thenReturn(mockData);

        // WHEN
        List<EnergyMixTimestamp> result = energyService.fetchCurrentThreeDays();

        // THEN
        verify(carbonPort, times(1)).createMixRange(start, end);

        assertEquals(3, result.size());

        assertEquals(today, result.get(0).getFrom().toLocalDate());
        assertEquals(today.plusDays(1), result.get(1).getFrom().toLocalDate());
        assertEquals(today.plusDays(2), result.get(2).getFrom().toLocalDate());
    }

    private EnergyMixRange createMockDataForDays(LocalDate startDate, int count) {
        List<EnergyMixTimestamp> timestamps = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            LocalDate day = startDate.plusDays(i);
            ZonedDateTime start = day.atTime(12, 0).atZone(EnergyMixRange.TIME_ZONE);

            timestamps.add(new EnergyMixTimestamp(
                    start, start.plusMinutes(30),
                    List.of(new EnergyMixTimestamp.EnergySource("Wind", 10.0)),
                    List.of(new EnergyMixTimestamp.EnergySource("Coal", 90.0))
            ));
        }
        return new EnergyMixRange(timestamps);
    }

}