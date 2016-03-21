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
    private int id;
    private String localPath;
    //0是成功，1是等待，3是失败
    private int status;
    private Date success_time;
    private String family_uuid;
    private String data_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData_id() {
        return data_id;
    }

    public void setData_id(String data_id) {
        this.data_id = data_id;
    }

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

    public String getFamily_uuid() {
        return family_uuid;
    }

    public void setFamily_uuid(String family_uuid) {
        this.family_uuid = family_uuid;
    }

    @Override
    public String toString() {
        return "AlreadySavePath{" +
                "localPath='" + localPath + '\'' +
                ", status=" + status +
                ", success_time=" + success_time +
                ", family_uuid='" + family_uuid + '\'' +
                ", data_id='" + data_id + '\'' +
                '}';
    }
}
