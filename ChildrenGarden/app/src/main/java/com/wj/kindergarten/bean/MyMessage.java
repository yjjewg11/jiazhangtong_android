package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * MyMessage
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-13 17:37
 */
public class MyMessage {
    @Expose
    private String group_name;
    @Expose
    private String message;
    @Expose
    private String title;
    @Expose
    private String revice_useruuid;
    @Expose
    private String revice_user;
    @Expose
    private String send_user;
    @Expose
    private String group_uuid;
    @Expose
    private int isread;
    @Expose
    private String send_useruuid;
    @Expose
    private String create_time;
    @Expose
    private int isdelete;
    @Expose
    private String uuid;
    @Expose
    private int type;

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getRevice_user() {
        return revice_user;
    }

    public void setRevice_user(String revice_user) {
        this.revice_user = revice_user;
    }

    public String getSend_user() {
        return send_user;
    }

    public void setSend_user(String send_user) {
        this.send_user = send_user;
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

    public String getSend_useruuid() {
        return send_useruuid;
    }

    public void setSend_useruuid(String send_useruuid) {
        this.send_useruuid = send_useruuid;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(int isdelete) {
        this.isdelete = isdelete;
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
}
