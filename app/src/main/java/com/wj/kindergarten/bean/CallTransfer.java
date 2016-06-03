package com.wj.kindergarten.bean;

/**
 * Created by Administrator on 2015/11/5.
 */
public class CallTransfer extends BaseModel{
    private String ext_uuid;
    private int type;

    public CallTransfer(String ext_uuid, int type) {
        this.ext_uuid = ext_uuid;
        this.type = type;
    }

    public String getExt_uuid() {
        return ext_uuid;
    }

    public void setExt_uuid(String ext_uuid) {
        this.ext_uuid = ext_uuid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
