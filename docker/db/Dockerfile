# Используем официальный образ PostGIS
FROM postgis/postgis:15-3.3

# Устанавливаем переменные окружения для инициализации БД
ENV POSTGRES_DB=drone_db
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=dron2025

# Копируем SQL-скрипты в директорию, откуда они выполнятся при первом запуске контейнера
COPY  init-scripts/ /docker-entrypoint-initdb.d/

# Даем права на выполнение скриптов (может потребоваться)
RUN chmod +x /docker-entrypoint-initdb.d/*.sql

# Открываем стандартный порт PostgreSQL
EXPOSE 5432
