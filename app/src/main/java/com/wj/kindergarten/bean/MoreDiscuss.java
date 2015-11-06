package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

public class MoreDiscuss extends BaseModel{

    @Expose
<<<<<<< HEAD
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
=======
    private String uuid;
    @Expose
    private String create_time;
    @Expose
    private String ext_uuid;
    @Expose
    private String content;
    @Expose
    private String create_user;
    @Expose
    private String create_useruuid;
    @Expose
    private String type;
    @Expose
    private String score;

>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0

    @Override
    public String toString() {
        return "MoreDiscuss{" +
                "content='" + content + '\'' +
                ", uuid='" + uuid + '\'' +
                ", create_time='" + create_time + '\'' +
<<<<<<< HEAD
                ", create_user='" + create_user + '\'' +
=======
                ", ext_uuid='" + ext_uuid + '\'' +
                ", create_user='" + create_user + '\'' +
                ", create_useruuid='" + create_useruuid + '\'' +
                ", type='" + type + '\'' +
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
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

<<<<<<< HEAD
=======
    public String getCreate_useruuid() {
        return create_useruuid;
    }

    public void setCreate_useruuid(String create_useruuid) {
        this.create_useruuid = create_useruuid;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
