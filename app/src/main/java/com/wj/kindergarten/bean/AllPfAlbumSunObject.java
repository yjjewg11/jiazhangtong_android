package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/1/13.
 */
public class AllPfAlbumSunObject extends BaseModel{
    @Expose
    private String uuid;@Expose
    private String photo_time;@Expose
    private String path;@Expose
    private int type;@Expose
    private String address;@Expose
    private String note;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPhoto_time() {
        return photo_time;
    }

    public void setPhoto_time(String photo_time) {
        this.photo_time = photo_time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "AllPfAlbumSunObject{" +
                "uuid='" + uuid + '\'' +
                ", photo_time='" + photo_time + '\'' +
                ", path='" + path + '\'' +
                ", type=" + type +
                ", address='" + address + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
