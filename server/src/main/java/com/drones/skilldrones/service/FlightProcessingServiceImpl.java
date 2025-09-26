package com.drones.skilldrones.service;

import com.drones.skilldrones.dto.ParsedFlightData;
import com.drones.skilldrones.dto.ProcessingStats;
import com.drones.skilldrones.model.Flight;
import com.drones.skilldrones.model.RawTelegram;
import com.drones.skilldrones.model.Region;
import com.drones.skilldrones.repository.FlightRepository;
import com.drones.skilldrones.repository.RegionRepository;
import com.drones.skilldrones.service.FileParserService;
import com.drones.skilldrones.service.FlightProcessingService;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class FlightProcessingServiceImpl implements FlightProcessingService {

    private final FileParserService fileParserService;
    private final RegionRepository regionRepository;
    private final FlightRepository flightRepository;
    private final GeometryFactory geometryFactory;

    // Используем FileParserService вместо TelegramParserService
    public FlightProcessingServiceImpl(FileParserService fileParserService,
                                       RegionRepository regionRepository,
                                       FlightRepository flightRepository) {
        this.fileParserService = fileParserService;
        this.regionRepository = regionRepository;
        this.flightRepository = flightRepository;
        this.geometryFactory = new GeometryFactory();
    }

    @Override
    @Transactional
    public void processRawTelegrams(List<RawTelegram> rawTelegrams) {
        for (RawTelegram telegram : rawTelegrams) {
            try {
                Flight flight = convertToFlight(telegram);
                flightRepository.save(flight);
                telegram.setProcessingStatus("PROCESSED");
            } catch (Exception e) {
                telegram.setProcessingStatus("FAILED");
            }
        }
    }

    @Override
    @Transactional
    public int processBatch(List<RawTelegram> telegrams) {
        int successful = 0;

        for (RawTelegram telegram : telegrams) {
            try {
                Flight flight = convertToFlight(telegram);
                flightRepository.save(flight);
                telegram.setProcessingStatus("PROCESSED");
                successful++;
            } catch (Exception e) {
                telegram.setProcessingStatus("FAILED");
                // Логируем ошибку, но продолжаем обработку
                System.err.println("Ошибка обработки телеграммы " + telegram.getId() + ": " + e.getMessage());
            }
        }

        return successful;
    }

    @Override
    public Flight convertToFlight(RawTelegram telegram) {
        Flight flight = new Flight();

        // Используем FileParserService для извлечения данных
        ParsedFlightData parsedData = fileParserService.extractFlightDataFromTelegram(telegram);

        // Заполняем основные поля
        flight.setFlightCode(parsedData.getFlightId());
        flight.setDroneType(parsedData.getDroneType());
        flight.setRawTelegram(telegram);
        flight.setFlightDate(parsedData.getFlightDate());

        // Устанавливаем время (можно добавить парсинг времени)
        flight.setDepartureTime(LocalTime.now()); // временно, нужно парсить из телеграммы
        flight.setArrivalTime(LocalTime.now().plusMinutes(30)); // временно

        // Геопривязка
        String coords = parsedData.getCoordinates();
        if (coords != null) {
            Point point = createPointFromCoords(coords);
            flight.setDeparturePoint(point);
            flight.setDepartureCoords(coords);

            // Находим регион по координатам
            Optional<Region> region = regionRepository.findRegionByPoint(point);
            region.ifPresent(flight::setDepartureRegion);

            // Для простоты считаем, что вылет и прилет в одном месте
            flight.setArrivalPoint(point);
            flight.setArrivalCoords(coords);
            region.ifPresent(flight::setArrivalRegion);
        }

        // Рассчитываем продолжительность (временная логика)
        if (flight.getDepartureTime() != null && flight.getArrivalTime() != null) {
            long durationMinutes = java.time.Duration.between(
                    flight.getDepartureTime(), flight.getArrivalTime()
            ).toMinutes();
            flight.setDurationMinutes((int) durationMinutes);
        }

        return flight;
    }

    @Override
    public ProcessingStats getProcessingStats() {
        // Здесь можно добавить логику для сбора статистики
        // Например, посчитать количество обработанных/необработанных телеграмм
        return new ProcessingStats(0, 0, 0); // заглушка
    }

    private Point createPointFromCoords(String coords) {
        try {
            String[] parts = coords.split(",");
            double lat = Double.parseDouble(parts[0].trim());
            double lon = Double.parseDouble(parts[1].trim());

            return geometryFactory.createPoint(new Coordinate(lon, lat));
        } catch (Exception e) {
            throw new RuntimeException("Ошибка создания точки из координат: " + coords, e);
        }
    }
}
