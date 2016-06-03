package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2015/11/3.
 */
public class TrainTeacherInfoObject {

    @Expose
    private String uuid;@Expose
    private String name;@Expose
    private String img;@Expose
    private String summary;@Expose
    private String course_title;@Expose
    private String content;@Expose
    private int ct_stars;

    @Override
    public String toString() {
        return "TrainTeacherInfoObject{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", summary='" + summary + '\'' +
                ", course_title='" + course_title + '\'' +
                ", content='" + content + '\'' +
                ", ct_stars=" + ct_stars +
                '}';
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCourse_title() {
        return course_title;
    }

    public void setCourse_title(String course_title) {
        this.course_title = course_title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCt_stars() {
        return ct_stars;
    }

    public void setCt_stars(int ct_stars) {
        this.ct_stars = ct_stars;
    }
}
