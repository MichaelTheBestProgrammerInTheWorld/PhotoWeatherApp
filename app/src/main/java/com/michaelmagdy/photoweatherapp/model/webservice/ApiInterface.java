package com.michaelmagdy.photoweatherapp.model.webservice;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("weather?appid=ce27b19cd9d9f9ba26e8a8d8a58bfd5a")
    Call<WeatherApi> getWeather (
            @Query("lat") String latitude,
            @Query("lon") String longitude
    );
}
