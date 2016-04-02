package com.wj.kindergarten.bean;

/**
 * Created by tangt on 2016/3/16.
 */
public class FindAlreadyPath {
    private String localPath;

    @Override
    public String toString() {
        return "FindAlreadyPath{" +
                "localPath='" + localPath + '\'' +
                '}';
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
}
