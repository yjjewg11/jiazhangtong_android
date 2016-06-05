package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2015/12/17.
 */
public class AddChildSun extends BaseModel{
    @Expose
    private String uuid;@Expose
    private String name;@Expose
    private String nickname;@Expose
    private String sex;@Expose
    private String headimg;@Expose
    private String birthday;@Expose
    private String ma_tel;@Expose
    private String ba_tel;@Expose
    private String nai_tel;@Expose
    private String ye_tel;@Expose
    private String waipo_tel;@Expose
    private String waigong_tel;@Expose
    private String other_tel;@Expose
    private String groupuuid;@Expose
    private String classuuid;@Expose
    private String ma_name;@Expose
    private String ba_name;@Expose
    private String address;@Expose
    private String note;@Expose
    private String ma_work;@Expose
    private String ba_work;@Expose
    private String idcard;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getMa_tel() {
        return ma_tel;
    }

    public void setMa_tel(String ma_tel) {
        this.ma_tel = ma_tel;
    }

    public String getBa_tel() {
        return ba_tel;
    }

    public void setBa_tel(String ba_tel) {
        this.ba_tel = ba_tel;
    }

    public String getNai_tel() {
        return nai_tel;
    }

    public void setNai_tel(String nai_tel) {
        this.nai_tel = nai_tel;
    }

    public String getYe_tel() {
        return ye_tel;
    }

    public void setYe_tel(String ye_tel) {
        this.ye_tel = ye_tel;
    }

    public String getWaipo_tel() {
        return waipo_tel;
    }

    public void setWaipo_tel(String waipo_tel) {
        this.waipo_tel = waipo_tel;
    }

    public String getWaigong_tel() {
        return waigong_tel;
    }

    public void setWaigong_tel(String waigong_tel) {
        this.waigong_tel = waigong_tel;
    }

    public String getOther_tel() {
        return other_tel;
    }

    public void setOther_tel(String other_tel) {
        this.other_tel = other_tel;
    }

    public String getGroupuuid() {
        return groupuuid;
    }

    public void setGroupuuid(String groupuuid) {
        this.groupuuid = groupuuid;
    }

    public String getClassuuid() {
        return classuuid;
    }

    public void setClassuuid(String classuuid) {
        this.classuuid = classuuid;
    }

    public String getMa_name() {
        return ma_name;
    }

    public void setMa_name(String ma_name) {
        this.ma_name = ma_name;
    }

    public String getBa_name() {
        return ba_name;
    }

    public void setBa_name(String ba_name) {
        this.ba_name = ba_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMa_work() {
        return ma_work;
    }

    public void setMa_work(String ma_work) {
        this.ma_work = ma_work;
    }

    public String getBa_work() {
        return ba_work;
    }

    public void setBa_work(String ba_work) {
        this.ba_work = ba_work;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    @Override
    public String toString() {
        return "AddChildSun{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", headimg='" + headimg + '\'' +
                ", birthday='" + birthday + '\'' +
                ", ma_tel='" + ma_tel + '\'' +
                ", ba_tel='" + ba_tel + '\'' +
                ", nai_tel='" + nai_tel + '\'' +
                ", ye_tel='" + ye_tel + '\'' +
                ", waipo_tel='" + waipo_tel + '\'' +
                ", waigong_tel='" + waigong_tel + '\'' +
                ", other_tel='" + other_tel + '\'' +
                ", groupuuid='" + groupuuid + '\'' +
                ", classuuid='" + classuuid + '\'' +
                ", ma_name='" + ma_name + '\'' +
                ", ba_name='" + ba_name + '\'' +
                ", address='" + address + '\'' +
                ", note='" + note + '\'' +
                ", ma_work='" + ma_work + '\'' +
                ", ba_work='" + ba_work + '\'' +
                ", idcard='" + idcard + '\'' +
                '}';
    }
}
