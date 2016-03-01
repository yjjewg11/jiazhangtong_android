package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by tangt on 2016/3/1.
 */
public class BoutiqueAllpic extends BaseModel {
    @Expose
    private List<AllPfAlbumSunObject> list;

    public List<AllPfAlbumSunObject> getList() {
        return list;
    }

    public void setList(List<AllPfAlbumSunObject> list) {
        this.list = list;
    }
}