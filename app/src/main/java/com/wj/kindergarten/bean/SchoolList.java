package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * SchoolList
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-17 17:40
 */
public class SchoolList extends BaseModel{
    @Expose
    private ArrayList<School> list;

    public ArrayList<School> getList() {
        return list;
    }

    public void setList(ArrayList<School> list) {
        this.list = list;
    }
}
