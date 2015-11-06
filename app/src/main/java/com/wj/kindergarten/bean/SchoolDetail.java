package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

public class SchoolDetail extends BaseModel{

    @Expose
    private String uuid;@Expose
    private String img;@Expose
    private String brand_name;@Expose
    private String link_tel;@Expose
    private String map_point;@Expose
    private String description;@Expose
    private String createtime;@Expose
    private int ct_stars;@Expose
    private long ct_study_students;

    @Override
    public String toString() {
        return "SchoolDetail{" +
                "brand_name='" + brand_name + '\'' +
                ", uuid='" + uuid + '\'' +
                ", img='" + img + '\'' +
                ", link_tel='" + link_tel + '\'' +
                ", map_point='" + map_point + '\'' +
                ", description='" + description + '\'' +
                ", createtime='" + createtime + '\'' +
                ", ct_stars=" + ct_stars +
                ", ct_study_students=" + ct_study_students +
                '}';
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getCt_stars() {
        return ct_stars;
    }

    public void setCt_stars(int ct_stars) {
        this.ct_stars = ct_stars;
    }

    public long getCt_study_students() {
        return ct_study_students;
    }

    public void setCt_study_students(long ct_study_students) {
        this.ct_study_students = ct_study_students;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLink_tel() {
        return link_tel;
    }

    public void setLink_tel(String link_tel) {
        this.link_tel = link_tel;
    }

    public String getMap_point() {
        return map_point;
    }

    public void setMap_point(String map_point) {
        this.map_point = map_point;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
