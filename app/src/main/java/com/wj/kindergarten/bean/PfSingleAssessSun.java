package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by tangt on 2016/2/23.
 */
public class PfSingleAssessSun extends BaseModel{

    @Expose
    private int pageSize;@Expose
    private int pageNo;@Expose
    private int totalCount;@Expose
    private List<PfSingleAssessObject> data;

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setData(List<PfSingleAssessObject> data) {
        this.data = data;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public List<PfSingleAssessObject> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "PfSingleAssessSun{" +
                "pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                ", totalCount=" + totalCount +
                ", data=" + data +
                '}';
    }
}
