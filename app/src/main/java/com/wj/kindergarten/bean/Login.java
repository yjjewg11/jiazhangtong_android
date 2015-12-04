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
    @Expose
    private List<Group> group_list;
    @Expose
    private List<Class> class_list;
    @Expose
    private String md5;

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

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

    public List<Group> getGroup_list() {
        return group_list;
    }

    public void setGroup_list(List<Group> group_list) {
        this.group_list = group_list;
    }

    public List<Class> getClass_list() {
        return class_list;
    }

    public void setClass_list(List<Class> class_list) {
        this.class_list = class_list;
    }
}
