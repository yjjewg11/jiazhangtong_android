package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

public class SchoolDetailList extends BaseModel{
    @Expose
    private SchoolDetail data;
    @Expose
    private boolean isFavor;
    @Expose
    private String share_url;
    @Expose
    private String obj_url;
    @Expose
    private String recruit_url;

    public String getRecruit_url() {
        return recruit_url;
    }

    public void setRecruit_url(String recruit_url) {
        this.recruit_url = recruit_url;
    }

    public String getObj_url() {
        return obj_url;
    }

    public void setObj_url(String obj_url) {
        this.obj_url = obj_url;
    }

    public boolean isFavor() {
        return isFavor;
    }

    public void setIsFavor(boolean isFavor) {
        this.isFavor = isFavor;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    @Override
    public String toString() {
        return "SchoolDetailList{" +
                "data=" + data +
                ", isFavor=" + isFavor +
                ", share_url='" + share_url + '\'' +
                ", obj_url='" + obj_url + '\'' +
                ", recruit_url='" + recruit_url + '\'' +
                '}';
    }

    public SchoolDetail getData() {
        return data;
    }

    public void setData(SchoolDetail data) {
        this.data = data;
    }
}
