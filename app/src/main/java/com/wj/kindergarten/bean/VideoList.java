package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/5/31.
 */
public class VideoList extends BaseModel {
    @Expose
    private VideoListSun list;

    @Override
    public String toString() {
        return "VideoList{" +
                "list=" + list +
                '}';
    }

    public VideoListSun getList() {
        return list;
    }

    public void setList(VideoListSun list) {
        this.list = list;
    }
}
