package org.recruitmenttask.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.recruitmenttask.domain.EnergyMixService;
import org.recruitmenttask.infrastructure.adapter.mapper.CarbonMapper;
import org.recruitmenttask.infrastructure.dto.EnergyTimestampDto;
import org.recruitmenttask.infrastructure.dto.OptimalChargingDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/energy-mix")
@RequiredArgsConstructor
public class CarbonController {

    private final EnergyMixService energyService;

    @GetMapping("/current-three-days")
    public ResponseEntity<List<EnergyTimestampDto>> generationMixDays() {
        return ResponseEntity.ok(energyService.fetchCurrentThreeDays()
                .stream()
                .map(CarbonMapper::toEnergyTimestampDto) // referencja do metody mappera
                .collect(Collectors.toList()));
    }

    @GetMapping("/optimal-charging")
    public ResponseEntity<OptimalChargingDto> optimalCharging(@RequestParam Integer hours) {
        return ResponseEntity.ok(CarbonMapper.toChargingDto(energyService.calcOptimalChargingTime(hours).mergeTimestamps()));
    }

}
