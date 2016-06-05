package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;
import com.wj.kindergarten.net.upload.Result;

/**
 * Created by tangt on 2016/3/16.
 */
public class PfResult extends Result {
    @Expose
    private String data_id;

    @Override
    public String toString() {
        return "PfResult{" +
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
