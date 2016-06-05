package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Interaction
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/27 10:55
 */
public class DataInteractionList extends BaseListModel {
    @Expose
    private List<Interaction> data;

    public List<Interaction> getData() {
        return data;
    }

    public void setData(List<Interaction> data) {
        this.data = data;
    }
}
