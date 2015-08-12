package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * UserInfo
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/24 18:08
 */
public class UserInfo extends BaseModel {
    @Expose
    private String office;
    @Expose
    private String sex;
    @Expose
    private String last_login_time;
    @Expose
    private String tel;
    @Expose
    private String img;
    @Expose
    private int tel_verify;
    @Expose
    private int type;
    @Expose
    private String password;
    @Expose
    private String loginname;
    @Expose
    private String login_time;
    @Expose
    private String email;
    @Expose
    private String name;
    @Expose
    private String create_time;
    @Expose
    private int disable;
    @Expose
    private String uuid;


    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(String last_login_time) {
        this.last_login_time = last_login_time;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
