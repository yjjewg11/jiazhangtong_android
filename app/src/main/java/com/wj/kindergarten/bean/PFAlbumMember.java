package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/3/1.
 */
public class PFAlbumMember extends BaseModel{
    @Expose
    private String uuid;@Expose
    private String family_uuid;@Expose
    private String create_time;@Expose
    private String user_uuid;@Expose
    private String tel;@Expose
    private String family_name;

    @Override
    public String toString() {
        return "PFAlbumMember{" +
                "uuid='" + uuid + '\'' +
                ", family_uuid='" + family_uuid + '\'' +
                ", create_time='" + create_time + '\'' +
                ", user_uuid='" + user_uuid + '\'' +
                ", tel='" + tel + '\'' +
                ", family_name='" + family_name + '\'' +
                '}';
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFamily_uuid() {
        return family_uuid;
    }

    public void setFamily_uuid(String family_uuid) {
        this.family_uuid = family_uuid;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUser_uuid() {
        return user_uuid;
    }

    public void setUser_uuid(String user_uuid) {
        this.user_uuid = user_uuid;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PFAlbumMember member = (PFAlbumMember) o;

        return uuid.equals(member.uuid);

    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
