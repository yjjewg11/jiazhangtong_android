package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * ReplyList
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/28 10:05
 */
public class ReplyList extends BaseModel {
    @Expose
    private DataReplyList list;

    public DataReplyList getList() {
        return list;
    }

    public void setList(DataReplyList list) {
        this.list = list;
    }
}
