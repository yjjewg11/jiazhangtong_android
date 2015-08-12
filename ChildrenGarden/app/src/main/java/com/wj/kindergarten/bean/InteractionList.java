package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Interaction
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/27 10:55
 */
public class InteractionList extends BaseModel {
    @Expose
    private DataInteractionList list;

    public DataInteractionList getList() {
        return list;
    }

    public void setList(DataInteractionList list) {
        this.list = list;
    }
}
