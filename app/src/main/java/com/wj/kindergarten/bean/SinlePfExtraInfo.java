package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/2/19.
 */
public class SinlePfExtraInfo extends BaseModel{
    @Expose
    private boolean isFavor;@Expose
    private PfDianZan dianZan;@Expose
    private int reply_count;@Expose
    private int status;

    @Override
    public String toString() {
        return "SinlePfExtraInfo{" +
                "isFavor=" + isFavor +
                ", dianZan=" + dianZan +
                ", reply_count=" + reply_count +
                ", status=" + status +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getReply_count() {
        return reply_count;
    }

    public void setReply_count(int reply_count) {
        this.reply_count = reply_count;
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

}
