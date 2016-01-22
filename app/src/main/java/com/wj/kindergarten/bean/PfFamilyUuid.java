package com.wj.kindergarten.bean;

import net.tsz.afinal.annotation.sqlite.Table;

import java.io.Serializable;

/**
 * Created by tangt on 2016/1/22.
 */
public class PfFamilyUuid implements Serializable {
    private String maxTime;
    private String minTime;
    private String family_uuid;

    public String getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(String maxTime) {
        this.maxTime = maxTime;
    }

    public String getMinTime() {
        return minTime;
    }

    public void setMinTime(String minTime) {
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
                "maxTime='" + maxTime + '\'' +
                ", minTime='" + minTime + '\'' +
                ", family_uuid='" + family_uuid + '\'' +
                '}';
    }
}
