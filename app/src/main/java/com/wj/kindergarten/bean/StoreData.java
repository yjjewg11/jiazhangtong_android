package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * StoreData
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-18 11:16
 */
public class StoreData {
    @Expose
    private ArrayList<Store> data;

    public ArrayList<Store> getData() {
        return data;
    }

    public void setData(ArrayList<Store> data) {
        this.data = data;
    }
}
