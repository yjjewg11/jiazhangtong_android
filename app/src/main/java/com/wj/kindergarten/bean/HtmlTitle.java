package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2015/12/21.
 */
public class HtmlTitle extends BaseModel {
    @Expose
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HtmlTitle{" +
                "data='" + data + '\'' +
                '}';
    }
}
