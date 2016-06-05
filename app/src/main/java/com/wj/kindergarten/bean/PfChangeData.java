package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/1/23.
 */
public class PfChangeData extends BaseModel{
    @Expose
    private int newDataCount;
    @Expose
    private int updateDataCount;

    @Override
    public String toString() {
        return "PfChangeData{" +
                "newDataCount=" + newDataCount +
                ", updateDataCount=" + updateDataCount +
                '}';
    }

    public int getNewDataCount() {
        return newDataCount;
    }

    public void setNewDataCount(int newDataCount) {
        this.newDataCount = newDataCount;
    }

    public int getUpdateDataCount() {
        return updateDataCount;
    }

    public void setUpdateDataCount(int updateDataCount) {
        this.updateDataCount = updateDataCount;
    }
}
