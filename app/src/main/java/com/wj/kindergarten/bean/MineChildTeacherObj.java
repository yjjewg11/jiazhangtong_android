package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tanghongbin on 16/4/6.
 */
public class MineChildTeacherObj extends BaseModel{
    @Expose
    private String uuid;@Expose
    private String name;

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
}
