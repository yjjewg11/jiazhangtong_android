package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

public class ReplySun extends Reply{
    @Expose
    private int status;

    @Override
    public String toString() {
        return "ReplySun{" +
                "status=" + status +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
