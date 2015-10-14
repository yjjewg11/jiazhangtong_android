package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

public class OnceSpecialCourse extends BaseModel{
    @Expose
    private String uuid;
    @Expose
    private String groupuuid;
    @Expose
    private String title;
    //返回的是Html的字符串
    @Expose
    private String content;
    @Expose
    private String type;
    @Expose
    private String subtype;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGroupuuid() {
        return groupuuid;
    }

    public void setGroupuuid(String groupuuid) {
        this.groupuuid = groupuuid;
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

    @Override
    public String toString() {
        return "OnceSpecialCourse{" +
                "content='" + content + '\'' +
                ", uuid='" + uuid + '\'' +
                ", groupuuid='" + groupuuid + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", subtype='" + subtype + '\'' +
                '}';
    }
}
