package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by tangt on 2016/3/1.
 */
public class PfAlbumInfoSun extends BaseModel{
    @Expose
    private String uuid;@Expose
    private String title;@Expose
    private String herald;@Expose
    private String photo_count;@Expose
    private String status;


    @Override
    public String toString() {
        return "PfAlbumInfoSun{" +
                "uuid='" + uuid + '\'' +
                ", title='" + title + '\'' +
                ", herald='" + herald + '\'' +
                ", photo_count='" + photo_count + '\'' +
                ", status='" + status + '\'' +
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

    public String getPhoto_count() {
        return photo_count;
    }

    public void setPhoto_count(String photo_count) {
        this.photo_count = photo_count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
