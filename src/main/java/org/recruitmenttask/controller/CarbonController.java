package org.recruitmenttask.controller;

import lombok.RequiredArgsConstructor;
import org.recruitmenttask.dto.CarbonDto;
import org.recruitmenttask.repository.CarbonIntensityRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/energy-mix")
@RequiredArgsConstructor
public class CarbonController {

    private final CarbonIntensityRepository carbonRepository;

//    FIXME: test endpoint
    @GetMapping("/generation")
    public ResponseEntity<CarbonDto> generationMix(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return ResponseEntity.ok(carbonRepository.fetchGeneration(from, to));
    }

}
