package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2015/12/3.
 */
public class MainTopic extends BaseModel {
    @Expose
    private MainTopicSun data;


    @Override
    public String toString() {
        return "MainTopic{" +
                "data=" + data +
                '}';
    }

    public MainTopicSun getData() {
        return data;
    }

    public void setData(MainTopicSun data) {
        this.data = data;
    }
}
