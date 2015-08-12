package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * ArticleDetail
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/5 1:12
 */
public class ArticleDetail extends BaseModel {
    @Expose
    private Article data;
    @Expose
    private int count;
    @Expose
    private String share_url;


    public Article getData() {
        return data;
    }

    public void setData(Article data) {
        this.data = data;
    }

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
}
