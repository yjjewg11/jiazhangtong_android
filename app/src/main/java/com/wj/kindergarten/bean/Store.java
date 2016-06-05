package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Store
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-18 11:18
 */
public class Store {
    @Expose
    private String show_img;
    @Expose
    private String createtime;
    @Expose
    private String title;
    @Expose
    private String show_uuid;
    @Expose
    private String user_uuid;
    @Expose
    private String uuid;
    @Expose
    private int type;
    @Expose
    private String reluuid;
    @Expose
    private String show_name;
    @Expose
    private String url;

    public String getShow_img() {
        return show_img;
    }

    public void setShow_img(String show_img) {
        this.show_img = show_img;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShow_uuid() {
        return show_uuid;
    }

    public void setShow_uuid(String show_uuid) {
        this.show_uuid = show_uuid;
    }

    public String getUser_uuid() {
        return user_uuid;
    }

    public void setUser_uuid(String user_uuid) {
        this.user_uuid = user_uuid;
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

    public String getReluuid() {
        return reluuid;
    }

    public void setReluuid(String reluuid) {
        this.reluuid = reluuid;
    }

    public String getShow_name() {
        return show_name;
    }

    public void setShow_name(String show_name) {
        this.show_name = show_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
