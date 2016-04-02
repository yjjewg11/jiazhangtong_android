package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

import java.util.List;

public class MineAllCourseListSun extends BaseModel{
    @Expose
    private int pageSize;@Expose
    private int pageNo;@Expose
    private List<MineAllCourse> data;

    @Override
    public String toString() {
        return "MineAllCourseListSun{" +
                "data=" + data +
                ", pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                '}';
    }

    public List<MineAllCourse> getData() {
        return data;
    }

    public void setData(List<MineAllCourse> data) {
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
}
