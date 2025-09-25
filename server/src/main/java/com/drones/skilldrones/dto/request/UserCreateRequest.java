package com.drones.skilldrones.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreateRequest(
    @NotBlank(message = "Роль пользователя обязательна")
    String role,
    String email
) {}
