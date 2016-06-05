package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * NoticeList
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/28 23:55
 */
public class NoticeList extends BaseModel {
    @Expose
    private DataNoticeList list;

    public DataNoticeList getList() {
        return list;
    }

    public void setList(DataNoticeList list) {
        this.list = list;
    }
}
