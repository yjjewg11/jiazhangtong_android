package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2015/11/4.
 */
public class PrivilegeActive extends BaseModel{

    @Expose
    private String uuid;@Expose
    private String group_img;@Expose
    private String title;@Expose
    private String group_name;@Expose
    private String distance;@Expose
    private String count;

    @Override
    public String toString() {
        return "PrivilegeActive{" +
                "uuid='" + uuid + '\'' +
                ", group_img='" + group_img + '\'' +
                ", title='" + title + '\'' +
                ", group_name='" + group_name + '\'' +
                ", distance='" + distance + '\'' +
                ", count='" + count + '\'' +
                '}';
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getGroup_img() {
        return group_img;
    }

    public void setGroup_img(String group_img) {
        this.group_img = group_img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
