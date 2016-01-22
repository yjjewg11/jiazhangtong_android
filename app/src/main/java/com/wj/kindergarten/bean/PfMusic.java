package com.wj.kindergarten.bean;

import java.io.Serializable;

/**
 * Created by tangt on 2016/1/21.
 */
public class PfMusic implements Serializable {
    private String title;
    private String path;

    public PfMusic(String title, String path) {
        this.title = title;
        this.path = path;
    }

    public PfMusic() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PfMusic pfMusic = (PfMusic) o;

        return path.equals(pfMusic.path);

    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }
}
