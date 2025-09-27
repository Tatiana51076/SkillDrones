🚁 SkillDrones - Система анализа полетов БПЛА
📋 О проекте

Система для анализа полетов беспилотных летательных аппаратов (БПЛА) в регионах России. Сервис автоматически обрабатывает данные из формализованных сообщений, производит геопривязку к субъектам РФ и предоставляет аналитику через REST API.

Технологический стек:

    Backend: Java 21, Spring Boot 3.x

    База данных: PostgreSQL с PostGIS

    Геоданные: JTS, GeoTools, Hibernate Spatial

    Графики: JFreeChart

    Документация: Swagger/OpenAPI 3

    Маппинг: MapStruct

    Безопасность: Spring Security

🎯 Архитектура API
🎮 Контроллеры системы

    RegionAnalysisController - Анализ и геопривязка данных (/api/analysis)

    FlightProcessingController - Обработка и сохранение полетов (/api/processing)

    ReportController - Генерация отчетов и графиков (/api/reports)

🚀 Быстрый старт
Предварительные требования

    Java 21 или выше

    PostgreSQL 15+

    Maven 3.9+

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
port: 8081
servlet:
context-path: /api

springdoc:
api-docs:
path: /v3/api-docs
swagger-ui:
path: /swagger-ui.html
enabled: true

logging:
level:
com.drones.skilldrones: DEBUG

    Запуск приложения

bash

mvn clean install
mvn spring-boot:run

Приложение будет доступно по адресу: http://localhost:8081/api
📊 API Endpoints
🔍 1. RegionAnalysisController - Анализ данных

Базовый URL: /api/analysis
Анализ топ регионов из файла
http

POST /api/analysis/top-regions
Content-Type: multipart/form-data

file: <excel-file>

Пример запроса:
bash

curl -X POST -F "file=@flights_data.xlsx" \
http://localhost:8081/api/api/analysis/top-regions

Анализ за период
http

POST /api/analysis/top-regions/period?startDate=2024-01-01&endDate=2024-01-31
Content-Type: multipart/form-data

file: <excel-file>

Упрощенный анализ
http

POST /api/analysis/top-regions/simple
Content-Type: multipart/form-data

file: <excel-file>

Получение справочника регионов
http

GET /api/analysis/regions
GET /api/analysis/regions/{regionId}

⚙️ 2. FlightProcessingController - Обработка данных

Базовый URL: /api/processing
Пакетная обработка файла
http

POST /api/processing/process-file
Content-Type: multipart/form-data

file: <excel-file>

Пример:
bash

curl -X POST -F "file=@flights_data.xlsx" \
http://localhost:8081/api/api/processing/process-file

Ответ:
json

{
"message": "Файл успешно обработан",
"totalRecords": 150,
"processedSuccessfully": 145,
"failed": 5,
"successRate": "96.67%"
}

Статистика обработки
http

GET /api/processing/stats

Ответ:
json

{
"totalProcessed": 1000,
"successful": 950,
"failed": 50,
"successRate": 95.0
}

📈 3. ReportController - Отчеты и аналитика

Базовый URL: /api/reports
Региональный отчет (JSON)
http

GET /api/reports/regional?startDate=2024-01-01&endDate=2024-01-31

Комплексный отчет
http

GET /api/reports/comprehensive?startDate=2024-01-01&endDate=2024-01-31

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

Управление отчетами
http

GET /api/reports/history              # История отчетов
GET /api/reports/{reportId}          # Отчет по ID
GET /api/reports/type/{reportType}   # Отчеты по типу
GET /api/reports/statistics          # Статистика отчетов

📁 Формат данных
Структура Excel файла

Файл должен содержать столбцы:

    Центр ЕС ОрВД - центр управления полетами

    SHR - сырой текст SHR-телеграммы

    DEP - сырой текст DEP-телеграммы

    ARR - сырой текст ARR-телеграммы

Пример формата телеграммы:
text

SHR-RA2411G
-ZZZZ1100
-M0000/M0029 /ZONA 5943N02942E...
-DEP/5935N02935E DEST/5938N02946E DOF/250105...

🗄️ Модели данных
Основные сущности

    RawTelegram - Сырые телеграммы из Excel

    Flight - Обработанные данные полетов

    Region - Регионы РФ с геоданными

    ReportLog - История генерации отчетов

    User - Пользователи системы

🔧 Тестирование API
1. Swagger документация

После запуска приложения откройте:
text

http://localhost:8081/api/swagger-ui.html

2. Примеры тестовых запросов

Тест полного цикла:
bash

# 1. Обработка файла
curl -X POST -F "file=@test_data.xlsx" http://localhost:8081/api/api/processing/process-file

# 2. Анализ данных
curl -X POST -F "file=@test_data.xlsx" http://localhost:8081/api/api/analysis/top-regions

# 3. Генерация отчета
curl "http://localhost:8081/api/api/reports/comprehensive?startDate=2024-01-01&endDate=2024-01-31"

# 4. Получение графика
curl "http://localhost:8081/api/api/reports/chart?startDate=2024-01-01&endDate=2024-01-31" --output chart.png

🐳 Docker развертывание
yaml

version: '3.8'
services:
postgres:
image: postgis/postgis:15-3.3
environment:
POSTGRES_DB: dronedb
POSTGRES_USER: postgres
POSTGRES_PASSWORD: password
ports:
- "5432:5432"

app:
build: .
ports:
- "8081:8081"
environment:
SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/dronedb
SPRING_DATASOURCE_USERNAME: postgres
SPRING_DATASOURCE_PASSWORD: password
depends_on:
- postgres

🚨 Troubleshooting
Частые проблемы

Доступ к Swagger:
text

Ошибка 403 Forbidden на /swagger-ui.html

Решение: Настройка Spring Security для разрешения доступа к эндпоинтам Swagger

Ошибка базы данных:
text

No property 'processingStatus' found for type 'Flight'

Решение: Убедитесь, что поле processingStatus существует в сущности Flight

Формат координат:
text

Ошибка создания точки из координат

Решение: Проверьте формат координат в Excel файле (пример: "5957N02905E")
📞 Интеграция
Базовые URL:

    Приложение: http://localhost:8081/api

    Swagger: http://localhost:8081/api/swagger-ui.html

    API Docs: http://localhost:8081/api/v3/api-docs