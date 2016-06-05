package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * Msg
 *
 * @Description:消息 第三个
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-17 10:44
 */
public class Msg extends BaseModel {
    @Expose
    private MsgData list;
    @Expose
    private int pageSize;
    @Expose
    private int totalCount;
    @Expose
    private int pageNo;

    public MsgData getList() {
        return list;
    }

    public void setList(MsgData list) {
        this.list = list;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}
