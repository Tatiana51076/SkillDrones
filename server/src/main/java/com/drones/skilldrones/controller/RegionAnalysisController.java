package com.drones.skilldrones.controller;

import com.drones.skilldrones.dto.ParsedFlightData;
import com.drones.skilldrones.service.FileParserService;
import com.drones.skilldrones.service.RegionAnalysisService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
public class RegionAnalysisController {
    private final FileParserService fileParserService;
    private final RegionAnalysisService regionAnalysisService;

    // Контроллер зависит от интерфейсов, а не от реализаций
    public RegionAnalysisController(FileParserService fileParserService,
                                    RegionAnalysisService regionAnalysisService) {
        this.fileParserService = fileParserService;
        this.regionAnalysisService = regionAnalysisService;
    }

    @PostMapping("/top-regions")
    public ResponseEntity<Map<String, Object>> analyzeTopRegionsFromFile(
            @RequestParam("file") MultipartFile file) {

        try {
            List<ParsedFlightData> flightData = fileParserService.parseFlightData(file);
            Map<String, Object> result = regionAnalysisService.analyzeTopRegions(flightData);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Ошибка анализа: " + e.getMessage()));
        }
    }

    @PostMapping("/top-regions/period")
    public ResponseEntity<Map<String, Object>> analyzeTopRegionsForPeriod(
            @RequestParam("file") MultipartFile file,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        try {
            List<ParsedFlightData> flightData = fileParserService.parseFlightData(file);
            Map<String, Object> result = regionAnalysisService.analyzeTopRegionsByPeriod(
                    flightData, startDate, endDate);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Ошибка анализа: " + e.getMessage()));
        }
    }

    @PostMapping("/top-regions/simple")
    public ResponseEntity<List<String>> getSimpleTopRegions(
            @RequestParam("file") MultipartFile file) {

        try {
            List<ParsedFlightData> flightData = fileParserService.parseFlightData(file);
            List<String> result = regionAnalysisService.getSimpleTopRegions(flightData);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(List.of("Ошибка: " + e.getMessage()));
        }
    }
}
