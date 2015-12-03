package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2015/12/3.
 */
public class ConfigObjectSun extends BaseModel{
    @Expose
    private String sns_url;

    public String getSns_url() {
        return sns_url;
    }

    public void setSns_url(String sns_url) {
        this.sns_url = sns_url;
    }

    @Override
    public String toString() {
        return "ConfigObjectSun{" +
                "sns_url='" + sns_url + '\'' +
                '}';
    }
}
