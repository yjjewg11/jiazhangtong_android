package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

public class TeacherCount extends BaseModel{
    @Expose
    private String uuid;@Expose
    private String name;@Expose
    private String img;@Expose
    private String course_title;@Expose
    private String summary;@Expose
    private int ct_stars;

    @Override
    public String toString() {
        return "TeacherCount{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", course_title='" + course_title + '\'' +
                ", summary='" + summary + '\'' +
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

    public String getCourse_title() {
        return course_title;
    }

    public void setCourse_title(String course_title) {
        this.course_title = course_title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getCt_stars() {
        return ct_stars;
    }

    public void setCt_stars(int ct_stars) {
        this.ct_stars = ct_stars;
    }
}
