package com.canhmai.theweatherapi.api.response.curentweather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CloudElement {

    @SerializedName("all")
    @Expose
    private Integer all;

    public Integer getAll() {
        return all;
    }

    public void setAll(Integer all) {
        this.all = all;
    }
}
