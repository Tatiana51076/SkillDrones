package com.drones.skilldrones.mapper;

import com.drones.skilldrones.dto.response.RegionResponse;
import com.drones.skilldrones.model.Region;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RegionMapper {
    @Mapping(source = "regionId", target = "regionId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "areaKm2", target = "areaKm2")
    RegionResponse toResponse(Region region);

    // Добавляем метод для списка
    default List<RegionResponse> toResponseList(List<Region> regions) {
        return regions.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
