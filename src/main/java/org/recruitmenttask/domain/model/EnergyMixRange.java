package org.recruitmenttask.domain.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class EnergyMixRange {

    public EnergyMixRange(List<EnergyMixTimestamp> timestamps) {
        this.timestamps = timestamps;
        from = timestamps.getFirst().getFrom();
        to = timestamps.getLast().getTo();
        totalRange = Duration.between(from, to);
    }

    private LocalDateTime from;
    private LocalDateTime to;
    private Duration totalRange;

    private List<EnergyMixTimestamp> timestamps;

    public int getTimestampsCount() {
        return timestamps.size();
    }
}
