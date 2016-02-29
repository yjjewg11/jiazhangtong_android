package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by tangt on 2016/2/18.
 */
    public class PfModeNameObject implements Serializable{
    @Expose
        private String mp3;@Expose
        private String herald;@Expose
        private String title;@Expose
        private String key;

    @Override
    public String toString() {
        return "PfModeNameObject{" +
                "mp3='" + mp3 + '\'' +
                ", herald='" + herald + '\'' +
                ", title='" + title + '\'' +
                ", key='" + key + '\'' +
                '}';
    }

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }

    public String getHerald() {
        return herald;
    }

    public void setHerald(String herald) {
        this.herald = herald;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

