package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

public class SpecialCourseInfoObject extends BaseModel{
    @Expose
<<<<<<< HEAD
    private int ct_stars;@Expose
    private long ct_study_students;@Expose
    private String group_name;@Expose
    private String logo;@Expose
    private String title;@Expose
    private String distance;@Expose
    private String group_img;@Expose
    private String updatetime;@Expose
    private String address;@Expose
    private String map_point;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getGroup_img() {
        return group_img;
    }

    public void setGroup_img(String group_img) {
        this.group_img = group_img;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getMap_point() {
        return map_point;
    }

    public void setMap_point(String map_point) {
        this.map_point = map_point;
    }

    @Expose

    private String uuid;

    @Override
    public String toString() {
        return "SpecialCourseInfoObject{" +
                "ct_stars=" + ct_stars +
                ", ct_study_students=" + ct_study_students +
                ", group_name='" + group_name + '\'' +
                ", logo='" + logo + '\'' +
                ", title='" + title + '\'' +
                ", distance=" + distance +
                ", group_img='" + group_img + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", address='" + address + '\'' +
                ", map_point='" + map_point + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
=======
    private String uuid;
    @Expose
    private String groupuuid;
    @Expose
    private String title;
    @Expose
    private String type;
    @Expose
    private String subtype;

    public String getGroupuuid() {
        return groupuuid;
    }

    public void setGroupuuid(String groupuuid) {
        this.groupuuid = groupuuid;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

<<<<<<< HEAD
=======
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
<<<<<<< HEAD
    public boolean equals(Object o) {

        return uuid.equals(((SpecialCourseInfoObject)o).getUuid());
=======
    public String toString() {
        return "SpecialCourseInfoObject{" +
                "groupuuid='" + groupuuid + '\'' +
                ", uuid='" + uuid + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", subtype='" + subtype + '\'' +
                '}';
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
    }
}
