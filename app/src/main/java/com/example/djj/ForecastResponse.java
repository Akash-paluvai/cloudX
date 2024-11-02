package com.example.djj;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ForecastResponse {
    private List<HourlyData> hourly;
    private List<DailyData> daily;

    public static class HourlyData {
        private long dt;
        private double temp;
        private double pop;
        private List<WeatherResponse.Weather> weather;

        // Getters
        public long getDt() { return dt; }
        public double getTemp() { return temp; }
        public double getPop() { return pop; }
        public List<WeatherResponse.Weather> getWeather() { return weather; }
    }

    public static class DailyData {
        private long dt;
        private Temp temp;
        private double pop;
        private List<WeatherResponse.Weather> weather;

        @SerializedName("sunrise")
        private long sunrise;

        @SerializedName("sunset")
        private long sunset;

        public static class Temp {
            private double min;
            private double max;

            public double getMin() { return min; }
            public double getMax() { return max; }
        }

        public long getDt() { return dt; }
        public Temp getTemp() { return temp; }
        public double getPop() { return pop; }
        public List<WeatherResponse.Weather> getWeather() { return weather; }

        // Getters for sunrise and sunset
        public long getSunrise() { return sunrise; }
        public long getSunset() { return sunset; }
    }

    public List<HourlyData> getHourly() { return hourly; }
    public List<DailyData> getDaily() { return daily; }
}
