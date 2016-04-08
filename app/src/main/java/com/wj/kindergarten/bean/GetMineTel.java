package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tanghongbin on 16/4/7.
 */
public class GetMineTel extends BaseModel{
    @Expose
    private GetMineTelSun data;

    public GetMineTelSun getData() {
        return data;
    }

    public void setData(GetMineTelSun data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GetMineTel{" +
                "data=" + data +
                '}';
    }
}
