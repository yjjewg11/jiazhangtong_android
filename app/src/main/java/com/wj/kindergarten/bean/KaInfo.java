package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * KaInfo
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-25 16:49
 */
public class KaInfo {
    @Expose
    private String uuid;
    @Expose
    private String cardid;
    @Expose
    private String name;
    @Expose
    private String create_useruuid;
    @Expose
    private String note;
    @Expose
    private String createtime;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreate_useruuid() {
        return create_useruuid;
    }

    public void setCreate_useruuid(String create_useruuid) {
        this.create_useruuid = create_useruuid;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
