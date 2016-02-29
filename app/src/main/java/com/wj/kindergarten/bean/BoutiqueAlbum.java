package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/1/31.
 */
public class BoutiqueAlbum extends BaseModel {
    @Expose
    private BoutiqueAlbumList list;

    public BoutiqueAlbumList getList() {
        return list;
    }

    public void setList(BoutiqueAlbumList list) {
        this.list = list;
    }
}
