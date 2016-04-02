package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/3/25.
 */
public class NeedBoundPhoneResult extends BaseModel {
    @Expose
    private String needBindTel;@Expose
    private String access_token;

    @Override
    public String toString() {
        return "NeedBoundPhoneResult{" +
                "needBindTel='" + needBindTel + '\'' +
                ", access_token='" + access_token + '\'' +
                '}';
    }

    public String getNeedBindTel() {
        return needBindTel;
    }

    public void setNeedBindTel(String needBindTel) {
        this.needBindTel = needBindTel;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
