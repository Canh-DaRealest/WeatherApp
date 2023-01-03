package com.canhmai.theweatherapi.api.response.forecast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastResponse {

    @SerializedName("cod")
    @Expose
    private String cod;
    @SerializedName("message")
    @Expose
    private Integer message;
    @SerializedName("cnt")
    @Expose
    private Integer cnt;
    @SerializedName("list")
    @Expose
    private java.util.List<ForecastListElemnt> list = null;
    @SerializedName("city")
    @Expose
    private CityElement city;

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public Integer getMessage() {
        return message;
    }

    public void setMessage(Integer message) {
        this.message = message;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public List<ForecastListElemnt> getList() {
        return list;
    }

    public void setList(List<ForecastListElemnt> forecastList) {
        this.list = list;
    }

    public CityElement getCity() {
        return city;
    }

    public void setCity(CityElement city) {
        this.city = city;
    }
}
