package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2015/12/3.
 */
public class ConfigObject extends BaseModel {
    @Expose
    private ConfigObjectSun data;
    @Expose
    private String   md5;

    public ConfigObjectSun getData() {
        return data;
    }

    public void setData(ConfigObjectSun data) {
        this.data = data;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    @Override
    public String toString() {
        return "ConfigObject{" +
                "data=" + data +
                ", md5='" + md5 + '\'' +
                '}';
    }
}
