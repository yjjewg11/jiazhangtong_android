package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

public class MoreDiscussList extends BaseModel{
    @Expose
    private MoreDiscussListSun list;

    public MoreDiscussListSun getList() {
        return list;
    }

    public void setList(MoreDiscussListSun list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "MoreDiscussList{" +
                "list=" + list +
                '}';
    }
}
