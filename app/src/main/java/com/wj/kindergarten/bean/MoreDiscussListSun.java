package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

import java.util.List;

public class MoreDiscussListSun extends BaseModel{
    @Expose
    private int pageSize;
    @Expose
    private int pageNo;
    @Expose
    private int totalCount;
    @Expose
    private List<MoreDiscuss> data;

    @Override
    public String toString() {
        return "MoreDiscussListSun{" +
                "data=" + data +
                ", pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                ", totalCount=" + totalCount +
                '}';
    }

    public List<MoreDiscuss> getData() {
        return data;
    }

    public void setData(List<MoreDiscuss> data) {
        this.data = data;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
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
}
