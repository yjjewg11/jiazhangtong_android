package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Ka
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-25 16:49
 */
public class Ka extends BaseModel{
    @Expose
    private ArrayList<KaInfo> list;

    public ArrayList<KaInfo> getList() {
        return list;
    }

    public void setList(ArrayList<KaInfo> list) {
        this.list = list;
    }
}
