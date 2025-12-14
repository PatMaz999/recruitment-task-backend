package org.recruitmenttask.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.recruitmenttask.domain.model.EnergyMixRange;
import org.recruitmenttask.domain.model.EnergyMixTimestamp;
import org.recruitmenttask.infrastructure.adapter.CarbonAdapter;
import org.recruitmenttask.infrastructure.dto.CarbonDto;
import org.recruitmenttask.infrastructure.repository.CarbonIntensityRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/energy-mix")
@RequiredArgsConstructor
public class CarbonController {

    private final CarbonAdapter  carbonAdapter;

//    FIXME: test endpoint
    @GetMapping("/generation")
    public ResponseEntity<EnergyMixRange> generationMix(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return ResponseEntity.ok(carbonAdapter.createMixRange(from, to));
    }

    @GetMapping("/generation/as-one")
    public ResponseEntity<EnergyMixTimestamp> generationMixTimestamp(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return ResponseEntity.ok(carbonAdapter.createMixRange(from, to).mergeTimestamps());
    }

}
