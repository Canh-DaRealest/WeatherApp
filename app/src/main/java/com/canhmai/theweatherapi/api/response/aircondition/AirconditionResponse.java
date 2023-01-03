package com.canhmai.theweatherapi.api.response.aircondition;

import com.canhmai.theweatherapi.api.response.CoordElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AirconditionResponse {


    @SerializedName("coord")
    @Expose
    private CoordElement coord;
    @SerializedName("list")
    @Expose
    private List<ListElement> list = null;

    public CoordElement getCoord() {
        return coord;
    }

    public void setCoord(CoordElement coord) {
        this.coord = coord;
    }

    public java.util.List<ListElement> getList() {
        return list;
    }

    public void setList(java.util.List<ListElement> list) {
        this.list = list;
    }
}

