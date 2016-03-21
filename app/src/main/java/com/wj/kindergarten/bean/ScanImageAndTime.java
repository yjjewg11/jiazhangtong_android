package com.wj.kindergarten.bean;

/**
 * Created by tangt on 2016/3/19.
 */
public class ScanImageAndTime {
    public ScanImageAndTime(String path, String time) {
        this.path = path;
        this.time = time;
    }

    public ScanImageAndTime() {
    }

    private String path;
    private String time;
    private String choose = "全选";

    public String getChoose() {
        return choose;
    }

    public void setChoose(String choose) {
        this.choose = choose;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
