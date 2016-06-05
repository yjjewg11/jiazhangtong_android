package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * AddressBookEmot
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-12 21:54
 */
public class AddressBookEmot extends BaseModel{
    @Expose
    private ArrayList<Emot> list;

    public ArrayList<Emot> getList() {
        return list;
    }

    public void setList(ArrayList<Emot> list) {
        this.list = list;
    }
}
