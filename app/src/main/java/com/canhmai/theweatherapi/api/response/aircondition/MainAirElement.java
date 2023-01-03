package com.canhmai.theweatherapi.api.response.aircondition;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainAirElement {
    @SerializedName("aqi")
    @Expose
    private Integer aqi;

    public Integer getAqi() {
        return aqi;
    }

    public void setAqi(Integer aqi) {
        this.aqi = aqi;
    }
}
