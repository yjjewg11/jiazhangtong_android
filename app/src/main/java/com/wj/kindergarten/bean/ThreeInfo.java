package com.wj.kindergarten.bean;

/**
 * Created by tangt on 2016/3/25.
 */
public class ThreeInfo {
    String access_token;
    String loginType;

    public ThreeInfo() {
    }

    public ThreeInfo(String access_token, String loginType) {
        this.access_token = access_token;
        this.loginType = loginType;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}
