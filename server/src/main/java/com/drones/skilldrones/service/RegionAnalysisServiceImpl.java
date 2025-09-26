package com.drones.skilldrones.service;
import com.drones.skilldrones.dto.ParsedFlightData;
import com.drones.skilldrones.model.Region;
import com.drones.skilldrones.repository.RegionRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RegionAnalysisServiceImpl implements RegionAnalysisService {

    private final RegionRepository regionRepository;
    private final GeometryFactory geometryFactory;

    public RegionAnalysisServiceImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
        this.geometryFactory = new GeometryFactory();
    }

    /**
     * Определяет регион для полета по координатам
     */
    @Override
    public Optional<Region> findRegionForCoordinates(String coordinates) {
        if (coordinates == null || coordinates.isEmpty()) {
            return Optional.empty();
        }

        try {
            Point point = createPointFromCoordinates(coordinates);
            return regionRepository.findRegionByPoint(point);
        } catch (Exception e) {
            System.err.println("Ошибка определения региона для координат: " + coordinates);
            return Optional.empty();
        }
    }

    @Override
    public Map<String, Object> analyzeTopRegions(List<ParsedFlightData> flightData) {
        Map<Region, Integer> regionStats = new HashMap<>();
        int totalProcessed = 0;
        int failedGeolocation = 0;

        for (ParsedFlightData flight : flightData) {
            Optional<Region> regionOpt = findRegionForCoordinates(flight.getCoordinates());

            if (regionOpt.isPresent()) {
                Region region = regionOpt.get();
                regionStats.put(region, regionStats.getOrDefault(region, 0) + 1);
                totalProcessed++;
            } else {
                failedGeolocation++;
            }
        }

        List<Map.Entry<Region, Integer>> sortedRegions = regionStats.entrySet()
                .stream()
                .sorted(Map.Entry.<Region, Integer>comparingByValue().reversed())
                .limit(10)
                .toList();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalFlightsProcessed", totalProcessed);
        result.put("failedGeolocation", failedGeolocation);
        result.put("analysisDate", new Date());

        List<Map<String, Object>> topRegions = new ArrayList<>();
        int rank = 1;

        for (Map.Entry<Region, Integer> entry : sortedRegions) {
            Map<String, Object> regionInfo = new LinkedHashMap<>();
            regionInfo.put("rank", rank++);
            regionInfo.put("regionName", entry.getKey().getName());
            regionInfo.put("flightCount", entry.getValue());
            regionInfo.put("regionId", entry.getKey().getRegionId());

            if (entry.getKey().getAreaKm2() != null) {
                double density = (double) entry.getValue() / entry.getKey().getAreaKm2() * 1000;
                regionInfo.put("flightDensity", Math.round(density * 100) / 100.0);
            }

            topRegions.add(regionInfo);
        }

        result.put("topRegions", topRegions);
        return result;
    }

    /**
     * Анализирует топ регионов за определенный период
     */
    @Override
    public Map<String, Object> analyzeTopRegionsByPeriod(List<ParsedFlightData> flightData,
                                                         LocalDate startDate,
                                                         LocalDate endDate) {
        List<ParsedFlightData> filteredFlights = flightData.stream()
                .filter(flight -> flight.getFlightDate() != null)
                .filter(flight -> !flight.getFlightDate().isBefore(startDate))
                .filter(flight -> !flight.getFlightDate().isAfter(endDate))
                .collect(Collectors.toList());

        Map<String, Object> result = analyzeTopRegions(filteredFlights);
        result.put("periodStart", startDate);
        result.put("periodEnd", endDate);
        result.put("periodFlightsCount", filteredFlights.size());

        return result;
    }

    @Override
    public List<String> getSimpleTopRegions(List<ParsedFlightData> flightData) {
        Map<String, Object> analysisResult = analyzeTopRegions(flightData);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> topRegions =
                (List<Map<String, Object>>) analysisResult.get("topRegions");

        return topRegions.stream()
                .map(region -> String.format("%d. %s (%d полетов)",
                        region.get("rank"), region.get("regionName"), region.get("flightCount")))
                .collect(Collectors.toList());
    }

    /**
     * Создает Point из строки координат "lat,lon"
     */
    private Point createPointFromCoordinates(String coordinates) {
        String[] parts = coordinates.split(",");
        double lat = Double.parseDouble(parts[0].trim());
        double lon = Double.parseDouble(parts[1].trim());

        return geometryFactory.createPoint(new Coordinate(lon, lat));
    }
}
