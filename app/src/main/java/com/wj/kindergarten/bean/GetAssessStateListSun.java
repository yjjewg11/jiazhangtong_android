package com.wj.kindergarten.bean;


import com.google.gson.annotations.Expose;

import java.util.List;

public class GetAssessStateListSun extends BaseModel{

    @Expose
    private int pageSize;@Expose
    private int pageNo;@Expose
    private List<GetAssessState> data;

    public List<GetAssessState> getData() {
        return data;
    }

    public void setData(List<GetAssessState> data) {
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
