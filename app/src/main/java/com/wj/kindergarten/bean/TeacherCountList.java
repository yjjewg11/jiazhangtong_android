package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

public class TeacherCountList extends BaseModel{
    @Expose
    private TeacherCountListSun list;

    @Override
    public String toString() {
        return "TeacherCountList{" +
                "list=" + list +
                '}';
    }

    public TeacherCountListSun getList() {
        return list;
    }

    public void setList(TeacherCountListSun list) {
        this.list = list;
    }
}
