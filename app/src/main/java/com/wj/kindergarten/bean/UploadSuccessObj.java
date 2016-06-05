package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/3/16.
 */
public class UploadSuccessObj extends BaseModel {
    @Expose
    private String data_id;

    @Override
    public String toString() {
        return "UploadSuccessObj{" +
                "data_id='" + data_id + '\'' +
                '}';
    }

    public String getData_id() {
        return data_id;
    }

    public void setData_id(String data_id) {
        this.data_id = data_id;
    }
}
