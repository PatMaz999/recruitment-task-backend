package org.recruitmenttask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

import static org.recruitmenttask.domain.model.EnergyMixRange.TIME_ZONE;

@Configuration
public class DateTimeConfig {
    @Bean
    public Clock clock(){
        return Clock.system(TIME_ZONE);
    }
}
