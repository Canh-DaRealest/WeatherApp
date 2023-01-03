package com.canhmai.theweatherapi.api.response.forecast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForecastSysElement {

    @SerializedName("pod")
    @Expose
    private String pod;

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }
}
