package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/3/2.
 */
public class SyncUploadPic extends BaseModel {
    @Expose
    private SyncUploadPicList list;

    @Override
    public String toString() {
        return "SyncUploadPic{" +
                "list=" + list +
                '}';
    }

    public SyncUploadPicList getList() {
        return list;
    }

    public void setList(SyncUploadPicList list) {
        this.list = list;
    }
}
