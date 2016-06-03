package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/5/31.
 */
public class VideoAccessToken extends BaseModel {
    @Expose
    private String accessToken;@Expose
    private String appKey;

    @Override
    public String toString() {
        return "VideoAccessToken{" +
                "accessToken='" + accessToken + '\'' +
                ", appKey='" + appKey + '\'' +
                '}';
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
