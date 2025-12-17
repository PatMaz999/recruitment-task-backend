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

    /**
     * Retrieves the average energy mix for today and the next two days.
     * @return A list of {@link EnergyTimestampDto} representing daily averages.
     */
    @GetMapping("/current-three-days")
    public ResponseEntity<List<EnergyTimestampDto>> generationMixDays() {
        return ResponseEntity.ok(energyService.fetchCurrentThreeDays()
                .stream()
                .map(CarbonMapper::toEnergyTimestampDto) // referencja do metody mappera
                .collect(Collectors.toList()));
    }

    /**
     * Calculates the most eco-friendly time window for car charging based on duration.
     * @param hours The required charging duration in hours (1-6).
     * @return {@link OptimalChargingDto} containing the start time, end time, and green energy percentage.
     */
    @GetMapping("/optimal-charging")
    public ResponseEntity<OptimalChargingDto> optimalCharging(@RequestParam Integer hours) {
        return ResponseEntity.ok(CarbonMapper.toChargingDto(energyService.calcOptimalChargingTime(hours).mergeTimestamps()));
    }

}
