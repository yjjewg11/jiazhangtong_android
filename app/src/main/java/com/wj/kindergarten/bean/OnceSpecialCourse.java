package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

public class OnceSpecialCourse extends BaseModel{
    @Expose
    private String uuid;@Expose
    private String groupuuid;@Expose
    private String title;@Expose
    private String content;@Expose
    private String type;@Expose
    private double fees;@Expose
    private double discountfees;@Expose
    private String subtype;@Expose
    private String schedule;@Expose
    private int ct_stars;@Expose
    private long ct_study_students;@Expose
    private String context;@Expose
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "OnceSpecialCourse{" +
                "uuid='" + uuid + '\'' +
                ", groupuuid='" + groupuuid + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", fees=" + fees +
                ", discountfees=" + discountfees +
                ", subtype='" + subtype + '\'' +
                ", schedule='" + schedule + '\'' +
                ", ct_stars=" + ct_stars +
                ", ct_study_students=" + ct_study_students +
                ", context='" + context + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCt_stars() {
        return ct_stars;
    }

    public void setCt_stars(int ct_stars) {
        this.ct_stars = ct_stars;
    }

    public long getCt_study_students() {
        return ct_study_students;
    }

    public void setCt_study_students(long ct_study_students) {
        this.ct_study_students = ct_study_students;
    }

    public double getDiscountfees() {
        return discountfees;
    }

    public void setDiscountfees(double discountfees) {
        this.discountfees = discountfees;
    }

    public double getFees() {
        return fees;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }

    public String getGroupuuid() {
        return groupuuid;
    }

    public void setGroupuuid(String groupuuid) {
        this.groupuuid = groupuuid;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
