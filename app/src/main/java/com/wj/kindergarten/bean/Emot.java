package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Emot
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-12 21:49
 */
public class Emot extends BaseModel{
    @Expose
    private int enable;
    @Expose
    private String typeuuid;
    @Expose
    private String uuid;
    @Expose
    private String datavalue;
    @Expose
    private String description;
    @Expose
    private int datakey;

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getTypeuuid() {
        return typeuuid;
    }

    public void setTypeuuid(String typeuuid) {
        this.typeuuid = typeuuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDatavalue() {
        return datavalue;
    }

    public void setDatavalue(String datavalue) {
        this.datavalue = datavalue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDatakey() {
        return datakey;
    }

    public void setDatakey(int datakey) {
        this.datakey = datakey;
    }
}
