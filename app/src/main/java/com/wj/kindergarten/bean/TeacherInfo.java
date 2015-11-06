package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * TeacherInfo
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-25 10:59
 */
public class TeacherInfo extends BaseModel{
    @Expose
    private UserInfo data;

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }
}
