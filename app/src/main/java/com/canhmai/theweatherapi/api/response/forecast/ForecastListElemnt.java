package com.canhmai.theweatherapi.api.response.forecast;

import com.canhmai.theweatherapi.api.response.curentweather.CloudElement;
import com.canhmai.theweatherapi.api.response.curentweather.WeatherElement;
import com.canhmai.theweatherapi.api.response.curentweather.WindElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForecastListElemnt {

    @SerializedName("dt")
    @Expose
    private Integer dt;
    @SerializedName("main")
    @Expose
    private ForecastMainElement main;
    @SerializedName("weather")
    @Expose
    private java.util.List<WeatherElement> weather = null;
    @SerializedName("clouds")
    @Expose
    private CloudElement clouds;
    @SerializedName("wind")
    @Expose
    private WindElement wind;
    @SerializedName("visibility")
    @Expose
    private Integer visibility;
    @SerializedName("pop")
    @Expose
    private Double pop;
    @SerializedName("sys")
    @Expose
    private ForecastSysElement sys;
    @SerializedName("dt_txt")
    @Expose
    private String dtTxt;

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public ForecastMainElement getMain() {
        return main;
    }

    public void setMain(ForecastMainElement main) {
        this.main = main;
    }

    public java.util.List<WeatherElement> getWeather() {
        return weather;
    }

    public void setWeather(java.util.List<WeatherElement> weather) {
        this.weather = weather;
    }

    public CloudElement getClouds() {
        return clouds;
    }

    public void setClouds(CloudElement clouds) {
        this.clouds = clouds;
    }

    public WindElement getWind() {
        return wind;
    }

    public void setWind(WindElement wind) {
        this.wind = wind;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public Double getPop() {
        return pop;
    }

    public void setPop(Double pop) {
        this.pop = pop;
    }

    public ForecastSysElement getSys() {
        return sys;
    }

    public void setSys(ForecastSysElement sys) {
        this.sys = sys;
    }

    public String getDtTxt() {
        return dtTxt;
    }

    public void setDtTxt(String dtTxt) {
        this.dtTxt = dtTxt;
    }
}
