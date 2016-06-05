package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * AddressBook
 *
 * @Description:通讯录实体
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-12 15:02
 */
public class AddressBook extends BaseModel{
    @Expose
    private ArrayList<Teacher> listKD;//园长列表
    @Expose
    private ArrayList<Teacher> list;//老师列表

    public ArrayList<Teacher> getListKD() {
        return listKD;
    }

    public void setListKD(ArrayList<Teacher> listKD) {
        this.listKD = listKD;
    }

    public ArrayList<Teacher> getList() {
        return list;
    }

    public void setList(ArrayList<Teacher> list) {
        this.list = list;
    }
}
