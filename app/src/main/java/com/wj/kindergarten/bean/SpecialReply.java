package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

public class SpecialReply extends BaseModel{
    @Expose
    private String uuid;@Expose
    private String classuuid;@Expose
    private String title;@Expose
    private String content;@Expose
    private String create_use;@Expose
    private String create_useruuid;@Expose
    private String create_time;@Expose
    private String reply_time;@Expose
    private String update_time;

    @Override
    public String toString() {
        return "SpecialReply{" +
                "classuuid='" + classuuid + '\'' +
                ", uuid='" + uuid + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", create_use='" + create_use + '\'' +
                ", create_useruuid='" + create_useruuid + '\'' +
                ", create_time='" + create_time + '\'' +
                ", reply_time='" + reply_time + '\'' +
                ", update_time='" + update_time + '\'' +
                '}';
    }

    public String getClassuuid() {
        return classuuid;
    }

    public void setClassuuid(String classuuid) {
        this.classuuid = classuuid;
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

    public String getCreate_use() {
        return create_use;
    }

    public void setCreate_use(String create_use) {
        this.create_use = create_use;
    }

    public String getCreate_useruuid() {
        return create_useruuid;
    }

    public void setCreate_useruuid(String create_useruuid) {
        this.create_useruuid = create_useruuid;
    }

    public String getReply_time() {
        return reply_time;
    }

    public void setReply_time(String reply_time) {
        this.reply_time = reply_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
