package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * MoreData
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-17 16:29
 */
public class MoreData extends BaseModel{
    @Expose
    private ArrayList<More> list;

    public ArrayList<More> getList() {
        return list;
    }

    public void setList(ArrayList<More> list) {
        this.list = list;
    }
}
