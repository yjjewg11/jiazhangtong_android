package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * AppraiseTeacherOver
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/6 10:22
 */
public class AppraiseTeacherOver extends BaseModel {
    @Expose
    private String content;
    @Expose
    private String teacheruuid;
    @Expose
    private String type;

    private boolean isEditState = false;

    public boolean isEditState() {
        return isEditState;
    }

    public void setIsEditState(boolean isEditState) {
        this.isEditState = isEditState;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTeacheruuid() {
        return teacheruuid;
    }

    public void setTeacheruuid(String teacheruuid) {
        this.teacheruuid = teacheruuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
