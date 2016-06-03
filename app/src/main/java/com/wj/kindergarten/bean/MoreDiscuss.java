package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

public class MoreDiscuss extends BaseModel{

    @Expose
    private String uuid;@Expose
    private String create_time;@Expose
    private String content;@Expose
    private String create_user;@Expose
    private int score;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    @Override
    public String toString() {
        return "MoreDiscuss{" +
                "content='" + content + '\'' +
                ", uuid='" + uuid + '\'' +
                ", create_time='" + create_time + '\'' +
                ", create_user='" + create_user + '\'' +
                ", score='" + score + '\'' +
                '}';
    }

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
