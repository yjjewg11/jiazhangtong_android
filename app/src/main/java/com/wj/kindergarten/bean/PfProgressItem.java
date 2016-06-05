package com.wj.kindergarten.bean;

/**
 * Created by tangt on 2016/1/24.
 */
public class PfProgressItem {
    private int progress;
    private String path;

    public PfProgressItem() {
    }

    public PfProgressItem(int progress, String path) {
        this.progress = progress;
        this.path = path;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
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

        PfProgressItem that = (PfProgressItem) o;

        return path.equals(that.path);

    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }
}
