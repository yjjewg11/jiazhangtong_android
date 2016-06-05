package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

import java.util.List;

public class TrainCourseList extends BaseModel{

    @Expose
    private List<TrainCourse> list;

    public List<TrainCourse> getTrainCourseList() {
        return list;
    }

    public void setTrainCourseList(List<TrainCourse> trainCourseList) {
        this.list = trainCourseList;
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
