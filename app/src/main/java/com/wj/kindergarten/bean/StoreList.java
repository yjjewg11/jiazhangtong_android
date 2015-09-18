package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

/**
 * StoreList
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-18 11:32
 */
public class StoreList extends BaseModel{
    @Expose
    private StoreData list;
    @Expose
    private int pageSize;
    @Expose
    private int totalCount;
    @Expose
    private int pageNo;

    public StoreData getList() {
        return list;
    }

    public void setList(StoreData list) {
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
