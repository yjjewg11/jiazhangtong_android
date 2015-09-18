package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * School
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-17 17:40
 */
public class School {
    @Expose
    private String address;
    @Expose
    private String company_name;
    @Expose
    private String img;
    @Expose
    private String map_point;
    @Expose
    private String uuid;
    @Expose
    private int type;
    @Expose
    private String brand_name;
    @Expose
    private String link_tel;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getLink_tel() {
        return link_tel;
    }

    public void setLink_tel(String link_tel) {
        this.link_tel = link_tel;
    }
}
