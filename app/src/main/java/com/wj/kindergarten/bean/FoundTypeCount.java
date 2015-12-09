package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2015/12/9.
 */
public class FoundTypeCount extends BaseModel {
    @Expose
    private FoundTypeCountSun data;

    public FoundTypeCountSun getData() {
        return data;
    }

    public void setData(FoundTypeCountSun data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FoundTypeCount{" +
                "data=" + data +
                '}';
    }
}
