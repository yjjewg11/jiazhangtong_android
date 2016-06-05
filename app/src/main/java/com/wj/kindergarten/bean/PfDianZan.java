package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/2/19.
 */
public class PfDianZan extends BaseModel{
    @Expose
    private int dianzan_count;@Expose
    private int yidianzan;

    @Override
    public String toString() {
        return "PfDianZan{" +
                "dianzan_count=" + dianzan_count +
                ", yidianzan=" + yidianzan +
                '}';
    }

    public int getDianzan_count() {
        return dianzan_count;
    }

    public void setDianzan_count(int dianzan_count) {
        this.dianzan_count = dianzan_count;
    }

    public int getYidianzan() {
        return yidianzan;
    }

    public void setYidianzan(int yidianzan) {
        this.yidianzan = yidianzan;
    }
}
