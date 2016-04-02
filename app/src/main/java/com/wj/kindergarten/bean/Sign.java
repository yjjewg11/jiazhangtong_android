package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Sign
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/2 2:22
 */
public class Sign extends BaseModel {
    @Expose
    private String sign_name;
    @Expose
    private String groupname;
    @Expose
    private String groupuuid;
    @Expose
    private String studentuuid;
    @Expose
    private String uuid;
    @Expose
    private int type;
    @Expose
    private String sign_uuid;
    @Expose
    private String studentname;
    @Expose
    private String cardid;
    @Expose
    private String sign_time;

    public String getSign_name() {
        return sign_name;
    }

    public void setSign_name(String sign_name) {
        this.sign_name = sign_name;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getGroupuuid() {
        return groupuuid;
    }

    public void setGroupuuid(String groupuuid) {
        this.groupuuid = groupuuid;
    }

    public String getStudentuuid() {
        return studentuuid;
    }

    public void setStudentuuid(String studentuuid) {
        this.studentuuid = studentuuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSign_uuid() {
        return sign_uuid;
    }

    public void setSign_uuid(String sign_uuid) {
        this.sign_uuid = sign_uuid;
    }

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public String getSign_time() {
        return sign_time;
    }

    public void setSign_time(String sign_time) {
        this.sign_time = sign_time;
    }
}
