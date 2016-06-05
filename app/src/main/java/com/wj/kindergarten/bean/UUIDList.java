package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/1/23.
 */
public class UUIDList extends BaseModel{
    @Expose
    private UUIDListSun list;

    @Override
    public String toString() {
        return "UUIDList{" +
                "list=" + list +
                '}';
    }

    public UUIDListSun getList() {
        return list;
    }

    public void setList(UUIDListSun list) {
        this.list = list;
    }
}
