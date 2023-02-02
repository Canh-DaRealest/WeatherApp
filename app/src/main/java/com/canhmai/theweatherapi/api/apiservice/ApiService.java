package com.canhmai.theweatherapi.api.apiservice;

import com.canhmai.theweatherapi.api.response.aircondition.AirconditionResponse;
import com.canhmai.theweatherapi.api.response.curentweather.CurrenWeatherResponse;
import com.canhmai.theweatherapi.api.response.forecast.ForecastResponse;
import com.canhmai.theweatherapi.api.response.getlocation.LocationResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    public static final String API_KEY = "d521a9f72e4f43178ce171338222512";

    @GET("data/2.5/weather?appid=14d1e0f9316a58d75a7cc962a9d17ff8&units=metric&lang=vi")
    Call<CurrenWeatherResponse> getWeatherCurrent(@Query("lat") String lat, @Query("lon") String lon);

    @GET("data/2.5/forecast?appid=14d1e0f9316a58d75a7cc962a9d17ff8&units=metric&lang=vi&cnt=12")
    Call<ForecastResponse> getWeatherForecast(@Query("lat") String lat, @Query("lon") String lon);

    @GET("data/2.5/air_pollution?appid=14d1e0f9316a58d75a7cc962a9d17ff8&units=metric&lang=vie")
    Call<AirconditionResponse> getAirCondition(@Query("lat") String lat, @Query("lon") String lon);

    @GET("geo/1.0/direct?limit=1&appid=14d1e0f9316a58d75a7cc962a9d17ff8")
    Call<List<LocationResponse>> getLatLongAPI(@Query("q") String cityName);
}
