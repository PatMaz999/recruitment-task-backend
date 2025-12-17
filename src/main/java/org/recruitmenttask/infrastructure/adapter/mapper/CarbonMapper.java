package org.recruitmenttask.infrastructure.adapter.mapper;

import org.recruitmenttask.domain.model.EnergyMixRange;
import org.recruitmenttask.domain.model.EnergyMixTimestamp;
import org.recruitmenttask.infrastructure.dto.CarbonDto;
import org.recruitmenttask.infrastructure.dto.CarbonTimestampDto;
import org.recruitmenttask.infrastructure.dto.EnergyTimestampDto;
import org.recruitmenttask.infrastructure.dto.OptimalChargingDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;
import java.util.stream.Collectors;

public class CarbonMapper {
    private CarbonMapper() {
    }

    private final static Set<String> greenEnergy = Set.of("biomass", "nuclear", "hydro", "wind", "solar");

    public static EnergyMixRange toDomain(CarbonDto carbonDto) {
        return new EnergyMixRange(carbonDto.getData().stream().map(CarbonMapper::toDomain).toList());
    }

    private static EnergyMixTimestamp toDomain(CarbonTimestampDto carbonTimestampDto) {
        return new EnergyMixTimestamp(
                carbonTimestampDto.getFrom(),
                carbonTimestampDto.getTo(),
                carbonTimestampDto.getGenerationMix().stream().filter(x -> greenEnergy.contains(x.fuel())).collect(Collectors.toList()),
                carbonTimestampDto.getGenerationMix().stream().filter(x -> !greenEnergy.contains(x.fuel())).collect(Collectors.toList())
        );
    }

    public static OptimalChargingDto toChargingDto(EnergyMixTimestamp timestamp) {
        return OptimalChargingDto.builder()
                .from(timestamp.getFrom())
                .to(timestamp.getTo())
                .greenPerc(BigDecimal
                        .valueOf(timestamp.getTotalGreenPerc())
                        .setScale(2, RoundingMode.HALF_UP))
                .build();
    }

    public static EnergyTimestampDto toEnergyTimestampDto(EnergyMixTimestamp timeStamp) {
        return EnergyTimestampDto.builder()
                .from(timeStamp.getFrom())
                .to(timeStamp.getTo())
                .greenEnergyPerc(timeStamp.getGreenEnergyPerc())
                .otherEnergyPerc(timeStamp.getOtherEnergyPerc())
                .totalGreenPerc(BigDecimal
                        .valueOf(timeStamp.getTotalGreenPerc())
                        .setScale(2, RoundingMode.HALF_UP))
                .build();
    }

}
