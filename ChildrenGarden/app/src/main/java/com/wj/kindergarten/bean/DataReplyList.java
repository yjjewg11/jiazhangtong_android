package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * DataReplyList
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/28 10:06
 */
public class DataReplyList extends BaseListModel {
    @Expose
    private List<Reply> data;

    public List<Reply> getData() {
        return data;
    }

    public void setData(List<Reply> data) {
        this.data = data;
    }
}
