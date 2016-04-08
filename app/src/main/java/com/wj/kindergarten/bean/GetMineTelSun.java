package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tanghongbin on 16/4/7.
 */
public class GetMineTelSun extends BaseModel{

    /**
     * count : 1265
     * last_login_time : 2016-04-07 14:27:02
     * nickname : null
     * tel : 13628037996
     * img : null
     * tel_verify : 0
     * loginname : 13628037996
     * login_time : 2016-04-07 15:13:28
     * email : null
     * name : 学生125妈妈
     * create_time : 2015-07-18 21:08:28
     * disable : 0
     * realname : null
     * uuid : 211ee9bf-1645-4797-a122-60e75462dfdc
     */

    @Expose
    private int count;@Expose
    private String last_login_time;@Expose
    private Object nickname;@Expose
    private String tel;@Expose
    private String img;@Expose
    private int tel_verify;@Expose
    private String loginname;@Expose
    private String login_time;@Expose
    private String email;@Expose
    private String name;@Expose
    private String create_time;@Expose
    private int disable;@Expose
    private String realname;@Expose
    private String uuid;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(String last_login_time) {
        this.last_login_time = last_login_time;
    }

    public Object getNickname() {
        return nickname;
    }

    public void setNickname(Object nickname) {
        this.nickname = nickname;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getTel_verify() {
        return tel_verify;
    }

    public void setTel_verify(int tel_verify) {
        this.tel_verify = tel_verify;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getLogin_time() {
        return login_time;
    }

    public void setLogin_time(String login_time) {
        this.login_time = login_time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getDisable() {
        return disable;
    }

    public void setDisable(int disable) {
        this.disable = disable;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
