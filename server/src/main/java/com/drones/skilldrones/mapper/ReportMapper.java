package com.drones.skilldrones.mapper;

import com.drones.skilldrones.dto.response.ReportResponse;
import com.drones.skilldrones.model.ReportLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ReportMapper {
    @Mapping(source = "reportId", target = "reportId")
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "reportType", target = "reportType")
    @Mapping(source = "reportPeriodStart", target = "reportPeriodStart")
    @Mapping(source = "reportPeriodEnd", target = "reportPeriodEnd")
    @Mapping(source = "parameters", target = "parameters")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "filePath", target = "filePath")
    @Mapping(source = "errorMessage", target = "errorMessage")
    ReportResponse toResponse(ReportLog reportLog);
    // Добавляем метод для списка
    default List<ReportResponse> toResponseList(List<ReportLog> reports) {
        return reports.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
