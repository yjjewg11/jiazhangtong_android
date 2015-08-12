package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Course
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/2 17:04
 */
public class Course extends BaseModel {
    @Expose
    private String uuid;
    @Expose
    private String plandate;
    @Expose
    private String morning;
    @Expose
    private String afternoon;
    @Expose
    private String classuuid;
    @Expose
    private int count;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPlandate() {
        return plandate;
    }

    public void setPlandate(String plandate) {
        this.plandate = plandate;
    }

    public String getMorning() {
        return morning;
    }

    public void setMorning(String morning) {
        this.morning = morning;
    }

    public String getAfternoon() {
        return afternoon;
    }

    public void setAfternoon(String afternoon) {
        this.afternoon = afternoon;
    }

    public String getClassuuid() {
        return classuuid;
    }

    public void setClassuuid(String classuuid) {
        this.classuuid = classuuid;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
