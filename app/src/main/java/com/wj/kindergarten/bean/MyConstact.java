package com.wj.kindergarten.bean;

/**
 * Created by tangt on 2016/3/21.
 */
public class MyConstact {
    String contantName;
    String pingying;
    String phoneNumber;

    @Override
    public String toString() {
        return "MyContact{" +
                "contantName='" + contantName + '\'' +
                ", pingying='" + pingying + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    public String getContantName() {
        return contantName;
    }

    public void setContantName(String contantName) {
        this.contantName = contantName;
    }

    public String getPingying() {
        return pingying;
    }

    public void setPingying(String pingying) {
        this.pingying = pingying;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
