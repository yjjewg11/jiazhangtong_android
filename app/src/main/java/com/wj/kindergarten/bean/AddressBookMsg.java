package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * AddressBookMsg
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-13 17:18
 */
public class AddressBookMsg {
    @Expose
    private ArrayList<MyMessage> data;

    public ArrayList<MyMessage> getData() {
        return data;
    }

    public void setData(ArrayList<MyMessage> data) {
        this.data = data;
    }
}

