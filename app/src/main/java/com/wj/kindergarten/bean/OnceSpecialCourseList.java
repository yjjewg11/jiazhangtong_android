package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

public class OnceSpecialCourseList extends BaseModel{
    @Expose
    private OnceSpecialCourse data;@Expose
    private boolean isFavor;@Expose
    private String share_url;@Expose
    private String link_tel;@Expose
    private String obj_url;@Expose
    private String age_min_max;

    public String getAge_min_max() {
        return age_min_max;
    }

    public void setAge_min_max(String age_min_max) {
        this.age_min_max = age_min_max;
    }

    public String getObj_url() {
        return obj_url;
    }

    public void setObj_url(String obj_url) {
        this.obj_url = obj_url;
    }

    public String getLink_tel() {
        return link_tel;
    }

    public void setLink_tel(String link_tel) {
        this.link_tel = link_tel;
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

    public OnceSpecialCourse getData() {
        return data;
    }

    public void setData(OnceSpecialCourse data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "OnceSpecialCourseList{" +
                "data=" + data +
                ", isFavor=" + isFavor +
                ", share_url='" + share_url + '\'' +
                ", link_tel='" + link_tel + '\'' +
                ", obj_url='" + obj_url + '\'' +
                ", age_min_max='" + age_min_max + '\'' +
                '}';
    }
}
