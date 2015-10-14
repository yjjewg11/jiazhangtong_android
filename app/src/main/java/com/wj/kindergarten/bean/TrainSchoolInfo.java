package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

public class TrainSchoolInfo extends BaseModel{
    @Expose
    private String uuid;
    @Expose
    private String create_time;
    @Expose
    private String brand_name;
    @Expose
    private String company_name;
    @Expose
    private String type;
    @Expose
    private String status;
    @Expose
    private String map_point;
    @Expose
    private String link_tel;
    @Expose
    private String img;
    @Expose
    private String address;

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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    @Override
    public String toString() {
        return "TrainSchoolInfo{" +
                "brand_name='" + brand_name + '\'' +
                ", uuid='" + uuid + '\'' +
                ", link_tel='" + link_tel + '\'' +
                ", map_point='" + map_point + '\'' +

                '}';
    }
}
