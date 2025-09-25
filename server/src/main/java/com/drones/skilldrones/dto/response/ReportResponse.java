package com.drones.skilldrones.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReportResponse(
    Long reportId,
    Long userId,
    String reportType,
    LocalDate reportPeriodStart,
    LocalDate reportPeriodEnd,
    String parameters,
    String status,
    String filePath,
    String errorMessage,
    LocalDateTime createdAt,
    LocalDateTime completedAt
) {}
