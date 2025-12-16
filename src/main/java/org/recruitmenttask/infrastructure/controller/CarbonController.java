package org.recruitmenttask.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.recruitmenttask.domain.EnergyMixService;
import org.recruitmenttask.domain.model.EnergyMixTimestamp;
import org.recruitmenttask.infrastructure.adapter.mapper.CarbonMapper;
import org.recruitmenttask.infrastructure.dto.OptimalChargingDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/energy-mix")
@RequiredArgsConstructor
public class CarbonController {

    private final EnergyMixService energyService;

    @GetMapping("/current-three-days")
    public ResponseEntity<List<EnergyMixTimestamp>> generationMixDays() {
        return ResponseEntity.ok(energyService.fetchCurrentThreeDays());
    }

    @GetMapping("/optimal-charging")
    public ResponseEntity<OptimalChargingDto> optimalCharging(@RequestParam Integer hours) {
        return ResponseEntity.ok(CarbonMapper.toChargingDto(energyService.calcOptimalChargingTime(hours).mergeTimestamps()));
    }

}
