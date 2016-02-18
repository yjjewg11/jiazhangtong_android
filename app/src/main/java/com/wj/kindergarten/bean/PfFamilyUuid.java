package com.wj.kindergarten.bean;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tangt on 2016/1/22.
 */
public class PfFamilyUuid implements Serializable {
    private Date maxTime;
    private Date minTime;
    @Id(column = "family_uuid")
    private String family_uuid;
    private Date updateTime;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Date maxTime) {
        this.maxTime = maxTime;
    }

    public Date getMinTime() {
        return minTime;
    }

    public void setMinTime(Date minTime) {
        this.minTime = minTime;
    }

    public String getFamily_uuid() {
        return family_uuid;
    }

    public void setFamily_uuid(String family_uuid) {
        this.family_uuid = family_uuid;
    }

    @Override
    public String toString() {
        return "PfFamilyUuid{" +
                "maxTime=" + maxTime +
                ", minTime=" + minTime +
                ", family_uuid='" + family_uuid + '\'' +
                '}';
    }

}
