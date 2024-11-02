package com.example.djj;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherResponse {
    private String name;
    private Main main;
    private List<Weather> weather;
    private Wind wind;
    private Sys sys;

    public static class Main {
        private double temp;
        @SerializedName("feels_like")
        private double feelsLike;
        private int humidity;

        public double getTemp() { return temp; }
        public double getFeelsLike() { return feelsLike; }
        public int getHumidity() { return humidity; }
    }

    public static class Weather {
        private String main;
        private String description;
        private String icon;

        // Getters
        public String getMain() { return main; }
        public String getDescription() { return description; }
        public String getIcon() { return icon; }
    }

    public static class Wind {
        private double speed;

        // Getter
        public double getSpeed() { return speed; }
    }

    public static class Sys {
        private long sunrise;
        private long sunset;

        // Getters
        public long getSunrise() { return sunrise; }
        public long getSunset() { return sunset; }
    }

    public String getName() { return name; }
    public Main getMain() { return main; }
    public List<Weather> getWeather() { return weather; }
    public Wind getWind() { return wind; }
    public Sys getSys() { return sys; }
}