package com.drones.skilldrones.dto;

public class ProcessingStats {
    private int totalProcessed;
    private int successful;
    private int failed;

    // конструкторы, геттеры, сеттеры
    public ProcessingStats(int totalProcessed, int successful, int failed) {
        this.totalProcessed = totalProcessed;
        this.successful = successful;
        this.failed = failed;
    }

    public int getTotalProcessed() {
        return totalProcessed;
    }

    public void setTotalProcessed(int totalProcessed) {
        this.totalProcessed = totalProcessed;
    }

    public int getSuccessful() {
        return successful;
    }

    public void setSuccessful(int successful) {
        this.successful = successful;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }
}
