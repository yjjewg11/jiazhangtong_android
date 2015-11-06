package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * DianZan
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/13 23:55
 */
public class DianZan extends BaseModel {
    @Expose
    private int count;
    @Expose
    private String names;
    @Expose
    private boolean canDianzan;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public boolean isCanDianzan() {
        return canDianzan;
    }

    public void setCanDianzan(boolean canDianzan) {
        this.canDianzan = canDianzan;
    }

    @Override
    public String toString() {
        return "DianZan{" +
                "canDianzan=" + canDianzan +
                ", count=" + count +
                ", names='" + names + '\'' +
                '}';
    }
}
