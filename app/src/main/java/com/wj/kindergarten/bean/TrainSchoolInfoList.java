package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

import java.util.List;

public class TrainSchoolInfoList extends BaseModel{
    @Expose
    private int totalCount;
    @Expose
    private int pageSize;
    @Expose
    private int pageNo;
    @Expose
    private List<TrainSchoolInfo> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<TrainSchoolInfo> getData() {
        return data;
    }

    public void setData(List<TrainSchoolInfo> data) {
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

    @Override
    public String toString() {
        return "TrainSchoolInfoList{" +
                "data=" + data +
                ", pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                '}';
    }
}
