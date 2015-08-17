package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Class
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/12 23:26
 */
public class Class extends BaseModel {
    @Expose
    private String uuid;
    @Expose
    private String name;
    @Expose
    private String groupuuid;

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

    public String getGroupuuid() {
        return groupuuid;
    }

    public void setGroupuuid(String groupuuid) {
        this.groupuuid = groupuuid;
    }
}
