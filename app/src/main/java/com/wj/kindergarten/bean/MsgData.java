package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * MsgData
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-17 10:55
 */
public class MsgData {
    @Expose
    private ArrayList<MsgDataModel> data;

    public ArrayList<MsgDataModel> getData() {
        return data;
    }

    public void setData(ArrayList<MsgDataModel> data) {
        this.data = data;
    }
}
