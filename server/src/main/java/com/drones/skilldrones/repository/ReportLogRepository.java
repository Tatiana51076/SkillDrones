package com.drones.skilldrones.repository;

import com.drones.skilldrones.model.ReportLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportLogRepository extends JpaRepository<ReportLog, Long> {
    /**
     * Находит все отчеты, отсортированные по дате создания (новые сначала)
     */
    List<ReportLog> findAllByOrderByCreatedAtDesc();

    /**
     * Находит отчеты по типу
     */
    List<ReportLog> findByReportTypeOrderByCreatedAtDesc(String reportType);

    /**
     * Находит отчеты по статусу
     */
    List<ReportLog> findByStatusOrderByCreatedAtDesc(ReportLog.ReportStatus status);

    /**
     * Находит отчеты за определенный период
     */
    List<ReportLog> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime start, LocalDateTime end);

    /**
     * Находит отчеты по периоду данных
     */
    @Query("SELECT r FROM ReportLog r WHERE r.reportPeriodStart >= :startDate AND r.reportPeriodEnd <= :endDate ORDER BY r.createdAt DESC")
    List<ReportLog> findByReportPeriod(@Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);

    /**
     * Находит завершенные отчеты
     */
    @Query("SELECT r FROM ReportLog r WHERE r.status = 'COMPLETED' ORDER BY r.createdAt DESC")
    List<ReportLog> findCompletedReports();

    /**
     * Находит отчеты с ошибками
     */
    @Query("SELECT r FROM ReportLog r WHERE r.status = 'FAILED' ORDER BY r.createdAt DESC")
    List<ReportLog> findFailedReports();

    /**
     * Подсчитывает количество отчетов по статусу
     */
    long countByStatus(ReportLog.ReportStatus status);

    /**
     * Находит отчеты по пользователю
     */
    List<ReportLog> findByUserUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * Удаляет старые отчеты (для очистки)
     */
    void deleteByCreatedAtBefore(LocalDateTime date);

}
