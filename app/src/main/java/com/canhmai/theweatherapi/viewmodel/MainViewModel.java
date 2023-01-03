package com.canhmai.theweatherapi.viewmodel;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.canhmai.theweatherapi.api.clientservice.ClientService;

import com.canhmai.theweatherapi.api.response.aircondition.AirconditionResponse;
import com.canhmai.theweatherapi.api.response.curentweather.CurrenWeatherResponse;
import com.canhmai.theweatherapi.api.response.forecast.ForecastResponse;
import com.canhmai.theweatherapi.api.response.getlocation.LocationResponse;
import com.canhmai.theweatherapi.callback.OnResponseCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {


    public static final String GET_AIR_CONDTION = "GET_AIR_CONDTION";
    public static final String GET_CURENT_WEATHER = "GET_CURENT_WEATHER";
    public static final String GET_LAT_LON = "GET_LAT_LON";
    public boolean isCalled = false;
    private OnResponseCallback onResponseCallback;
    private CurrenWeatherResponse currenWeatherResponse;

    public void setCurrenWeatherResponse(CurrenWeatherResponse currenWeatherResponse) {
        this.currenWeatherResponse = currenWeatherResponse;
    }

    public CurrenWeatherResponse getCurrenWeatherResponse() {
        return currenWeatherResponse;
    }

    private String lat;
    private String lon;
    private boolean getCurrent = false;
    private boolean getForecast = false;
    private boolean getAir = false;

    public boolean getCurrent() {
        return getCurrent;
    }

    public boolean checkStateComplete() {

        return getAir && getCurrent && getForecast;

    }

    public void setCurrent(boolean getCurrent) {
        this.getCurrent = getCurrent;
    }

    public boolean getForecast() {
        return getForecast;
    }

    public void setForecast(boolean getForecast) {
        this.getForecast = getForecast;
    }

    public boolean getAir() {
        return getAir;
    }

    public void setAir(boolean getAir) {
        this.getAir = getAir;
    }

    public void setOnResponseCallback(OnResponseCallback onResponseCallback) {
        this.onResponseCallback = onResponseCallback;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public static final String GET_WEATHER_FORECAST = "GET_WEATHER_FORECAST";
    private boolean networkState = true;


    public void setNetworkState(boolean b) {
        networkState = b;
    }

    public boolean isNetworkState() {
        return networkState;
    }

    public void getWeatherAPI() {
        Log.e(TAG, "getWeatherAPI: calling api");
        ClientService.getInstance().getRetrofit().getWeatherCurrent(lat, lon).enqueue(initResponeCallback(GET_CURENT_WEATHER));
    }

    protected <T> Callback<T> initResponeCallback(String key) {

        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "onResponse: calling success");
                    handleAPISuccess(key, response.code() + "", response.body());

                } else {
                    Log.e(TAG, "onResponse: calling fail");
                    handleAPIFail(key, response.code(), response.message());
                    //   response.errorBody().toString()
                    call.cancel();
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.e(TAG, "onResponse: canot call");
                handleAPIFail(key, 9999, t.getMessage());
                call.cancel();
            }
        };

    }

    private void handleAPIFail(String key, int code, String s) {
        onResponseCallback.onApiFailure(key, null, code + " " + s);
        Log.e(TAG, "handleAPIFail: " + key + " : " + s);
    }

    private void handleAPISuccess(String key, String s, Object body) {
        if (key.equals(GET_CURENT_WEATHER)) {
            CurrenWeatherResponse weatherResponse = (CurrenWeatherResponse) body;
            onResponseCallback.onApiSuccess(key, weatherResponse, s);
        } else if (key.equals(GET_WEATHER_FORECAST)) {
            ForecastResponse forecastResponse = (ForecastResponse) body;
            onResponseCallback.onApiSuccess(key, forecastResponse, s);
        } else if (key.equals(GET_AIR_CONDTION)) {
            AirconditionResponse response = (AirconditionResponse) body;

            onResponseCallback.onApiSuccess(key, response, s);

        } else if (key.equals(GET_LAT_LON)) {
            List<LocationResponse> response = (List<LocationResponse>) body;
            if (response.isEmpty()) {
                onResponseCallback.onApiFailure(key, null, "404");
            } else {
                onResponseCallback.onApiSuccess(key, response, s);
            }

        }


    }


    public void setLat(double latitude) {
        this.lat = latitude + "";
    }

    public void setLon(double lontitude) {
        this.lon = lontitude + "";
    }

    public void getAirContidionAPI() {
        ClientService.getInstance().getRetrofit().getAirCondition(lat, lon).enqueue(initResponeCallback(GET_AIR_CONDTION));

    }

    public void getForecastAPI() {
        ClientService.getInstance().getRetrofit().getWeatherForecast(lat, lon).enqueue(initResponeCallback(GET_WEATHER_FORECAST));

    }

    public void getLocationAPI(String cityName) {
        ClientService.getInstance().getRetrofit().getLatLongAPI(cityName).enqueue(initResponeCallback(GET_LAT_LON));

    }
}
