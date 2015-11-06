package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

public class OnceSpecialCourseList extends BaseModel{
    @Expose
<<<<<<< HEAD
    private OnceSpecialCourse data;@Expose
    private boolean isFavor;@Expose
    private String share_url;@Expose
    private String link_tel;

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
=======
    private OnceSpecialCourse data;
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0

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
<<<<<<< HEAD
                ", isFavor=" + isFavor +
                ", share_url='" + share_url + '\'' +
                ", link_tel='" + link_tel + '\'' +
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
                '}';
    }
}
