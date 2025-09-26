package com.drones.skilldrones.service;

import com.drones.skilldrones.model.Flight;
import com.drones.skilldrones.model.Region;
import com.drones.skilldrones.repository.FlightRepository;
import com.drones.skilldrones.repository.RegionRepository;
import com.drones.skilldrones.service.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;


@Service
public class ReportServiceImpl implements ReportService {
    private final FlightRepository flightRepository;
    private final RegionRepository regionRepository;
    private final ObjectMapper objectMapper;

    public ReportServiceImpl(FlightRepository flightRepository,
                             RegionRepository regionRepository) {
        this.flightRepository = flightRepository;
        this.regionRepository = regionRepository;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public File generateFlightsChart(LocalDate startDate, LocalDate endDate, String chartType) {
        try {
            List<Flight> flights = flightRepository.findByFlightDateBetween(startDate, endDate);

            DefaultCategoryDataset dataset = createDataset(flights);
            JFreeChart chart = createChart(dataset, chartType, flights);

            File chartFile = File.createTempFile("chart", ".png");
            BufferedImage image = chart.createBufferedImage(800, 600);
            ImageIO.write(image, "png", chartFile);

            return chartFile;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка генерации графика", e);
        }
    }

    @Override
    public String generateRegionalReport(LocalDate startDate, LocalDate endDate) {
        try {
            List<Flight> flights = flightRepository.findByFlightDateBetween(startDate, endDate);

            // Группируем полеты по регионам вылета
            Map<String, Long> regionalStats = flights.stream()
                    .filter(flight -> flight.getDepartureRegion() != null)
                    .collect(Collectors.groupingBy(
                            flight -> flight.getDepartureRegion().getName(),
                            Collectors.counting()
                    ));

            // Сортируем по убыванию количества полетов
            Map<String, Object> reportData = new LinkedHashMap<>();
            reportData.put("periodStart", startDate.toString());
            reportData.put("periodEnd", endDate.toString());
            reportData.put("totalFlights", flights.size());
            reportData.put("regionalDistribution", regionalStats);

            // Топ-10 регионов
            List<Map<String, Object>> topRegions = regionalStats.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(10)
                    .map(entry -> {
                        Map<String, Object> regionInfo = new HashMap<>();
                        regionInfo.put("region", entry.getKey());
                        regionInfo.put("flightCount", entry.getValue());
                        return regionInfo;
                    })
                    .collect(Collectors.toList());

            reportData.put("top10Regions", topRegions);

            // Конвертируем в JSON
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(reportData);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка генерации регионального отчета", e);
        }
    }

    @Override
    public Map<String, Object> generateComprehensiveReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> comprehensiveReport = new LinkedHashMap<>();

        try {
            List<Flight> flights = flightRepository.findByFlightDateBetween(startDate, endDate);

            // Основная статистика
            comprehensiveReport.put("reportType", "COMPREHENSIVE");
            comprehensiveReport.put("periodStart", startDate.toString());
            comprehensiveReport.put("periodEnd", endDate.toString());
            comprehensiveReport.put("totalFlights", flights.size());

            // Статистика по типам дронов
            Map<String, Long> droneTypeStats = flights.stream()
                    .filter(flight -> flight.getDroneType() != null)
                    .collect(Collectors.groupingBy(
                            Flight::getDroneType,
                            Collectors.counting()
                    ));
            comprehensiveReport.put("droneTypeDistribution", droneTypeStats);

            // Региональная статистика
            Map<String, Long> regionalStats = flights.stream()
                    .filter(flight -> flight.getDepartureRegion() != null)
                    .collect(Collectors.groupingBy(
                            flight -> flight.getDepartureRegion().getName(),
                            Collectors.counting()
                    ));
            comprehensiveReport.put("regionalDistribution", regionalStats);

            // Ежедневная статистика
            Map<String, Long> dailyStats = flights.stream()
                    .collect(Collectors.groupingBy(
                            flight -> flight.getFlightDate().toString(),
                            Collectors.counting()
                    ));
            comprehensiveReport.put("dailyFlights", dailyStats);

            // Расчет дополнительных метрик
            comprehensiveReport.put("averageFlightsPerDay",
                    calculateAverageFlightsPerDay(flights, startDate, endDate));
            comprehensiveReport.put("peakDay", findPeakDay(flights));
            comprehensiveReport.put("mostActiveRegion", findMostActiveRegion(flights));

            // Генерация графика
            File chartFile = generateFlightsChart(startDate, endDate, "bar");
            comprehensiveReport.put("chartFilePath", chartFile.getAbsolutePath());

        } catch (Exception e) {
            comprehensiveReport.put("error", "Ошибка генерации отчета: " + e.getMessage());
        }

        return comprehensiveReport;
    }

    @Override
    public Map<String, Object> generateTopRegionsReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new LinkedHashMap<>();

        try {
            List<Flight> flights = flightRepository.findByFlightDateBetween(startDate, endDate);

            // Группируем по регионам и считаем плотность полетов
            List<Map<String, Object>> topRegions = flights.stream()
                    .filter(flight -> flight.getDepartureRegion() != null)
                    .collect(Collectors.groupingBy(
                            Flight::getDepartureRegion,
                            Collectors.counting()
                    ))
                    .entrySet().stream()
                    .sorted(Map.Entry.<Region, Long>comparingByValue().reversed())
                    .limit(10)
                    .map(entry -> {
                        Region region = entry.getKey();
                        long flightCount = entry.getValue();
                        double density = region.getAreaKm2() != null ?
                                (double) flightCount / region.getAreaKm2() * 1000 : 0;

                        Map<String, Object> regionInfo = new HashMap<>();
                        regionInfo.put("regionName", region.getName());
                        regionInfo.put("flightCount", flightCount);
                        regionInfo.put("areaKm2", region.getAreaKm2());
                        regionInfo.put("flightDensity", Math.round(density * 100) / 100.0);
                        regionInfo.put("regionId", region.getRegionId());

                        return regionInfo;
                    })
                    .collect(Collectors.toList());

            report.put("topRegions", topRegions);
            report.put("totalRegionsWithFlights",
                    flights.stream().filter(f -> f.getDepartureRegion() != null)
                            .map(f -> f.getDepartureRegion().getRegionId())
                            .distinct().count());
            report.put("period", startDate + " - " + endDate);

        } catch (Exception e) {
            report.put("error", "Ошибка генерации отчета по регионам: " + e.getMessage());
        }

        return report;
    }

    @Override
    public Map<String, Object> generateTimeSeriesReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new LinkedHashMap<>();

        try {
            List<Flight> flights = flightRepository.findByFlightDateBetween(startDate, endDate);

            // Группируем по дням
            Map<LocalDate, Long> dailyCounts = flights.stream()
                    .collect(Collectors.groupingBy(
                            Flight::getFlightDate,
                            Collectors.counting()
                    ));

            // Заполняем пропущенные дни нулями
            List<Map<String, Object>> timeSeries = new ArrayList<>();
            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                long count = dailyCounts.getOrDefault(currentDate, 0L);

                Map<String, Object> dayData = new HashMap<>();
                dayData.put("date", currentDate.toString());
                dayData.put("flightCount", count);
                dayData.put("isPeak", count >= Collections.max(dailyCounts.values()));

                timeSeries.add(dayData);
                currentDate = currentDate.plusDays(1);
            }

            report.put("timeSeries", timeSeries);
            report.put("totalDays", timeSeries.size());
            report.put("daysWithFlights", dailyCounts.size());
            report.put("daysWithoutFlights", timeSeries.size() - dailyCounts.size());
            report.put("averageFlightsPerDay",
                    flights.size() / (double) timeSeries.size());

            // Статистика по дням недели
            Map<String, Long> weeklyPattern = flights.stream()
                    .collect(Collectors.groupingBy(
                            flight -> flight.getFlightDate().getDayOfWeek().toString(),
                            Collectors.counting()
                    ));
            report.put("weeklyPattern", weeklyPattern);

        } catch (Exception e) {
            report.put("error", "Ошибка генерации временного ряда: " + e.getMessage());
        }

        return report;
    }

    // Вспомогательные методы
    private DefaultCategoryDataset createDataset(List<Flight> flights) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        flights.stream()
                .collect(Collectors.groupingBy(
                        Flight::getFlightDate,
                        Collectors.counting()
                ))
                .forEach((date, count) ->
                        dataset.addValue(count, "Полеты", date.toString())
                );

        return dataset;
    }

    private JFreeChart createChart(DefaultCategoryDataset dataset, String chartType, List<Flight> flights) {
        return switch (chartType.toLowerCase()) {
            case "line" -> ChartFactory.createLineChart(
                    "Статистика полетов по дням",
                    "Дата",
                    "Количество полетов",
                    dataset,
                    org.jfree.chart.plot.PlotOrientation.VERTICAL,
                    true,    // show legend
                    true,    // use tooltips
                    false    // configure URLs
            );
            case "pie" -> ChartFactory.createPieChart(
                    "Распределение полетов по регионам",
                    createPieDataset(flights),
                    true,    // show legend
                    true,    // use tooltips
                    false    // configure URLs
            );
            default -> ChartFactory.createBarChart(
                    "Статистика полетов по дням",
                    "Дата",
                    "Количество полетов",
                    dataset,
                    org.jfree.chart.plot.PlotOrientation.VERTICAL,
                    true,    // show legend
                    true,    // use tooltips
                    false    // configure URLs
            );
        };
    }

    private org.jfree.data.general.DefaultPieDataset createPieDataset(List<Flight> flights) {
        org.jfree.data.general.DefaultPieDataset dataset = new org.jfree.data.general.DefaultPieDataset();

        flights.stream()
                .filter(flight -> flight.getDepartureRegion() != null)
                .collect(Collectors.groupingBy(
                        flight -> flight.getDepartureRegion().getName(),
                        Collectors.counting()
                ))
                .forEach(dataset::setValue);

        return dataset;
    }

    private double calculateAverageFlightsPerDay(List<Flight> flights, LocalDate start, LocalDate end) {
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(start, end) + 1;
        return flights.size() / (double) daysBetween;
    }

    private Map<String, Object> findPeakDay(List<Flight> flights) {
        return flights.stream()
                .collect(Collectors.groupingBy(
                        Flight::getFlightDate,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> {
                    Map<String, Object> peakDay = new HashMap<>();
                    peakDay.put("date", entry.getKey().toString());
                    peakDay.put("flightCount", entry.getValue());
                    return peakDay;
                })
                .orElse(Map.of("date", "N/A", "flightCount", 0));
    }

    private Map<String, Object> findMostActiveRegion(List<Flight> flights) {
        return flights.stream()
                .filter(flight -> flight.getDepartureRegion() != null)
                .collect(Collectors.groupingBy(
                        Flight::getDepartureRegion,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> {
                    Map<String, Object> regionInfo = new HashMap<>();
                    regionInfo.put("regionName", entry.getKey().getName());
                    regionInfo.put("flightCount", entry.getValue());
                    return regionInfo;
                })
                .orElse(Map.of("regionName", "N/A", "flightCount", 0));
    }

    // Метод для создания временного ряда (для линейных графиков)
    private TimeSeriesCollection createTimeSeriesDataset(List<Flight> flights) {
        TimeSeries series = new TimeSeries("Полеты");

        flights.stream()
                .collect(Collectors.groupingBy(
                        Flight::getFlightDate,
                        Collectors.counting()
                ))
                .forEach((date, count) -> {
                    series.add(new Day(
                                    Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant())),
                            count
                    );
                });

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }
}
