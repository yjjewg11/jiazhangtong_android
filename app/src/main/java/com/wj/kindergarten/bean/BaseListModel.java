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
public class BaseListModel implements Serializable {
    @Expose
    private int pageSize;
    @Expose
    private int totalCount;
    @Expose
    private int pageNo;

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
