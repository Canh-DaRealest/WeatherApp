package com.canhmai.theweatherapi.api.response.aircondition;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListElement {

    @SerializedName("main")
    @Expose
    private MainAirElement main;
    @SerializedName("components")
    @Expose
    private ComponentsElement components;
    @SerializedName("dt")
    @Expose
    private Integer dt;

    public MainAirElement getMain() {
        return main;
    }

    public void setMain(MainAirElement main) {
        this.main = main;
    }

    public ComponentsElement getComponents() {
        return components;
    }

    public void setComponents(ComponentsElement components) {
        this.components = components;
    }

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }
}
