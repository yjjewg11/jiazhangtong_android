package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * LoginModel
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/23 22:14
 */
public class Login extends BaseModel {
    @Expose
    private UserInfo userinfo;
    @Expose
    private List<ChildInfo> list;

    public UserInfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserInfo userinfo) {
        this.userinfo = userinfo;
    }

    public List<ChildInfo> getList() {
        return list;
    }

    public void setList(List<ChildInfo> list) {
        this.list = list;
    }
}
