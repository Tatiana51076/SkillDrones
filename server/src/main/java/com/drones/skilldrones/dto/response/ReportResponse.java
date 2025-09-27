package com.drones.skilldrones.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Ответ с информацией об отчете")
public record ReportResponse(
        @Schema(description = "ID отчета", example = "1")
        Long reportId,

        @Schema(description = "ID пользователя, создавшего отчет", example = "123")
        Long userId,

        @Schema(description = "Тип отчета", example = "REGIONAL",
                allowableValues = {"REGIONAL", "COMPREHENSIVE", "TOP_REGIONS", "CHART", "TIME_SERIES"})
        String reportType,

        @Schema(description = "Начало периода отчета", example = "2024-01-01")
        LocalDate reportPeriodStart,

        @Schema(description = "Конец периода отчета", example = "2024-01-31")
        LocalDate reportPeriodEnd,

        @Schema(description = "Параметры отчета в JSON формате",
                example = "{\"chartType\": \"bar\", \"includeDetails\": true}")
        String parameters,

        @Schema(description = "Статус отчета", example = "COMPLETED",
                allowableValues = {"PENDING", "PROCESSING", "COMPLETED", "FAILED"})
        String status,

        @Schema(description = "Путь к файлу отчета", example = "/tmp/report_123.pdf")
        String filePath,

        @Schema(description = "Сообщение об ошибке", example = "Ошибка генерации графика")
        String errorMessage,

        @Schema(description = "Дата и время создания отчета", example = "2024-01-25T10:30:00")
        LocalDateTime createdAt,

        @Schema(description = "Дата и время завершения отчета", example = "2024-01-25T10:35:00")
        LocalDateTime completedAt
) {}
