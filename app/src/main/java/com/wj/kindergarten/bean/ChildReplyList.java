package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * ChildReplyList
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/14 1:15
 */
public class ChildReplyList extends BaseListModel{
    @Expose
    private List<Reply> data;

    public List<Reply> getData() {
        return data;
    }

    public void setData(List<Reply> data) {
        this.data = data;
    }
}
