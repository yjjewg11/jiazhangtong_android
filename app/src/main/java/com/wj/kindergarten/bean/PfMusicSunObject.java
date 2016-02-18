package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/2/18.
 */
public class PfMusicSunObject extends BaseModel{
    @Expose
    private String path;@Expose
    private String title;@Expose
    private String uuid;

    public void setPath(String path) {
        this.path = path;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "PfMusicSunObject{" +
                "path='" + path + '\'' +
                ", title='" + title + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PfMusicSunObject object = (PfMusicSunObject) o;

        return !(uuid != null ? !uuid.equals(object.uuid) : object.uuid != null);

    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }
}
