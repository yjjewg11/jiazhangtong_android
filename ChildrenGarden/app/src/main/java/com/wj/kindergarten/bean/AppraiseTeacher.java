package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * AppraiseTeacher
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/6 10:20
 */
public class AppraiseTeacher extends BaseModel {
    @Expose
    private String img;
    @Expose
    private String type;
    @Expose
    private String tel;
    @Expose
    private String name;
    @Expose
    private String teacher_uuid;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
