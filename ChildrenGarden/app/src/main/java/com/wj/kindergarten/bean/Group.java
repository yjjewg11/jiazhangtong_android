package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Group
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/12 23:25
 */
public class Group extends BaseModel {
    @Expose
    private String uuid;
    @Expose
    private String brand_name;
    @Expose
    private String img;
    @Expose
    private String link_tel;
    @Expose
    private String address;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
