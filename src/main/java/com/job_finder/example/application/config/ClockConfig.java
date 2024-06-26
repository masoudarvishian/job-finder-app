package com.job_finder.example.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.TimeZone;

@Configuration
public class ClockConfig {

    @Bean
    public Clock getClockUTC() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        return Clock.systemUTC();
    }
}
