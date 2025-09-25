package com.drones.skilldrones.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReportRequest(
    @NotNull(message = "Дата начала обязательна")
    LocalDate startDate,
    
    @NotNull(message = "Дата окончания обязательна")
    LocalDate endDate,
    
    String reportType,
    String parameters
) {}
