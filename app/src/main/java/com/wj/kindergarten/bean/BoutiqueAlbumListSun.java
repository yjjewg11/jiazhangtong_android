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
    private String create_username;@Expose
    private String create_time;@Expose
    private int reply_count;@Expose
    private int status;@Expose
    private PfDianZan dianZan;

    @Override
    public String toString() {
        return "BoutiqueAlbumListSun{" +
                "uuid='" + uuid + '\'' +
                ", title='" + title + '\'' +
                ", herald='" + herald + '\'' +
                ", photo_count='" + photo_count + '\'' +
                ", create_useruuid='" + create_useruuid + '\'' +
                ", create_username='" + create_username + '\'' +
                ", create_time='" + create_time + '\'' +
                ", reply_count=" + reply_count +
                ", status=" + status +
                ", dianZan=" + dianZan +
                '}';
    }

    public PfDianZan getDianZan() {
        return dianZan;
    }

    public void setDianZan(PfDianZan dianZan) {
        this.dianZan = dianZan;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

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

    public void setReply_count(int reply_count) {
        this.reply_count = reply_count;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getReply_count() {
        return reply_count;
    }

    public int getStatus() {
        return status;
    }


    public static class DianZanEntity {
        private int dianzan_count;
        private int yidianzan;

        public void setDianzan_count(int dianzan_count) {
            this.dianzan_count = dianzan_count;
        }

        public void setYidianzan(int yidianzan) {
            this.yidianzan = yidianzan;
        }

        public int getDianzan_count() {
            return dianzan_count;
        }

        public int getYidianzan() {
            return yidianzan;
        }
    }
}
