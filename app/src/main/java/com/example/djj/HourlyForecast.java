package com.example.djj;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HourlyForecast {
    private long time;
    private double temperature;
    private String condition;
    private double precipitationChance;
    private String icon;

    public HourlyForecast() {
    }

    public HourlyForecast(long time, double temperature, String condition,
                          double precipitationChance, String icon) {
        this.time = time;
        this.temperature = temperature;
        this.condition = condition;
        this.precipitationChance = precipitationChance;
        this.icon = icon;
    }

    public long getTime() {
        return time;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getCondition() {
        return condition;
    }

    public double getPrecipitationChance() {
        return precipitationChance;
    }

    public String getIcon() {
        return icon;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setPrecipitationChance(double precipitationChance) {
        this.precipitationChance = precipitationChance;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getFormattedTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(new Date(time * 1000));
    }

    public String getFormattedTemperature() {
        return String.format(Locale.getDefault(), "%.1f°C", temperature);
    }

    public String getFormattedPrecipitationChance() {
        return String.format(Locale.getDefault(), "%.0f%%", precipitationChance * 100);
    }

    public double getTemperatureFahrenheit() {
        return (temperature * 9/5) + 32;
    }

    public String getFormattedTemperatureFahrenheit() {
        return String.format(Locale.getDefault(), "%.1f°F", getTemperatureFahrenheit());
    }

    @Override
    public String toString() {
        return "HourlyForecast{" +
                "time=" + getFormattedTime() +
                ", temperature=" + getFormattedTemperature() +
                ", condition='" + condition + '\'' +
                ", precipitationChance=" + getFormattedPrecipitationChance() +
                ", icon='" + icon + '\'' +
                '}';
    }
}