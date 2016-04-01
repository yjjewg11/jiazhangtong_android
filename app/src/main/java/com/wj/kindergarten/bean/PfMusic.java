package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by tangt on 2016/1/21.
 */
public class PfMusic extends BaseModel {
    @Expose
    private PfMusicSun list;

    @Override
    public String toString() {
        return "PfMusic{" +
                "list=" + list +
                '}';
    }

    public PfMusicSun getList() {
        return list;
    }

    public void setList(PfMusicSun list) {
        this.list = list;
    }
}
