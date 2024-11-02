package com.example.djj;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherApiClient {
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    public static final String API_KEY = "b9381c70aa8203a933186a010a621eec";

    private static Retrofit retrofit = null;

    public static WeatherApi getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(WeatherApi.class);
    }
}