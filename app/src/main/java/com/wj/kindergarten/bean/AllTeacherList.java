package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

public class AllTeacherList extends BaseModel{
    @Expose
    private List<AllTeacher> list;

    public List<AllTeacher> getList() {
        return list;
    }

    public void setList(List<AllTeacher> list) {
        this.list = list;
    }
}
