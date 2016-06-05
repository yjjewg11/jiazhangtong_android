package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

public class SpecialCourseInfoList extends BaseModel{

    @Expose
    private SpecialCourseInfo list;

    public SpecialCourseInfo getList() {
        return list;
    }

    public void setList(SpecialCourseInfo list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "SpecialCourseInfoList{" +
                "list=" + list +
                '}';
    }
}
