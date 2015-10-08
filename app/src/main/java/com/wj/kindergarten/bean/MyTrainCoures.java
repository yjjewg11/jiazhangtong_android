package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

import java.util.List;

public class MyTrainCoures {
    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    @Expose
    private int totalCount;
    @Expose
    private int pageSize;
    @Expose
    private int pageNo;
    @Expose
    private List<TrainCourseContent> data;

    public List<TrainCourseContent> getData() {
        return data;
    }

    public void setDate(List<TrainCourseContent> data) {
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
        return "MyTrainCoures{" +
                "data=" + data +
                ", totalCount=" + totalCount +
                ", pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                '}';
    }
}
