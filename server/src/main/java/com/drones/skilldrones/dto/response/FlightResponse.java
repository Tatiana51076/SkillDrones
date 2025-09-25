package com.drones.skilldrones.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public record FlightResponse(
    Long flightId,
    Integer droneId,
    Long rawId,
    String flightCode,
    String droneType,
    String droneRegistration,
    LocalDate flightDate,
    LocalTime departureTime,
    LocalTime arrivalTime,
    Integer durationMinutes,
    String departureCoords,
    String arrivalCoords,
    Long departureRegionId,
    Long arrivalRegionId,
    String departureRegionName,
    String arrivalRegionName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
