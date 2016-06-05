package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by tangt on 2016/3/2.
 */
public class SyncUploadPicList extends BaseModel{
    @Expose
    private int totalCount;@Expose
    private int pageSize;@Expose
    private int pageNo;@Expose
    private List<SyncUploadPicObj> data;

    @Override
    public String toString() {
        return "SyncUploadPicList{" +
                "totalCount=" + totalCount +
                ", pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                ", data=" + data +
                '}';
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public List<SyncUploadPicObj> getData() {
        return data;
    }

    public void setData(List<SyncUploadPicObj> data) {
        this.data = data;
    }
}
