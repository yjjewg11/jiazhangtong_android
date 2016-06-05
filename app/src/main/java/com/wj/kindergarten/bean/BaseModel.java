package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * BaseModel
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/5/21 17:00
 */
public class BaseModel implements Serializable {
    @Expose
    private String JSESSIONID;
    @Expose
    private BaseResMsg ResMsg;


    public String getJSESSIONID() {
        return JSESSIONID;
    }

    public void setJSESSIONID(String JSESSIONID) {
        this.JSESSIONID = JSESSIONID;
    }

    public BaseResMsg getResMsg() {
        return ResMsg;
    }

    public void setResMsg(BaseResMsg resMsg) {
        ResMsg = resMsg;
    }
}
