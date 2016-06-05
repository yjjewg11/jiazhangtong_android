package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * DataSignList
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/2 2:21
 */
public class DataSignList extends BaseListModel{
    @Expose
    private List<Sign> data;

    public List<Sign> getData() {
        return data;
    }

    public void setData(List<Sign> data) {
        this.data = data;
    }
}
