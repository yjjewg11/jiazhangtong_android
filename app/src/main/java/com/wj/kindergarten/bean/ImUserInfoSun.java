package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tanghongbin on 16/4/11.
 */
public class ImUserInfoSun extends BaseModel {
    @Expose
    private String address;@Expose
    private String age;@Expose
    private String career;@Expose
    private String email;@Expose
    private String extra;@Expose
    private String gender;@Expose
    private String gmtModified;@Expose
    private String iconUrl;@Expose
    private String mobile;@Expose
    private String name;@Expose
    private String nick;@Expose
    private String password;@Expose
    private String qq;@Expose
    private String remark;@Expose
    private String status;@Expose
    private String taobaoid;@Expose
    private String userid;@Expose
    private String vip;@Expose
    private String wechat;@Expose
    private String weibo;

    @Override
    public String toString() {
        return "ImUserInfoSun{" +
                "address='" + address + '\'' +
                ", age='" + age + '\'' +
                ", career='" + career + '\'' +
                ", email='" + email + '\'' +
                ", extra='" + extra + '\'' +
                ", gender='" + gender + '\'' +
                ", gmtModified='" + gmtModified + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", mobile='" + mobile + '\'' +
                ", name='" + name + '\'' +
                ", nick='" + nick + '\'' +
                ", password='" + password + '\'' +
                ", qq='" + qq + '\'' +
                ", remark='" + remark + '\'' +
                ", status='" + status + '\'' +
                ", taobaoid='" + taobaoid + '\'' +
                ", userid='" + userid + '\'' +
                ", vip='" + vip + '\'' +
                ", wechat='" + wechat + '\'' +
                ", weibo='" + weibo + '\'' +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTaobaoid() {
        return taobaoid;
    }

    public void setTaobaoid(String taobaoid) {
        this.taobaoid = taobaoid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }
}
