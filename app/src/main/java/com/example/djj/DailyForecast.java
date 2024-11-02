package com.example.djj;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DailyForecast {
    private long date;
    private double minTemp;
    private double maxTemp;
    private String condition;
    private double precipitationChance;
    private String icon;
    private long sunrise;
    private long sunset;


    public DailyForecast() {
    }

    public DailyForecast(long date, double minTemp, double maxTemp, String condition,
                         double precipitationChance, String icon, long sunrise, long sunset) {
        this.date = date;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.condition = condition;
        this.precipitationChance = precipitationChance;
        this.icon = icon;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    // Getters
    public long getDate() {
        return date;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
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

    public long getSunrise() {
        return sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    // Setters
    public void setDate(long date) {
        this.date = date;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
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

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

    // Utility methods
    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());
        return sdf.format(new Date(date * 1000));
    }

    public String getFormattedSunrise() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(new Date(sunrise * 1000));
    }

    public String getFormattedSunset() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(new Date(sunset * 1000));
    }

    public String getFormattedTemperature() {
        return String.format(Locale.getDefault(), "%.1f°C - %.1f°C", minTemp, maxTemp);
    }

    public String getFormattedPrecipitationChance() {
        return String.format(Locale.getDefault(), "%.0f%%", precipitationChance * 100);
    }

    @Override
    public String toString() {
        return "DailyForecast{" +
                "date=" + getFormattedDate() +
                ", temperature=" + getFormattedTemperature() +
                ", condition='" + condition + '\'' +
                ", precipitationChance=" + getFormattedPrecipitationChance() +
                ", icon='" + icon + '\'' +
                ", sunrise=" + getFormattedSunrise() +
                ", sunset=" + getFormattedSunset() +
                '}';
    }
}