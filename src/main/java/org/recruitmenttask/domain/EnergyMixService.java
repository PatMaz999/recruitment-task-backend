package org.recruitmenttask.domain;

import lombok.RequiredArgsConstructor;
import org.recruitmenttask.application.port.CarbonPort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnergyMixService {

    private final CarbonPort carbonPort;
}
