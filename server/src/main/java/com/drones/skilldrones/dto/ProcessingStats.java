package com.drones.skilldrones.dto;

public class ProcessingStats {
    private int totalProcessed;
    private int successful;
    private int failed;
    private double successRate;

    public ProcessingStats(int totalProcessed, int successful, int failed) {
        this.totalProcessed = totalProcessed;
        this.successful = successful;
        this.failed = failed;
        this.successRate = totalProcessed > 0 ? (double) successful / totalProcessed * 100 : 0;
    }

    // геттеры
    public int getTotalProcessed() { return totalProcessed; }
    public int getSuccessful() { return successful; }
    public int getFailed() { return failed; }
    public double getSuccessRate() { return successRate; }

    // сеттеры (если нужны)
    public void setTotalProcessed(int totalProcessed) { this.totalProcessed = totalProcessed; }
    public void setSuccessful(int successful) { this.successful = successful; }
    public void setFailed(int failed) { this.failed = failed; }
    public void setSuccessRate(double successRate) { this.successRate = successRate; }
}
