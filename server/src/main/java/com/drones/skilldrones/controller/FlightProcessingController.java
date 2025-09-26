package com.drones.skilldrones.controller;
import com.drones.skilldrones.model.RawTelegram;
import com.drones.skilldrones.service.FileParserService;
import com.drones.skilldrones.service.FlightProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/processing")
public class FlightProcessingController {
    private final FileParserService fileParserService;
    private final FlightProcessingService flightProcessingService;

    public FlightProcessingController(FileParserService fileParserService,
                                      FlightProcessingService flightProcessingService) {
        this.fileParserService = fileParserService;
        this.flightProcessingService = flightProcessingService;
    }

    @PostMapping("/process-file")
    public ResponseEntity<Map<String, Object>> processFlightFile(
            @RequestParam("file") MultipartFile file) {

        try {
            // 1. Парсим Excel файл
            List<RawTelegram> telegrams = fileParserService.parseExcelFile(file);

            // 2. Обрабатываем телеграммы в полеты
            int processedCount = flightProcessingService.processBatch(telegrams);

            // 3. Возвращаем результат
            return ResponseEntity.ok(Map.of(
                    "message", "Файл успешно обработан",
                    "totalRecords", telegrams.size(),
                    "processedSuccessfully", processedCount,
                    "failed", telegrams.size() - processedCount
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Ошибка обработки файла: " + e.getMessage()));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getProcessingStats() {
        try {
            var stats = flightProcessingService.getProcessingStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Ошибка получения статистики: " + e.getMessage()));
        }
    }
}
