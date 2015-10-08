package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

public class TrainClass {
    @Expose
    private String class_name;
    @Expose
    private String student_uuid;
    @Expose
    private String class_uuid;

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getClass_uuid() {
        return class_uuid;
    }

    public void setClass_uuid(String class_uuid) {
        this.class_uuid = class_uuid;
    }

    public String getStudent_uuid() {
        return student_uuid;
    }

    public void setStudent_uuid(String student_uuid) {
        this.student_uuid = student_uuid;
    }

    @Override
    public String toString() {
        return "TrainClass{" +
                "class_name='" + class_name + '\'' +
                ", student_uuid='" + student_uuid + '\'' +
                ", class_uuid='" + class_uuid + '\'' +
                '}';
    }
}

