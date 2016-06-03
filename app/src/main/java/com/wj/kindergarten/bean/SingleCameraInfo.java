package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by tangt on 2016/5/31.
 */
public class SingleCameraInfo extends BaseModel{
    @Expose
    private String cameraId;@Expose
    private String cameraName;@Expose
    private String channelNo;@Expose
    private String deviceSerial;@Expose
    private boolean isEncrypt;@Expose
    private boolean isShared;@Expose
    private String picUrl;@Expose
    private boolean isOnline;

    @Override
    public String toString() {
        return "SingleCameraInfo{" +
                "cameraId='" + cameraId + '\'' +
                ", cameraName='" + cameraName + '\'' +
                ", channelNo='" + channelNo + '\'' +
                ", deviceSerial='" + deviceSerial + '\'' +
                ", isEncrypt=" + isEncrypt +
                ", isShared=" + isShared +
                ", picUrl='" + picUrl + '\'' +
                ", isOnline=" + isOnline +
                '}';
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }

    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public boolean isEncrypt() {
        return isEncrypt;
    }

    public void setEncrypt(boolean encrypt) {
        isEncrypt = encrypt;
    }

    public boolean isShared() {
        return isShared;
    }

    public void setShared(boolean shared) {
        isShared = shared;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
