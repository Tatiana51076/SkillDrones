🚁 SkillDrones - Система анализа полетов БПЛА

📋 О проекте

Система для анализа полетов беспилотных летательных аппаратов (БПЛА) в регионах России. Сервис автоматически обрабатывает данные из формализованных сообщений, производит геопривязку к субъектам РФ и предоставляет аналитику через REST API.

Технологический стек:

    Backend: Java 17, Spring Boot 3.x

    База данных: PostgreSQL с PostGIS

    Геоданные: JTS, GeoTools

    Графики: JFreeChart

    Документация: Swagger/OpenAPI 3

🎯 Архитектура API
🎮 Контроллеры системы

    RegionAnalysisController - Анализ и геопривязка данных

    FlightProcessingController - Обработка и сохранение полетов

    ReportController - Генерация отчетов и графиков

🚀 Быстрый старт
Предварительные требования

    Java 21 или выше

    PostgreSQL 12+

    Maven 3.6+

    Docker (опционально)

Установка и запуск

    Клонирование репозитория

bash

git clone <your-repository-url>
cd skilldrones/server

    Настройка базы данных

sql

-- Создание базы данных
CREATE DATABASE dronedb;
CREATE EXTENSION postgis;

    Настройка конфигурации
    Создайте application.yml в src/main/resources:

yaml

spring:
datasource:
url: jdbc:postgresql://localhost:5432/dronedb
username: your-username
password: your-password
jpa:
hibernate:
ddl-auto: update
show-sql: true

server:
port: 8080

logging:
level:
com.drones.skilldrones: DEBUG

    Запуск приложения

bash

mvn clean install
mvn spring-boot:run

Приложение будет доступно по адресу: http://localhost:8081
📊 API Endpoints
🔍 1. RegionAnalysisController - Анализ данных

Базовый URL: /api/analysis
Загрузка и анализ Excel файла
http

POST /api/analysis/top-regions
Content-Type: multipart/form-data

file: <excel-file>

Пример запроса (curl):
bash

curl -X POST -F "file=@flights_data.xlsx" \
http://localhost:8081/api/analysis/top-regions

Анализ за период:
http

POST /api/analysis/top-regions/period?startDate=2024-01-01&endDate=2024-01-31
Content-Type: multipart/form-data

file: <excel-file>

Простой топ регионов:
http

POST /api/analysis/top-regions/simple
Content-Type: multipart/form-data

file: <excel-file>

Ответ:
json

[
"1. Московская область (42 полетов)",
"2. Санкт-Петербург (38 полетов)"
]

⚙️ 2. FlightProcessingController - Обработка данных

Базовый URL: /api/processing
Пакетная обработка файла
http

POST /api/processing/process-file
Content-Type: multipart/form-data

file: <excel-file>

Пример запроса:
bash

curl -X POST -F "file=@flights_data.xlsx" \
http://localhost:8081/api/processing/process-file

Ответ:
json

{
"message": "Файл успешно обработан",
"totalRecords": 150,
"processedSuccessfully": 145,
"failed": 5
}

Статистика обработки
http

GET /api/processing/stats

Ответ:
json

{
"totalProcessed": 1000,
"successful": 950,
"failed": 50
}

📈 3. ReportController - Отчеты и аналитика

Базовый URL: /api/reports
Региональный отчет (JSON)
http

GET /api/reports/regional?startDate=2024-01-01&endDate=2024-01-31

Пример:
bash

curl "http://localhost:8081/api/reports/regional?startDate=2024-01-01&endDate=2024-01-31"

Комплексный отчет
http

GET /api/reports/comprehensive?startDate=2024-01-01&endDate=2024-01-31

Ответ включает:
json

{
"reportType": "COMPREHENSIVE",
"totalFlights": 145,
"droneTypeDistribution": {
"BLA": 80,
"SHAR": 45,
"AER": 20
},
"regionalDistribution": {
"Московская область": 42,
"Санкт-Петербург": 38
},
"chartFilePath": "/tmp/chart123.png"
}

Топ регионов отчет
http

GET /api/reports/top-regions?startDate=2024-01-01&endDate=2024-01-31

Графики (PNG)
http

GET /api/reports/chart?startDate=2024-01-01&endDate=2024-01-31&chartType=bar

Параметры chartType:

    bar - столбчатая диаграмма

    line - линейный график

    pie - круговая диаграмма

Пример загрузки графика:
bash

curl "http://localhost:8081/api/reports/chart?startDate=2024-01-01&endDate=2024-01-31&chartType=bar" --output chart.png

🔄 Workflow использования API
Сценарий 1: Полный цикл обработки

    Загрузка данных → POST /api/processing/process-file

    Анализ результатов → POST /api/analysis/top-regions

    Генерация отчетов → GET /api/reports/comprehensive

    Получение графиков → GET /api/reports/chart

Сценарий 2: Быстрый анализ

    Прямой анализ → POST /api/analysis/top-regions

    Визуализация → GET /api/reports/chart

🧪 Тестовые сценарии для фронтенда
Тест 1: Загрузка и обработка файла
javascript

// 1. Загрузка файла
const formData = new FormData();
formData.append('file', excelFile);

// 2. Обработка данных
const processingResponse = await fetch('/api/processing/process-file', {
method: 'POST',
body: formData
});

// 3. Получение статистики
const statsResponse = await fetch('/api/processing/stats');

Тест 2: Анализ и визуализация
javascript

// 1. Анализ топ регионов
const analysisResponse = await fetch('/api/analysis/top-regions', {
method: 'POST',
body: formData
});

// 2. Генерация графика
const chartResponse = await fetch('/api/reports/chart?chartType=bar');
const chartBlob = await chartResponse.blob();

Тест 3: Комплексная отчетность
javascript

// 1. Получение комплексного отчета
const reportResponse = await fetch('/api/reports/comprehensive?startDate=2024-01-01&endDate=2024-01-31');
const reportData = await reportResponse.json();

// 2. Отображение метрик
console.log('Топ регионов:', reportData.regionalDistribution);
console.log('Распределение по типам:', reportData.droneTypeDistribution);

📊 Форматы ответов
RegionAnalysisController
json

{
"totalFlightsProcessed": 145,
"failedGeolocation": 5,
"topRegions": [
{
"rank": 1,
"regionName": "Московская область",
"flightCount": 42,
"flightDensity": 0.95
}
]
}

FlightProcessingController
json

{
"message": "Файл успешно обработан",
"totalRecords": 150,
"processedSuccessfully": 145,
"failed": 5
}

ReportController
json

{
"totalFlights": 145,
"regionalDistribution": {
"Московская область": 42,
"Санкт-Петербург": 38
},
"dailyFlights": {
"2024-01-01": 15,
"2024-01-02": 20
}
}

🚨 Ошибки и решение проблем
Частые ошибки

    Ошибка подключения к БД
    text

Connection to localhost:5432 refused

Решение: Убедитесь, что PostgreSQL запущен и доступен

Ошибка загрузки файла
text

Required request part 'file' is not present

Решение: Проверьте, что файл передается с правильным именем параметра

Ошибка формата координат
text

Ошибка создания точки из координат

    Решение: Проверьте формат координат в Excel файле

Логирование

Уровни логирования можно настроить в application.yml:
yaml

logging:
level:
com.drones.skilldrones: DEBUG
org.hibernate.SQL: DEBUG