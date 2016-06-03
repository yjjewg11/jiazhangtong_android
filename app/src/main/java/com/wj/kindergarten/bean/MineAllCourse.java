package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

public class MineAllCourse extends BaseModel{

    @Expose
    private String uuid;@Expose
    private String plandate;@Expose
    private String address;@Expose
    private String context;@Expose
    private String name;@Expose
    private String readyfor;

    public String getReadyfor() {
        return readyfor;
    }

    public void setReadyfor(String readyfor) {
        this.readyfor = readyfor;
    }

    @Override
    public String toString() {
        return "MineAllCourse{" +
                "uuid='" + uuid + '\'' +
                ", plandate='" + plandate + '\'' +
                ", address='" + address + '\'' +
                ", context='" + context + '\'' +
                ", name='" + name + '\'' +
                ", readyfor='" + readyfor + '\'' +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlandate() {
        return plandate;
    }

    public void setPlandate(String plandate) {
        this.plandate = plandate;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
