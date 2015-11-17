package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

public class SpecialCourseTypeList extends BaseModel{

    @Expose
    private List<SpecialCourseType> list;

    public List<SpecialCourseType> getList() {
        return list;
    }

    public void setList(List<SpecialCourseType> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "SpecialCourseTypeList{" +
                "list=" + list +
                '}';
    }
}
