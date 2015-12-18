package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2015/12/18.
 */
public class MainTopicSun extends BaseModel{
    @Expose
    private String title;
    @Expose
    private String url;

    @Override
    public String toString() {
        return "MainTopicSun{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
