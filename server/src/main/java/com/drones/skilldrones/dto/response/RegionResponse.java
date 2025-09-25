package com.drones.skilldrones.dto.response;

import java.time.LocalDateTime;

public record RegionResponse(
    Long regionId,
    String name,
    Double areaKm2,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
