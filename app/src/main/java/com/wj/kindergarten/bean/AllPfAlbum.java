package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/1/13.
 */
public class AllPfAlbum extends BaseModel {
    @Expose
    private AllPfAlbumSun list;
    @Expose
    private String lastTime;

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    @Override
    public String toString() {
        return "AllPfAlbum{" +
                "list=" + list +
                ", lastTime='" + lastTime + '\'' +
                '}';
    }

    public AllPfAlbumSun getList() {
        return list;
    }

    public void setList(AllPfAlbumSun list) {
        this.list = list;
    }
}
