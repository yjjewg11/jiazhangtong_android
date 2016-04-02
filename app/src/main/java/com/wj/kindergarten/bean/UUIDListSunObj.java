package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/1/23.
 */
public class UUIDListSunObj extends BaseModel{
    @Expose
    private String u;@Expose
    private int s;

    @Override
    public String toString() {
        return "UUIDListSunObj{" +
                "u='" + u + '\'' +
                ", s=" + s +
                '}';
    }

    public String getU() {
        return u;
    }

    public void setU(String u) {
        this.u = u;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }
}
