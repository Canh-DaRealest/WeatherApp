package com.canhmai.theweatherapi.api.response.curentweather;

import com.canhmai.theweatherapi.api.response.CoordElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CurrenWeatherResponse {
    @SerializedName("coord")
    @Expose
    private CoordElement coord;
    @SerializedName("weather")
    @Expose
    private List<WeatherElement> weather = null;
    @SerializedName("base")
    @Expose
    private String base;
    @SerializedName("main")
    @Expose
    private CurrentMainElement main;
    @SerializedName("visibility")
    @Expose
    private Integer visibility;
    @SerializedName("wind")
    @Expose
    private WindElement wind;
    @SerializedName("clouds")
    @Expose
    private CloudElement clouds;
    @SerializedName("dt")
    @Expose
    private Integer dt;
    @SerializedName("sys")
    @Expose
    private SysElement sys;
    @SerializedName("timezone")
    @Expose
    private Integer timezone;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cod")
    @Expose
    private Integer cod;

    public CoordElement getCoord() {
        return coord;
    }

    public void setCoord(CoordElement coord) {
        this.coord = coord;
    }

    public List<WeatherElement> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherElement> weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public CurrentMainElement getMain() {
        return main;
    }

    public void setMain(CurrentMainElement main) {
        this.main = main;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public WindElement getWind() {
        return wind;
    }

    public void setWind(WindElement wind) {
        this.wind = wind;
    }

    public CloudElement getClouds() {
        return clouds;
    }

    public void setClouds(CloudElement clouds) {
        this.clouds = clouds;
    }

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public SysElement getSys() {
        return sys;
    }

    public void setSys(SysElement sys) {
        this.sys = sys;
    }

    public Integer getTimezone() {
        return timezone;
    }

    public void setTimezone(Integer timezone) {
        this.timezone = timezone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }
}
