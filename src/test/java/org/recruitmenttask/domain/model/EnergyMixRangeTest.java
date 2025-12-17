package org.recruitmenttask.domain.model;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnergyMixRangeTest {

    private final ZonedDateTime MOCK_DATE = ZonedDateTime.of(2025, 10, 15, 10, 0, 0, 0, EnergyMixRange.TIME_ZONE);

    @Test
    void shouldCreateObjectWithCorrectBoundaries() {
        // GIVEN
        EnergyMixTimestamp t1 = new EnergyMixTimestamp(MOCK_DATE, MOCK_DATE.plusMinutes(30), List.of(), List.of());
        EnergyMixTimestamp t2 = new EnergyMixTimestamp(MOCK_DATE.plusMinutes(30), MOCK_DATE.plusMinutes(60), List.of(), List.of());
        EnergyMixTimestamp t3 = new EnergyMixTimestamp(MOCK_DATE.plusMinutes(60), MOCK_DATE.plusMinutes(90), List.of(), List.of());

        List<EnergyMixTimestamp> inputList = List.of(t1, t2, t3);

        // WHEN
        EnergyMixRange range = new EnergyMixRange(inputList);

        // THEN
        assertEquals(t1.getFrom(), range.getFrom(), "Start of range should match the start of the first timestamp");
        assertEquals(t3.getTo(), range.getTo(), "End of range should match the end of the last timestamp");

        assertEquals(inputList, range.getTimestamps());
        assertEquals(3, range.getTimestamps().size());
    }

    @Test
    void shouldCreateObjectFromSingleTimestamp() {
        // GIVEN
        EnergyMixTimestamp t1 = new EnergyMixTimestamp(MOCK_DATE, MOCK_DATE.plusMinutes(30), List.of(), List.of());
        List<EnergyMixTimestamp> inputList = List.of(t1);

        // WHEN
        EnergyMixRange range = new EnergyMixRange(inputList);

        // THEN
        assertEquals(t1.getFrom(), range.getFrom());
        assertEquals(t1.getTo(), range.getTo());
    }

    @Test
    void shouldThrowWhenTimestampIsEmpty() {
        // GIVEN
        List<EnergyMixTimestamp> inputList = List.of();

        // WHEN THEN
        assertThrows(IllegalArgumentException.class, () -> new EnergyMixRange(inputList));
    }

    @Test
    void shouldReturnListOfTwoRanges(){
        //GIVEN
        ZonedDateTime firstDay = ZonedDateTime.of(2025, 10, 15, 23, 0, 0, 0, EnergyMixRange.TIME_ZONE);

        //first day
        EnergyMixTimestamp t1 = new EnergyMixTimestamp(firstDay, firstDay.plusMinutes(30), List.of(), List.of());
        EnergyMixTimestamp t2 = new EnergyMixTimestamp(firstDay.plusMinutes(30), firstDay.plusMinutes(60), List.of(), List.of());
        //second day
        EnergyMixTimestamp t3 = new EnergyMixTimestamp(firstDay.plusMinutes(60), firstDay.plusMinutes(90), List.of(), List.of());
        EnergyMixTimestamp t4 = new EnergyMixTimestamp(firstDay.plusMinutes(90), firstDay.plusMinutes(120), List.of(), List.of());

        List<EnergyMixTimestamp> inputList = List.of(t1, t2, t3, t4);
        EnergyMixRange range = new EnergyMixRange(inputList);

        //WHEN
        List<EnergyMixRange> rangeList = range.splitRangeByDays();

        //THEN
        assertEquals(List.of(t1,t2), rangeList.getFirst().getTimestamps());
        assertEquals(List.of(t3,t4), rangeList.getLast().getTimestamps());
    }

    @Test
    void shouldReturnSingleRangeWithBoundaryHours(){
        //GIVEN
        ZonedDateTime firstDay = ZonedDateTime.of(2025, 10, 15, 23, 0, 0, 0, EnergyMixRange.TIME_ZONE);

        //first day
        EnergyMixTimestamp t1 = new EnergyMixTimestamp(firstDay, firstDay.plusMinutes(30), List.of(), List.of());
        EnergyMixTimestamp t2 = new EnergyMixTimestamp(firstDay.plusMinutes(30), firstDay.plusMinutes(60), List.of(), List.of());

        List<EnergyMixTimestamp> inputList = List.of(t1, t2);
        EnergyMixRange range = new EnergyMixRange(inputList);

        //WHEN
        List<EnergyMixRange> rangeList = range.splitRangeByDays();

        //THEN
        assertEquals(List.of(t1,t2), rangeList.getFirst().getTimestamps());
        assertEquals(1, rangeList.size());
    }

}