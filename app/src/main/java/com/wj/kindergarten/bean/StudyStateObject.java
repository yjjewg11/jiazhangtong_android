package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

public class StudyStateObject extends BaseModel{



    @Expose
    private String uuid;@Expose
    private String logo;@Expose
    private String group_name;@Expose
    private String course_title;@Expose
    private String student_name;@Expose
    private String class_name;@Expose
    private String groupuuid;@Expose
    private String courseuuid;@Expose
    private String disable_time;@Expose
    private String plandate;

    public String getDisable_time() {
        return disable_time;
    }

    public void setDisable_time(String disable_time) {
        this.disable_time = disable_time;
    }

    public String getPlandate() {
        return plandate;
    }

    public void setPlandate(String plandate) {
        this.plandate = plandate;
    }

    @Override
    public String toString() {
        return "StudyStateObject{" +
                "class_name='" + class_name + '\'' +
                ", uuid='" + uuid + '\'' +
                ", logo='" + logo + '\'' +
                ", group_name='" + group_name + '\'' +
                ", course_title='" + course_title + '\'' +
                ", student_name='" + student_name + '\'' +
                ", groupuuid='" + groupuuid + '\'' +
                ", courseuuid='" + courseuuid + '\'' +
                ", disable_time='" + disable_time + '\'' +
                ", plandate='" + plandate + '\'' +
                '}';
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getCourse_title() {
        return course_title;
    }

    public void setCourse_title(String course_title) {
        this.course_title = course_title;
    }

    public String getCourseuuid() {
        return courseuuid;
    }

    public void setCourseuuid(String courseuuid) {
        this.courseuuid = courseuuid;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroupuuid() {
        return groupuuid;
    }

    public void setGroupuuid(String groupuuid) {
        this.groupuuid = groupuuid;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
