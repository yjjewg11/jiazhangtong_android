package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/2/23.
 */
public class PfSingleAssessObject {
    @Expose
    private String content;@Expose
    private String create_img;@Expose
    private String create_time;@Expose
    private String create_user;@Expose
    private String uuid;@Expose
    private String to_useruuid;@Expose
    private String create_useruuid;

    @Override
    public String toString() {
        return "PfSingleAssessObject{" +
                "content='" + content + '\'' +
                ", create_img='" + create_img + '\'' +
                ", create_time='" + create_time + '\'' +
                ", create_user='" + create_user + '\'' +
                ", uuid='" + uuid + '\'' +
                ", to_useruuid='" + to_useruuid + '\'' +
                ", create_useruuid='" + create_useruuid + '\'' +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_img() {
        return create_img;
    }

    public void setCreate_img(String create_img) {
        this.create_img = create_img;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
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

    public String getTo_useruuid() {
        return to_useruuid;
    }

    public void setTo_useruuid(String to_useruuid) {
        this.to_useruuid = to_useruuid;
    }

    public String getCreate_useruuid() {
        return create_useruuid;
    }

    public void setCreate_useruuid(String create_useruuid) {
        this.create_useruuid = create_useruuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PfSingleAssessObject object = (PfSingleAssessObject) o;

        return uuid.equals(object.uuid);

    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
