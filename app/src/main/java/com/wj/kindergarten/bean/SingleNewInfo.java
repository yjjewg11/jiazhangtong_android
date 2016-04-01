package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/1/26.
 */
public class SingleNewInfo extends BaseModel{
    @Expose
    private AllPfAlbumSunObject data;

    @Override
    public String toString() {
        return "SingleNewInfo{" +
                "data=" + data +
                '}';
    }

    public AllPfAlbumSunObject getData() {
        return data;
    }

    public void setData(AllPfAlbumSunObject data) {
        this.data = data;
    }
}
