package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Interaction
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/27 10:55
 */
public class Interaction extends BaseModel {
    @Expose
    private String content;
    @Expose
    private String title;
    @Expose
    private String classuuid;
    @Expose
    private String update_time;
    @Expose
    private int count;
    @Expose
    private String share_url;
    @Expose
    private String create_time;
    @Expose
    private String usertype;
    @Expose
    private String create_user;
    @Expose
    private String reply_time;
    @Expose
    private String uuid;
    @Expose
    private String create_useruuid;

    @Expose
    private DianZan dianzan;
    @Expose
    private ChildReplyList replyPage;
    @Expose
    private List<String> imgsList;
    @Expose
    private String create_img;


    public String getCreate_img() {
        return create_img;
    }

    public void setCreate_img(String create_img) {
        this.create_img = create_img;
    }

    public List<String> getImgsList() {
        return imgsList;
    }

    public void setImgsList(List<String> imgsList) {
        this.imgsList = imgsList;
    }

    public DianZan getDianzan() {
        return dianzan;
    }

    public void setDianzan(DianZan dianzan) {
        this.dianzan = dianzan;
    }

    public ChildReplyList getReplyPage() {
        return replyPage;
    }

    public void setReplyPage(ChildReplyList replyPage) {
        this.replyPage = replyPage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClassuuid() {
        return classuuid;
    }

    public void setClassuuid(String classuuid) {
        this.classuuid = classuuid;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public String getReply_time() {
        return reply_time;
    }

    public void setReply_time(String reply_time) {
        this.reply_time = reply_time;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCreate_useruuid() {
        return create_useruuid;
    }

    public void setCreate_useruuid(String create_useruuid) {
        this.create_useruuid = create_useruuid;
    }
}
