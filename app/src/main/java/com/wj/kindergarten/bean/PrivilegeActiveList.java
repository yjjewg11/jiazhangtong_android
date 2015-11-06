package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2015/11/4.
 */
public class PrivilegeActiveList extends BaseModel{
    @Expose
    private PrivilegeActiveListSun list;

    @Override
    public String toString() {
        return "PrivilegeActiveList{" +
                "list=" + list +
                '}';
    }

    public PrivilegeActiveListSun getList() {
        return list;
    }

    public void setList(PrivilegeActiveListSun list) {
        this.list = list;
    }
}
