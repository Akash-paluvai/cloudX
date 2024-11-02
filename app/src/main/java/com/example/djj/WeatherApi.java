package com.example.djj;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("weather")
    Call<WeatherResponse> getCurrentWeather(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("appid") String apiKey,
            @Query("units") String units
    );

    @GET("forecast")
    Call<ForecastResponse> getForecast(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("appid") String apiKey,
            @Query("units") String units
    );

    @GET("http://api.openweathermap.org/geo/1.0/direct")
    Call<List<GeocodingResponse>> getGeocode(
            @Query("q") String cityName,
            @Query("limit") int limit,
            @Query("appid") String apiKey
    );
}