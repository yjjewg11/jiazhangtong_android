package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/1/13.
 */
public class AllPfAlbum extends BaseModel {
    @Expose
    private AllPfAlbumSun list;


    @Override
    public String toString() {
        return "AllPfAlbum{" +
                "list=" + list +
                '}';
    }

    public AllPfAlbumSun getList() {
        return list;
    }

    public void setList(AllPfAlbumSun list) {
        this.list = list;
    }
}
