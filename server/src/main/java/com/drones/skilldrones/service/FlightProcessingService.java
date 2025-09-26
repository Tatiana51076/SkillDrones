package com.drones.skilldrones.service;
import com.drones.skilldrones.dto.ProcessingStats;
import com.drones.skilldrones.model.Flight;
import com.drones.skilldrones.model.RawTelegram;

import java.util.List;

public interface FlightProcessingService {
    /**
     * Обрабатывает сырые телеграммы и создает полеты
     */
    void processRawTelegrams(List<RawTelegram> rawTelegrams);

    /**
     * Конвертирует телеграмму в полет
     */
    Flight convertToFlight(RawTelegram telegram);

    /**
     * Пакетная обработка телеграмм
     */
    int processBatch(List<RawTelegram> telegrams);

    /**
     * Получает статистику обработки
     */
    ProcessingStats getProcessingStats();
}
