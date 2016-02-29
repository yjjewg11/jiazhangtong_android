package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/2/29.
 */
public class BoutiqueSingleInfoObject extends BaseModel{
    @Expose
    private String uuid;@Expose
    private String title;@Expose
    private String herald;@Expose
    private int photo_count;@Expose
    private int status;@Expose
    private String photo_uuids;

    @Override
    public String toString() {
        return "BoutiqueSingleInfoObject{" +
                "uuid='" + uuid + '\'' +
                ", title='" + title + '\'' +
                ", herald='" + herald + '\'' +
                ", photo_count=" + photo_count +
                ", status=" + status +
                ", photo_uuids='" + photo_uuids + '\'' +
                '}';
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHerald() {
        return herald;
    }

    public void setHerald(String herald) {
        this.herald = herald;
    }

    public int getPhoto_count() {
        return photo_count;
    }

    public void setPhoto_count(int photo_count) {
        this.photo_count = photo_count;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPhoto_uuids() {
        return photo_uuids;
    }

    public void setPhoto_uuids(String photo_uuids) {
        this.photo_uuids = photo_uuids;
    }
}
