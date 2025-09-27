package com.drones.skilldrones.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ErrorResponse(LocalDateTime timestamp,
                            int status,
                            String error,
                            String message,
                            String path,
                            Map<String, String> fieldErrors,
                            List<String> globalErrors
) {
    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status,
                error,
                message,
                path,
                Map.of(),
                List.of()
        );
    }

    public static ErrorResponse validationError(
            int status,
            String message,
            String path,
            Map<String, String> fieldErrors
    ) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status,
                "Validation Failed",
                message,
                path,
                fieldErrors,
                List.of()
        );
    }
}
