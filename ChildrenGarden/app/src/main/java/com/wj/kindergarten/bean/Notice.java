package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Notice
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/28 23:57
 */
public class Notice extends BaseModel {
    @Expose
    private String uuid;
    @Expose
    private String groupuuid;
    @Expose
    private String title;
    @Expose
    private String create_user;
    @Expose
    private String create_useruuid;
    @Expose
    private String create_time;
    @Expose
    private String message;
    @Expose
    private int isimportant;
    @Expose
    private String classuuids;
    @Expose
    private String classnames;
    @Expose
    private DianZan dianzan;
    @Expose
    private ChildReplyList replyPage;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIsimportant() {
        return isimportant;
    }

    public void setIsimportant(int isimportant) {
        this.isimportant = isimportant;
    }

    public String getClassuuids() {
        return classuuids;
    }

    public void setClassuuids(String classuuids) {
        this.classuuids = classuuids;
    }

    public String getClassnames() {
        return classnames;
    }

    public void setClassnames(String classnames) {
        this.classnames = classnames;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getGroupuuid() {
        return groupuuid;
    }

    public void setGroupuuid(String groupuuid) {
        this.groupuuid = groupuuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
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
}
