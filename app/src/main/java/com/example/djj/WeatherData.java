package com.example.djj;

import androidx.annotation.NonNull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class WeatherData {
    private String location;
    private double temperature;
    private String condition;
    private String conditionIcon;
    private double windSpeed;
    private int humidity;
    private double feelsLike;
    private long sunrise;
    private long sunset;
    private int uvIndex;
    private int airQuality;
    private List<HourlyForecast> hourlyForecast;
    private List<DailyForecast> dailyForecast;
    private String timezone;
    private double precipitationProbability;

    private WeatherData(Builder builder) {
        this.location = builder.location;
        this.temperature = builder.temperature;
        this.condition = builder.condition;
        this.conditionIcon = builder.conditionIcon;
        this.windSpeed = builder.windSpeed;
        this.humidity = builder.humidity;
        this.feelsLike = builder.feelsLike;
        this.sunrise = builder.sunrise;
        this.sunset = builder.sunset;
        this.uvIndex = builder.uvIndex;
        this.airQuality = builder.airQuality;
        this.hourlyForecast = builder.hourlyForecast;
        this.dailyForecast = builder.dailyForecast;
        this.timezone = builder.timezone;
        this.precipitationProbability = builder.precipitationProbability;
    }

    public static class Builder {
        private String location;
        private double temperature;
        private String condition;
        private String conditionIcon;
        private double windSpeed;
        private int humidity;
        private double feelsLike;
        private long sunrise;
        private long sunset;
        private int uvIndex;
        private int airQuality;
        private List<HourlyForecast> hourlyForecast;
        private List<DailyForecast> dailyForecast;
        private String timezone = TimeZone.getDefault().getID();
        private double precipitationProbability;

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder temperature(double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder condition(String condition) {
            this.condition = condition;
            return this;
        }

        public Builder conditionIcon(String conditionIcon) {
            this.conditionIcon = conditionIcon;
            return this;
        }

        public Builder windSpeed(double windSpeed) {
            this.windSpeed = windSpeed;
            return this;
        }

        public Builder humidity(int humidity) {
            this.humidity = humidity;
            return this;
        }

        public Builder feelsLike(double feelsLike) {
            this.feelsLike = feelsLike;
            return this;
        }

        public Builder sunrise(long sunrise) {
            this.sunrise = sunrise;
            return this;
        }

        public Builder sunset(long sunset) {
            this.sunset = sunset;
            return this;
        }

        public Builder uvIndex(int uvIndex) {
            this.uvIndex = uvIndex;
            return this;
        }

        public Builder airQuality(int airQuality) {
            this.airQuality = airQuality;
            return this;
        }

        public Builder hourlyForecast(List<HourlyForecast> hourlyForecast) {
            this.hourlyForecast = hourlyForecast;
            return this;
        }

        public Builder dailyForecast(List<DailyForecast> dailyForecast) {
            this.dailyForecast = dailyForecast;
            return this;
        }

        public Builder timezone(String timezone) {
            this.timezone = timezone;
            return this;
        }

        public Builder precipitationProbability(double precipitationProbability) {
            this.precipitationProbability = precipitationProbability;
            return this;
        }

        public WeatherData build() {
            return new WeatherData(this);
        }
    }

    public String getLocation() { return location; }
    public double getTemperature() { return temperature; }
    public String getCondition() { return condition; }
    public String getConditionIcon() { return conditionIcon; }
    public double getWindSpeed() { return windSpeed; }
    public int getHumidity() { return humidity; }
    public double getFeelsLike() { return feelsLike; }
    public long getSunrise() { return sunrise; }
    public long getSunset() { return sunset; }
    public int getUvIndex() { return uvIndex; }
    public int getAirQuality() { return airQuality; }
    public List<HourlyForecast> getHourlyForecast() { return hourlyForecast; }
    public List<DailyForecast> getDailyForecast() { return dailyForecast; }
    public String getTimezone() { return timezone; }
    public double getPrecipitationProbability() { return precipitationProbability; }

    public boolean isDaytime() {
        long currentTime = System.currentTimeMillis() / 1000;
        return currentTime >= sunrise && currentTime < sunset;
    }

    public String getFormattedTemperature() {
        return String.format(Locale.getDefault(), "%.1f°C", temperature);
    }

    public String getFormattedFeelsLike() {
        return String.format(Locale.getDefault(), "%.1f°C", feelsLike);
    }

    public String getFormattedWindSpeed() {
        return String.format(Locale.getDefault(), "%.1f km/h", windSpeed);
    }

    public String getFormattedHumidity() {
        return String.format(Locale.getDefault(), "%d%%", humidity);
    }

    public String getFormattedTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone(timezone));
        return sdf.format(new Date(timestamp * 1000));
    }

    public String getFormattedDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone(timezone));
        return sdf.format(new Date(timestamp * 1000));
    }

    public String getFormattedPrecipitationProbability() {
        return String.format(Locale.getDefault(), "%d%%", (int)(precipitationProbability * 100));
    }

    @NonNull
    @Override
    public String toString() {
        return "WeatherData{" +
                "location='" + location + '\'' +
                ", temperature=" + getFormattedTemperature() +
                ", condition='" + condition + '\'' +
                ", windSpeed=" + getFormattedWindSpeed() +
                ", humidity=" + getFormattedHumidity() +
                ", feelsLike=" + getFormattedFeelsLike() +
                ", sunrise=" + getFormattedTime(sunrise) +
                ", sunset=" + getFormattedTime(sunset) +
                ", uvIndex=" + uvIndex +
                ", airQuality=" + airQuality +
                ", isDaytime=" + isDaytime() +
                ", precipProbability=" + getFormattedPrecipitationProbability() +
                '}';
    }
}