package com.drones.skilldrones.dto.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record FlightUploadRequest(
    @NotNull(message = "Файл обязателен для загрузки")
    MultipartFile file,
    
    String description
) {}
