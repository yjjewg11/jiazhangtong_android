package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

import java.util.List;

public class SpecialCourseInfo extends BaseModel{

    @Expose
    private int pageSize;
    @Expose
    private int pageNo;
    @Expose
    private List<SpecialCourseInfoObject> data;


    public List<SpecialCourseInfoObject> getData() {
        return data;
    }

    public void setData(List<SpecialCourseInfoObject> data) {
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
        return "SpecialCourseInfo{" +
                "data='" + data + '\'' +
                ", pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                '}';
    }
}
