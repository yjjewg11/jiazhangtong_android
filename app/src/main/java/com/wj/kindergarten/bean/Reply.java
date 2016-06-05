package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Reply
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/28 10:06
 */
public class Reply extends BaseModel {
    @Expose
    private String content;
    @Expose
    private String update_time;
    @Expose
    private String newsuuid;
    @Expose
    private String create_time;
    @Expose
    private String usertype;
    @Expose
    private String create_user;
    @Expose
    private String uuid;
    @Expose
    private String type;
    @Expose
    private String create_useruuid;
    @Expose
    private DianZan dianzan;
    @Expose
    private String create_img;

    public String getCreate_img() {
        return create_img;
    }

    public void setCreate_img(String create_img) {
        this.create_img = create_img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getNewsuuid() {
        return newsuuid;
    }

    public void setNewsuuid(String newsuuid) {
        this.newsuuid = newsuuid;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreate_useruuid() {
        return create_useruuid;
    }

    public void setCreate_useruuid(String create_useruuid) {
        this.create_useruuid = create_useruuid;
    }

    public DianZan getDianzan() {
        return dianzan;
    }

    public void setDianzan(DianZan dianzan) {
        this.dianzan = dianzan;
    }


    @Override
    public String toString() {
        return "Reply{" +
                "content='" + content + '\'' +
                ", update_time='" + update_time + '\'' +
                ", newsuuid='" + newsuuid + '\'' +
                ", create_time='" + create_time + '\'' +
                ", usertype='" + usertype + '\'' +
                ", create_user='" + create_user + '\'' +
                ", uuid='" + uuid + '\'' +
                ", type='" + type + '\'' +
                ", create_useruuid='" + create_useruuid + '\'' +
                ", dianzan=" + dianzan +
                ", create_img='" + create_img + '\'' +
                '}';
    }
}
