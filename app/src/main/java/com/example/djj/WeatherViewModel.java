package com.example.djj;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel extends ViewModel {
    private final MutableLiveData<WeatherData> currentWeather = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<WeatherData> getCurrentWeather() {
        return currentWeather;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void fetchWeatherForCity(String cityName) {
        isLoading.setValue(true);
        error.setValue(null);

        WeatherApiClient.getClient().getGeocode(
                cityName,
                1,
                WeatherApiClient.API_KEY
        ).enqueue(new Callback<List<GeocodingResponse>>() {
            @Override
            public void onResponse(Call<List<GeocodingResponse>> call, Response<List<GeocodingResponse>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    GeocodingResponse location = response.body().get(0);
                    fetchWeatherData(location.getLat(), location.getLon());
                } else {
                    error.setValue("City not found");
                    isLoading.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<List<GeocodingResponse>> call, Throwable t) {
                error.setValue("Network error: " + t.getMessage());
                isLoading.setValue(false);
            }
        });
    }

    public void fetchWeatherForLocation(double latitude, double longitude) {
        fetchWeatherData(latitude, longitude);
    }

    private void fetchWeatherData(double latitude, double longitude) {
        isLoading.setValue(true);
        error.setValue(null);

        WeatherApiClient.getClient().getCurrentWeather(
                latitude,
                longitude,
                WeatherApiClient.API_KEY,
                "metric"
        ).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();
                    WeatherData weatherData = new WeatherData.Builder()
                            .location(weatherResponse.getName())
                            .temperature(weatherResponse.getMain().getTemp())
                            .condition(weatherResponse.getWeather().get(0).getMain())
                            .conditionIcon(weatherResponse.getWeather().get(0).getIcon())
                            .windSpeed(weatherResponse.getWind().getSpeed())
                            .humidity(weatherResponse.getMain().getHumidity())
                            .feelsLike(weatherResponse.getMain().getFeelsLike())
                            .sunrise(weatherResponse.getSys().getSunrise())
                            .sunset(weatherResponse.getSys().getSunset())
                            .build();

                    fetchForecastData(latitude, longitude, weatherData);
                } else {
                    error.setValue("Weather data not found");
                    isLoading.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                error.setValue("Network error: " + t.getMessage());
                isLoading.setValue(false);
            }
        });
    }

    private void fetchForecastData(double latitude, double longitude, WeatherData weatherData) {
        WeatherApiClient.getClient().getForecast(
                latitude,
                longitude,
                WeatherApiClient.API_KEY,
                "metric"
        ).enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateWeatherWithForecast(weatherData, response.body());
                } else {
                    applyDefaultForecastData(weatherData);
                    error.setValue("Forecast data not found");
                    isLoading.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                applyDefaultForecastData(weatherData);
                error.setValue("Network error: " + t.getMessage());
                isLoading.setValue(false);
            }
        });
    }

    private void updateWeatherWithForecast(WeatherData weather, ForecastResponse forecast) {
        List<HourlyForecast> hourlyList = new ArrayList<>();
        List<DailyForecast> dailyList = new ArrayList<>();

        if (forecast.getHourly() != null) {
            for (int i = 0; i < Math.min(24, forecast.getHourly().size()); i++) {
                ForecastResponse.HourlyData hourData = forecast.getHourly().get(i);
                hourlyList.add(new HourlyForecast(
                        hourData.getDt(),
                        hourData.getTemp(),
                        hourData.getWeather().get(0).getMain(),
                        hourData.getPop(),
                        hourData.getWeather().get(0).getIcon()
                ));
            }
        } else {
            hourlyList = generateDefaultHourlyData();
        }

        if (forecast.getDaily() != null) {
            for (int i = 0; i < Math.min(7, forecast.getDaily().size()); i++) {
                ForecastResponse.DailyData dayData = forecast.getDaily().get(i);
                dailyList.add(new DailyForecast(
                        dayData.getDt(),
                        dayData.getTemp().getMin(),
                        dayData.getTemp().getMax(),
                        dayData.getWeather().get(0).getMain(),
                        dayData.getPop(),
                        dayData.getWeather().get(0).getIcon(),
                        dayData.getSunrise(),
                        dayData.getSunset()
                ));
            }
        } else {
            dailyList = generateDefaultDailyData();
        }

        WeatherData updatedWeather = new WeatherData.Builder()
                .location(weather.getLocation())
                .temperature(weather.getTemperature())
                .condition(weather.getCondition())
                .conditionIcon(weather.getConditionIcon())
                .windSpeed(weather.getWindSpeed())
                .humidity(weather.getHumidity())
                .feelsLike(weather.getFeelsLike())
                .sunrise(weather.getSunrise())
                .sunset(weather.getSunset())
                .hourlyForecast(hourlyList)
                .dailyForecast(dailyList)
                .build();

        currentWeather.setValue(updatedWeather);
    }

    private void applyDefaultForecastData(WeatherData weather) {
        List<HourlyForecast> hourlyForecast = generateDefaultHourlyData();
        List<DailyForecast> dailyForecast = generateDefaultDailyData();

        WeatherData updatedWeather = new WeatherData.Builder()
                .location(weather.getLocation())
                .temperature(weather.getTemperature())
                .condition(weather.getCondition())
                .conditionIcon(weather.getConditionIcon())
                .windSpeed(weather.getWindSpeed())
                .humidity(weather.getHumidity())
                .feelsLike(weather.getFeelsLike())
                .sunrise(weather.getSunrise())
                .sunset(weather.getSunset())
                .hourlyForecast(hourlyForecast)
                .dailyForecast(dailyForecast)
                .build();

        currentWeather.setValue(updatedWeather);
    }

    private List<HourlyForecast> generateDefaultHourlyData() {
        List<HourlyForecast> defaultHourly = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            defaultHourly.add(new HourlyForecast(
                    System.currentTimeMillis() + (i * 3600 * 1000),
                    Math.random() * 30 + 15,
                    "Cloudy",
                    Math.random(),
                    "01d"
            ));
        }
        return defaultHourly;
    }

    private List<DailyForecast> generateDefaultDailyData() {
        List<DailyForecast> defaultDaily = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            defaultDaily.add(new DailyForecast(
                    System.currentTimeMillis() + (i * 86400 * 1000),
                    Math.random() * 20 + 10,
                    Math.random() * 10 + 20,
                    "Sunny",
                    Math.random(),
                    "01d",
                    System.currentTimeMillis() + (6 * 3600 * 1000),
                    System.currentTimeMillis() + (18 * 3600 * 1000)
            ));
        }
        return defaultDaily;
    }
}
