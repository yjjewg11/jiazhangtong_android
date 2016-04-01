package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by tangt on 2016/1/13.
 */
public class PfAlbumList extends BaseModel{
    @Expose
    private List<PfAlbumListSun> list;

    @Override
    public String toString() {
        return "PfAlbumList{" +
                "list=" + list +
                '}';
    }

    public List<PfAlbumListSun> getList() {
        return list;
    }

    public void setList(List<PfAlbumListSun> list) {
        this.list = list;
    }
}
