package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

public class GetAssessState extends BaseModel {

    @Expose
    private String uuid;@Expose
    private String create_time;@Expose
    private String content;@Expose
    private String create_user;@Expose
    private String ext_uuid;@Expose
    private int type;@Expose
    private String score;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public String getExt_uuid() {
        return ext_uuid;
    }

    public void setExt_uuid(String ext_uuid) {
        this.ext_uuid = ext_uuid;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
