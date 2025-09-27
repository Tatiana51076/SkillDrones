package com.drones.skilldrones.mapper;

import com.drones.skilldrones.dto.ParsedFlightData;
import com.drones.skilldrones.model.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface FlightProcessingMapper {
    @Mapping(source = "flightId", target = "flightCode")
    @Mapping(source = "coordinates", target = "departureCoords")
    @Mapping(source = "coordinates", target = "arrivalCoords")
    @Mapping(source = "coordinates", target = "departurePoint", qualifiedByName = "coordinatesToPoint")
    @Mapping(source = "coordinates", target = "arrivalPoint", qualifiedByName = "coordinatesToPoint")
    @Mapping(source = "rawTelegram", target = "rawTelegram")
    @Mapping(target = "flightId", ignore = true)
    @Mapping(target = "departureRegion", ignore = true)
    @Mapping(target = "arrivalRegion", ignore = true)
    @Mapping(target = "durationMinutes", ignore = true)
    Flight toFlight(ParsedFlightData parsedData);

    @Named("coordinatesToPoint")
    default org.locationtech.jts.geom.Point coordinatesToPoint(String coordinates) {
        if (coordinates == null || coordinates.isEmpty()) {
            return null;
        }
        try {
            String[] parts = coordinates.split(",");
            double lat = Double.parseDouble(parts[0].trim());
            double lon = Double.parseDouble(parts[1].trim());

            org.locationtech.jts.geom.GeometryFactory geometryFactory =
                    new org.locationtech.jts.geom.GeometryFactory();
            return geometryFactory.createPoint(
                    new org.locationtech.jts.geom.Coordinate(lon, lat)
            );
        } catch (Exception e) {
            return null;
        }
    }

    default LocalTime extractTime(String timeText) {
        if (timeText == null) return null;
        try {
            // Парсинг времени из текста телеграммы
            // Пример: "0705" -> 07:05
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
            return LocalTime.parse(timeText, formatter);
        } catch (Exception e) {
            return null;
        }
    }
}
