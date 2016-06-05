package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/3/2.
 */
public class SyncUploadPicObj extends BaseModel{
    @Expose
    private String md5;@Expose
    private String phone_uuid;@Expose
    private String family_uuid;

    public String getFamily_uuid() {
        return family_uuid;
    }

    public void setFamily_uuid(String family_uuid) {
        this.family_uuid = family_uuid;
    }


    @Override
    public String toString() {
        return "SyncUploadPicObj{" +
                "md5='" + md5 + '\'' +
                ", phone_uuid='" + phone_uuid + '\'' +
                ", family_uuid='" + family_uuid + '\'' +
                '}';
    }

    public String getPhone_uuid() {
        return phone_uuid;
    }

    public void setPhone_uuid(String phone_uuid) {
        this.phone_uuid = phone_uuid;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
