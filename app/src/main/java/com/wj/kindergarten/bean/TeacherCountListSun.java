package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

public class TeacherCountListSun extends BaseModel{
    @Expose
    private int pageSize;@Expose
    private int pageNo;@Expose
    private List<TeacherCount> data;

    @Override
    public String toString() {
        return "TeacherCountListSun{" +
                "data=" + data +
                ", pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                '}';
    }

    public List<TeacherCount> getData() {
        return data;
    }

    public void setData(List<TeacherCount> data) {
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
