package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by tanghongbin on 16/4/6.
 */
public class MineChildTeachers extends BaseModel {
    @Expose
    private List<MineChildTeacherObj> list;

    public List<MineChildTeacherObj> getList() {
        return list;
    }

    public void setList(List<MineChildTeacherObj> list) {
        this.list = list;
    }
}
