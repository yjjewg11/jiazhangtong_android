package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

public class StudyStateObjectList extends BaseModel{
    @Expose
    private StudyStateObjectListSun list;

    @Override
    public String toString() {
        return "StudyStateObjectList{" +
                "list=" + list +
                '}';
    }

    public StudyStateObjectListSun getList() {
        return list;
    }

    public void setList(StudyStateObjectListSun list) {
        this.list = list;
    }
}
