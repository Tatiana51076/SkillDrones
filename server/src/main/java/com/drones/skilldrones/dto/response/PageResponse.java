package com.drones.skilldrones.dto.response;

import java.util.List;

public record PageResponse<T>(List<T> content,
                              int pageNumber,
                              int pageSize,
                              long totalElements,
                              int totalPages,
                              boolean first,
                              boolean last
) {
    public static <T> PageResponse<T> of(List<T> content, int pageNumber, int pageSize, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        return new PageResponse<>(
                content,
                pageNumber,
                pageSize,
                totalElements,
                totalPages,
                pageNumber == 0,
                pageNumber >= totalPages - 1
        );
    }
}
