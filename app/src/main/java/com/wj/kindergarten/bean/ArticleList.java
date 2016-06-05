package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * ArticleList
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/4 22:56
 */
public class ArticleList extends BaseModel{
    @Expose
    private DataArticleList list;

    public DataArticleList getList() {
        return list;
    }

    public void setList(DataArticleList list) {
        this.list = list;
    }
}
