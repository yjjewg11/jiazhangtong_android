package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * SignList
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/2 2:20
 */
public class SignList extends BaseModel {
    @Expose
    private DataSignList list;

    public DataSignList getList() {
        return list;
    }

    public void setList(DataSignList list) {
        this.list = list;
    }
}
