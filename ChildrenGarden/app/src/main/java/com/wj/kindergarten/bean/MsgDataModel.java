package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * MsgDataModel
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-17 10:55
 */
public class MsgDataModel {
    @Expose
    private String message;
    @Expose
    private String rel_uuid;
    @Expose
    private String title;
    @Expose
    private String revice_useruuid;
    @Expose
    private String group_uuid;
    @Expose
    private int isread;
    @Expose
    private String create_time;
    @Expose
    private String uuid;
    @Expose
    private int type;
    @Expose
    private String url;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRel_uuid() {
        return rel_uuid;
    }

    public void setRel_uuid(String rel_uuid) {
        this.rel_uuid = rel_uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRevice_useruuid() {
        return revice_useruuid;
    }

    public void setRevice_useruuid(String revice_useruuid) {
        this.revice_useruuid = revice_useruuid;
    }

    public String getGroup_uuid() {
        return group_uuid;
    }

    public void setGroup_uuid(String group_uuid) {
        this.group_uuid = group_uuid;
    }

    public int getIsread() {
        return isread;
    }

    public void setIsread(int isread) {
        this.isread = isread;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
