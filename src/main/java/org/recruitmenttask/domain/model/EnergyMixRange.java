package org.recruitmenttask.domain.model;

import java.time.Duration;
import java.util.List;

public class EnergyMixRange {

    private String from;
    private String to;
    private Duration timestampRange;

    private int rangeSize;

    private List<EnergyMixTimestamp> timestamps;

}
