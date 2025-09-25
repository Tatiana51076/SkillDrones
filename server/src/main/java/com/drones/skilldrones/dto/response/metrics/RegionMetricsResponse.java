package com.drones.skilldrones.dto.response.metrics;

import java.time.LocalDate;

public record RegionMetricsResponse(
    Long regionId,
    String regionName,
    LocalDate metricDate,
    String metricType,
    Integer totalFlights,
    Double avgDurationMinutes,
    Integer peakHourlyFlights,
    Double flightDensity,
    Integer zeroDaysCount,
    Double growthPercentage,
    LocalDateTime calculatedAt
) {}
