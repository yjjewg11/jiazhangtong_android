package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Article
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/4 22:57
 */
public class Article extends BaseModel {
    @Expose
    private String uuid;
    @Expose
    private String create_time;
    @Expose
    private String create_user;
    @Expose
    private String create_useruuid;
    @Expose
    private int isimportant;
    @Expose
    private String title;
    @Expose
    private int type;
    @Expose
    private String groupuuid;

    @Expose
    private String message;
    @Expose
    private String content;
    @Expose
    private int count;
    @Expose
    private String share_url;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public String getCreate_useruuid() {
        return create_useruuid;
    }

    public void setCreate_useruuid(String create_useruuid) {
        this.create_useruuid = create_useruuid;
    }

    public int getIsimportant() {
        return isimportant;
    }

    public void setIsimportant(int isimportant) {
        this.isimportant = isimportant;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGroupuuid() {
        return groupuuid;
    }

    public void setGroupuuid(String groupuuid) {
        this.groupuuid = groupuuid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }
}
