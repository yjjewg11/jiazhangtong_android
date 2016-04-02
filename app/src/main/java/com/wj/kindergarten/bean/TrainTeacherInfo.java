package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2015/11/3.
 */
public class TrainTeacherInfo extends BaseModel {
    @Expose
    private TrainTeacherInfoObject data;

    @Override
    public String toString() {
        return "TrainTeacherInfo{" +
                "data=" + data +
                '}';
    }

    public TrainTeacherInfoObject getData() {
        return data;
    }

    public void setData(TrainTeacherInfoObject data) {
        this.data = data;
    }
}
