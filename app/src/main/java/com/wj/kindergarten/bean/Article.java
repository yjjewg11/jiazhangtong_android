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
<<<<<<< HEAD
    private String url;
    @Expose
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
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
    private Zan dianzan;
    @Expose
    private int count;

<<<<<<< HEAD
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
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

    public Zan getDianzan() {
        return dianzan;
    }

    public void setDianzan(Zan dianzan) {
        this.dianzan = dianzan;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
<<<<<<< HEAD

    @Override
    public String toString() {
        return "Article{" +
                "content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", uuid='" + uuid + '\'' +
                ", create_time='" + create_time + '\'' +
                ", create_user='" + create_user + '\'' +
                ", create_useruuid='" + create_useruuid + '\'' +
                ", isimportant=" + isimportant +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", groupuuid='" + groupuuid + '\'' +
                ", message='" + message + '\'' +
                ", dianzan=" + dianzan +
                ", count=" + count +
                '}';
    }
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
}
