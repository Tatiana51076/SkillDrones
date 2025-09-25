package com.drones.skilldrones.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record UploadResultResponse(
    int totalRecords,
    int successfulRecords,
    int failedRecords,
    List<String> errors,
    LocalDateTime processedAt
) {}
