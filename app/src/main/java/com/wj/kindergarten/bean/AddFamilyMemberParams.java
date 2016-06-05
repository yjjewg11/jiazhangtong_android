package com.wj.kindergarten.bean;

import java.io.Serializable;

/**
 * Created by tangt on 2016/3/1.
 */
public class AddFamilyMemberParams implements Serializable {
    private String family_uuid;
    private String tel;
    private String uuid;
    private String family_name;

    public String getFamily_uuid() {
        return family_uuid;
    }

    public void setFamily_uuid(String family_uuid) {
        this.family_uuid = family_uuid;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }
}
