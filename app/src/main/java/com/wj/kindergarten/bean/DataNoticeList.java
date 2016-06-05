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
public class DataNoticeList extends BaseListModel {
    @Expose
    private List<Notice> data;

    public List<Notice> getData() {
        return data;
    }

    public void setData(List<Notice> data) {
        this.data = data;
    }
}
