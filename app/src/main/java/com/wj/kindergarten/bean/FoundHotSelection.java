package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by tangt on 2015/12/9.
 */
public class FoundHotSelection extends BaseModel {
    @Expose
    private int pageSize;
    @Expose
    private int totalCount;
    @Expose
    private int pageNo;
    @Expose
    private List<FoundHotSelectionSun> data;



    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }



    public int getPageSize() {
        return pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getPageNo() {
        return pageNo;
    }

    public List<FoundHotSelectionSun> getData() {
        return data;
    }

    public void setData(List<FoundHotSelectionSun> data) {
        this.data = data;
    }
}
