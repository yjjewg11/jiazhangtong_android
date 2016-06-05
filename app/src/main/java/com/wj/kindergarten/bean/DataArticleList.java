package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * DataArticleList
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/4 22:57
 */
public class DataArticleList extends BaseListModel{
    @Expose
    private List<Article> data;

    public List<Article> getData() {
        return data;
    }

    public void setData(List<Article> data) {
        this.data = data;
    }
}
