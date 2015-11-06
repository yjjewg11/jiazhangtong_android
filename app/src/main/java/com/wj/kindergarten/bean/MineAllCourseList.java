package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

public class MineAllCourseList extends BaseModel{
    @Expose
    private MineAllCourseListSun list;

    @Override
    public String toString() {
        return "MineAllCourseList{" +
                "list=" + list +
                '}';
    }

    public MineAllCourseListSun getList() {
        return list;
    }

    public void setList(MineAllCourseListSun list) {
        this.list = list;
    }
}
