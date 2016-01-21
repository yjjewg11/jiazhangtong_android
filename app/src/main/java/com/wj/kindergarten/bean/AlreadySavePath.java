package com.wj.kindergarten.bean;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

import java.io.Serializable;

/**
 * Created by tangt on 2016/1/21.
 */
@Table(name = "already_save_path")
public class AlreadySavePath implements Serializable {
    @Id(column = "already_path")
    private String alreadyPath;

    public AlreadySavePath(String alreadyPath) {
        this.alreadyPath = alreadyPath;
    }

    public AlreadySavePath() {
    }

    public String getAlreadyPath() {
        return alreadyPath;
    }

    public void setAlreadyPath(String alreadyPath) {
        this.alreadyPath = alreadyPath;
    }

    @Override
    public String toString() {
        return "AlreadySavePath{" +
                "alreadyPath='" + alreadyPath + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlreadySavePath that = (AlreadySavePath) o;

        return alreadyPath.equals(that.alreadyPath);

    }

    @Override
    public int hashCode() {
        return alreadyPath.hashCode();
    }
}
