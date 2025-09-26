package com.drones.skilldrones.service;

import java.io.File;
import java.time.LocalDate;
import java.util.Map;

public interface ReportService {
    /**
     * Генерирует график статистики полетов
     */
    File generateFlightsChart(LocalDate startDate, LocalDate endDate, String chartType);

    /**
     * Создает JSON отчет по региональной статистике
     */
    String generateRegionalReport(LocalDate startDate, LocalDate endDate);

    /**
     * Генерирует комплексный отчет в нескольких форматах
     */
    Map<String, Object> generateComprehensiveReport(LocalDate startDate, LocalDate endDate);

    /**
     * Генерирует отчет по топ-10 регионам
     */
    Map<String, Object> generateTopRegionsReport(LocalDate startDate, LocalDate endDate);

    /**
     * Генерирует временные ряды по полетам
     */
    Map<String, Object> generateTimeSeriesReport(LocalDate startDate, LocalDate endDate);
}
