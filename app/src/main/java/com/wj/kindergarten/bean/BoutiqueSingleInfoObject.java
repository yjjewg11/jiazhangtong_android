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
    private String photo_uuids;@Expose
    private String template_key;@Expose
    private String mp3;

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }

    public String getTemplate_key() {
        return template_key;
    }

    public void setTemplate_key(String template_key) {
        this.template_key = template_key;
    }

    @Override
    public String toString() {
        return "BoutiqueSingleInfoObject{" +
                "uuid='" + uuid + '\'' +
                ", title='" + title + '\'' +
                ", herald='" + herald + '\'' +
                ", photo_count=" + photo_count +
                ", status=" + status +
                ", photo_uuids='" + photo_uuids + '\'' +
                ", template_key='" + template_key + '\'' +
                ", mp3='" + mp3 + '\'' +
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
