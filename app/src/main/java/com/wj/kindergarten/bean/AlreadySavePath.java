package com.wj.kindergarten.bean;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tangt on 2016/1/21.
 */
@Table(name = "already_save_path")
public class AlreadySavePath implements Serializable {
    @Id(column = "localPath")
    private String localPath;
    private int status;
    private Date success_time;

    public AlreadySavePath(String localPath) {
        this.localPath = localPath;
    }

    public AlreadySavePath() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlreadySavePath that = (AlreadySavePath) o;

        return localPath.equals(that.localPath);

    }

    @Override
    public int hashCode() {
        return localPath.hashCode();
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getSuccess_time() {
        return success_time;
    }

    public void setSuccess_time(Date success_time) {
        this.success_time = success_time;
    }

    @Override
    public String toString() {
        return "AlreadySavePath{" +
                "localPath='" + localPath + '\'' +
                ", status=" + status +
                ", success_time=" + success_time +
                '}';
    }
}
