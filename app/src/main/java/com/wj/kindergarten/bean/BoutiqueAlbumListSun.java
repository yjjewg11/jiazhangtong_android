package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/2/29.
 */
public class BoutiqueAlbumListSun extends BaseModel{
    @Expose
    private String uuid;@Expose
    private String title;@Expose
    private String herald;@Expose
    private String photo_count;@Expose
    private String create_useruuid;@Expose
    private String create_username;

    public String getCreate_useruuid() {
        return create_useruuid;
    }

    public void setCreate_useruuid(String create_useruuid) {
        this.create_useruuid = create_useruuid;
    }

    public String getCreate_username() {
        return create_username;
    }

    public void setCreate_username(String create_username) {
        this.create_username = create_username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoutiqueAlbumListSun sun = (BoutiqueAlbumListSun) o;

        return uuid.equals(sun.uuid);

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

    @Override
    public String toString() {
        return "BoutiqueAlbumListSun{" +
                "uuid='" + uuid + '\'' +
                ", title='" + title + '\'' +
                ", herald='" + herald + '\'' +
                ", photo_count='" + photo_count + '\'' +
                ", create_useruuid='" + create_useruuid + '\'' +
                ", create_username='" + create_username + '\'' +
                '}';
    }
}
