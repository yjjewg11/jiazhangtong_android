package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/2/29.
 */
public class BoutiqueSingleInfo extends BaseModel{
    @Expose
    private BoutiqueSingleInfoObject data;@Expose
    boolean isFavor;@Expose
    PfDianZan dianZan;@Expose
    String share_url;

    @Override
    public String toString() {
        return "BoutiqueSingleInfo{" +
                "data=" + data +
                ", isFavor=" + isFavor +
                ", dianZan=" + dianZan +
                ", share_url='" + share_url + '\'' +
                '}';
    }

    public BoutiqueSingleInfoObject getData() {
        return data;
    }

    public void setData(BoutiqueSingleInfoObject data) {
        this.data = data;
    }

    public boolean isFavor() {
        return isFavor;
    }

    public void setIsFavor(boolean isFavor) {
        this.isFavor = isFavor;
    }

    public PfDianZan getDianZan() {
        return dianZan;
    }

    public void setDianZan(PfDianZan dianZan) {
        this.dianZan = dianZan;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }
}
