package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * BaseResMsg
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/24 17:50
 */
public class BaseResMsg extends BaseModel {
    @Expose
    String message;
    @Expose
    String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
