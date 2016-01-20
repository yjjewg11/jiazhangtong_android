package com.wj.kindergarten.bean;

import java.io.Serializable;

/**
 * Created by tangt on 2016/1/20.
 */
public class PicObject implements Serializable {
    private String time;
    private String address;
    private String note;
    private String md5;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public PicObject(String time, String address, String note, String md5) {
        this.time = time;
        this.address = address;
        this.note = note;
        this.md5 = md5;
    }

    public PicObject() {
    }
}
