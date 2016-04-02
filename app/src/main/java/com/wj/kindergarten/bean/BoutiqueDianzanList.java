package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by tangt on 2016/3/10.
 */
public class BoutiqueDianzanList extends BaseModel{
    @Expose
    private int pageSize;@Expose
    private int pageNo;@Expose
    private List<BoutiqueDianzanListObj> data;

    @Override
    public String toString() {
        return "BoutiqueDianzanList{" +
                "pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                ", data=" + data +
                '}';
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

    public List<BoutiqueDianzanListObj> getData() {
        return data;
    }

    public void setData(List<BoutiqueDianzanListObj> data) {
        this.data = data;
    }
}
