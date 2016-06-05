package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/3/9.
 */
public class PfInfoDianZanObj extends BaseModel{
    @Expose
    private String useruuid;@Expose
    private String username;

    public PfInfoDianZanObj(String useruuid) {
        this.useruuid = useruuid;
    }

    public PfInfoDianZanObj() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PfInfoDianZanObj obj = (PfInfoDianZanObj) o;

        return useruuid.equals(obj.useruuid);

    }

    @Override
    public int hashCode() {
        return useruuid.hashCode();
    }

    @Override
    public String toString() {
        return "PfInfoDianZanObj{" +
                "useruuid='" + useruuid + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public String getUseruuid() {
        return useruuid;
    }

    public void setUseruuid(String useruuid) {
        this.useruuid = useruuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
