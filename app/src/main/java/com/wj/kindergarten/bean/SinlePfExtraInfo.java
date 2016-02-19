package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/2/19.
 */
public class SinlePfExtraInfo extends BaseModel{
    @Expose
    private boolean isFavor;@Expose
    private PfDianZan dianZan;

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

    @Override
    public String toString() {
        return "SinlePfExtraInfo{" +
                "isFavor=" + isFavor +
                ", dianZan=" + dianZan +
                '}';
    }
}
