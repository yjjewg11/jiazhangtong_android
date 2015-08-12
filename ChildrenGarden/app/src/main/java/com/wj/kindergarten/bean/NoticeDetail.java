package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * NoticeDetail
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/29 1:21
 */
public class NoticeDetail extends BaseModel {
    @Expose
    private int count;
    @Expose
    private String share_url;
    @Expose
    private Notice data;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public Notice getData() {
        return data;
    }

    public void setData(Notice data) {
        this.data = data;
    }
}
