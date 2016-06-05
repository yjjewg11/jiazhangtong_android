package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2015/12/17.
 */
public class AddChild extends BaseModel{
    @Expose
    private AddChildSun data;

    public AddChildSun getData() {
        return data;
    }

    public void setData(AddChildSun data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AddChild{" +
                "data=" + data +
                '}';
    }
}
