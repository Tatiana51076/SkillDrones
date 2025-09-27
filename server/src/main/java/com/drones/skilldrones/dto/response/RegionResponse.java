package com.drones.skilldrones.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Ответ с информацией о регионе")
public record RegionResponse(
        @Schema(description = "ID региона", example = "1")
        Long regionId,
        @Schema(description = "Название региона", example = "Московская область")
        String name,
        @Schema(description = "Площадь региона в км²", example = "44300.5")
        Double areaKm2,
        @Schema(description = "Дата создания")
        LocalDateTime createdAt,
        @Schema(description = "Дата изменения")
        LocalDateTime updatedAt
) {
}
