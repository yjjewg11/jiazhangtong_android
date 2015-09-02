package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Teacher
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-12 15:07
 */
public class Teacher implements Serializable{
    @Expose
    private String img;
    @Expose
    private int type;
    @Expose
    private String tel;
    @Expose
    private String name;
    @Expose
    private String teacher_uuid;

    private boolean isFormMessage = false;

    public boolean isFormMessage() {
        return isFormMessage;
    }

    public void setIsFormMessage(boolean isFormMessage) {
        this.isFormMessage = isFormMessage;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher_uuid() {
        return teacher_uuid;
    }

    public void setTeacher_uuid(String teacher_uuid) {
        this.teacher_uuid = teacher_uuid;
    }
}
