package com.oxaira.airq.iotmonitoring.application.dto;

public class HourlyMetricDTO {
    private String hour; // e.g. "00:00", "01:00", etc.
    private Double averageCo2;
    private Double averagePm25;
    private Double averageTemperature;
    private Double averageHumidity;

    public HourlyMetricDTO() {}

    public HourlyMetricDTO(String hour, Double averageCo2, Double averagePm25, Double averageTemperature, Double averageHumidity) {
        this.hour = hour;
        this.averageCo2 = averageCo2;
        this.averagePm25 = averagePm25;
        this.averageTemperature = averageTemperature;
        this.averageHumidity = averageHumidity;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Double getAverageCo2() {
        return averageCo2;
    }

    public void setAverageCo2(Double averageCo2) {
        this.averageCo2 = averageCo2;
    }

    public Double getAveragePm25() {
        return averagePm25;
    }

    public void setAveragePm25(Double averagePm25) {
        this.averagePm25 = averagePm25;
    }

    public Double getAverageTemperature() {
        return averageTemperature;
    }

    public void setAverageTemperature(Double averageTemperature) {
        this.averageTemperature = averageTemperature;
    }

    public Double getAverageHumidity() {
        return averageHumidity;
    }

    public void setAverageHumidity(Double averageHumidity) {
        this.averageHumidity = averageHumidity;
    }
}
