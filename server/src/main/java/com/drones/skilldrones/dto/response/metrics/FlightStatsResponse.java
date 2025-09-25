package com.drones.skilldrones.dto.response.metrics;

import java.time.LocalDate;
import java.util.Map;

public record FlightStatsResponse(
    LocalDate startDate,
    LocalDate endDate,
    Integer totalFlights,
    Double averageDuration,
    Map<String, Long> flightsByRegion,
    Map<String, Long> flightsByDroneType,
    Map<LocalDate, Long> dailyFlights,
    PeakLoadInfo peakLoad
) {
    public record PeakLoadInfo(
        LocalDate date,
        String hour,
        Integer flightCount
    ) {}
}
