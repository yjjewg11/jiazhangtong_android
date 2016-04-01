package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/1/13.
 */
public class PfAlbumListSun extends BaseModel{
    @Expose
    private String uuid;@Expose
    private String title;@Expose
    private String herald;@Expose
    private String photo_count;@Expose
    private String status;

    public PfAlbumListSun(String title) {
        this.title = title;
    }

    public PfAlbumListSun() {
    }

    @Override
    public String toString() {
        return "PfAlbumListSun{" +
                "uuid='" + uuid + '\'' +
                ", title='" + title + '\'' +
                ", herald='" + herald + '\'' +
                ", photo_count='" + photo_count + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PfAlbumListSun that = (PfAlbumListSun) o;

        return uuid.equals(that.uuid);

    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
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
