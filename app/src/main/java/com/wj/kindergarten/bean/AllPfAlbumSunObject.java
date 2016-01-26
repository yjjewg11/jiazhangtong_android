package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import net.tsz.afinal.annotation.sqlite.Id;

/**
 * Created by tangt on 2016/1/13.
 */
//可自己指定表名 ：
//    @Table(name = "aaa")
public class AllPfAlbumSunObject extends BaseModel{
    @Expose
    private String create_time;
    private int id;
    @Expose
    @Id(column = "uuid")
    private String uuid;@Expose
    private String photo_time;@Expose
    private String path;@Expose
    private int type;@Expose
    private String address;@Expose
    private String note;
    @Expose
    private String family_uuid;@Expose
    private String create_useruuid;@Expose
    private int status;@Expose
    private String md5;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AllPfAlbumSunObject object = (AllPfAlbumSunObject) o;

        return uuid.equals(object.uuid);

    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFamily_uuid() {
        return family_uuid;
    }

    public void setFamily_uuid(String family_uuid) {
        this.family_uuid = family_uuid;
    }

    public String getCreate_useruuid() {
        return create_useruuid;
    }

    public void setCreate_useruuid(String create_useruuid) {
        this.create_useruuid = create_useruuid;
    }

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
                "create_time='" + create_time + '\'' +
                ", id=" + id +
                ", uuid='" + uuid + '\'' +
                ", photo_time='" + photo_time + '\'' +
                ", path='" + path + '\'' +
                ", type=" + type +
                ", address='" + address + '\'' +
                ", note='" + note + '\'' +
                ", family_uuid='" + family_uuid + '\'' +
                ", create_useruuid='" + create_useruuid + '\'' +
                ", status=" + status +
                '}';
    }


    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getMd5() {
        return md5;
    }
}
