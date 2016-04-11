package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by tanghongbin on 16/4/11.
 */
public class ImUserInfo extends BaseModel{
    @Expose
    private List<ImUserInfoSun> userinfos;

    @Override
    public String toString() {
        return "ImUserInfo{" +
                "userInfos=" + userinfos +
                '}';
    }

    public List<ImUserInfoSun> getUserInfos() {
        return userinfos;
    }

    public void setUserInfos(List<ImUserInfoSun> userInfos) {
        this.userinfos = userInfos;
    }
}
