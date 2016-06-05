package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/3/10.
 */
public class BoutiqueDianzanListObj extends BaseModel{
    @Expose
    private String useruuid;@Expose
    private String username;

    @Override
    public String toString() {
        return "BoutiqueDianzanListObj{" +
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
