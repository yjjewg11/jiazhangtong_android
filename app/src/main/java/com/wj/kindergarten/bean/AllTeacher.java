package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

public class AllTeacher extends BaseModel{
    @Expose
    private String uuid;@Expose
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
