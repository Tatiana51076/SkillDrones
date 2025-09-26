package com.drones.skilldrones.service;

import com.drones.skilldrones.dto.response.metrics.FlightStatsResponse;
import com.drones.skilldrones.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class MetricsService {
    private final FlightRepository flightRepository;

    public MetricsService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public FlightStatsResponse getFlightStats(LocalDate startDate, LocalDate endDate) {
        Long totalFlights = flightRepository.countFlightsInPeriod(startDate, endDate);

        // Расчет дополнительных метрик
        var flightsByType = flightRepository.countFlightsByDroneType()
                .stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> (Long) arr[1]
                ));

        return new FlightStatsResponse(
                startDate,
                endDate,
                totalFlights.intValue(),
                0.0, // averageDuration - нужно рассчитать
                null, // flightsByRegion - нужно рассчитать
                flightsByType,
                null, // dailyFlights - нужно рассчитать
                null  // peakLoad - нужно рассчитать
        );
    }
}
