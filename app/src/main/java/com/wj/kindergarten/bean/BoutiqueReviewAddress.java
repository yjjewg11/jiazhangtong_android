package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/2/29.
 */
public class BoutiqueReviewAddress extends BaseModel{
    @Expose
    private String data_id;@Expose
    private String share_url;

    @Override
    public String toString() {
        return "BoutiqueReviewAddress{" +
                "data_id='" + data_id + '\'' +
                ", share_url='" + share_url + '\'' +
                '}';
    }

    public String getData_id() {
        return data_id;
    }

    public void setData_id(String data_id) {
        this.data_id = data_id;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }
}
