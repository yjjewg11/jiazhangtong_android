package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

public class TrainReplyPage {
    @Expose
    private int pageSize;
    @Expose
    private int pageNo;
    @Expose
    private List<Reply> data;
    @Expose
    private int totalCount;

    public List<Reply> getData() {
        return data;
    }

    public void setData(List<Reply> data) {
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

    @Override
    public String toString() {
        return "TrainReplyPage{" +
                "data=" + data +
                ", pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                ", totalCount=" + totalCount +
                '}';
    }
}
