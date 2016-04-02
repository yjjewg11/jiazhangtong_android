package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by tangt on 2016/3/9.
 */
public class PfInfoDianZan extends BaseModel{
    @Expose
   private int pageSize;@Expose
   private int pageNo;@Expose
   private List<PfInfoDianZanObj> data;

    @Override
    public String toString() {
        return "PfInfoDianZan{" +
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

    public List<PfInfoDianZanObj> getData() {
        return data;
    }

    public void setData(List<PfInfoDianZanObj> data) {
        this.data = data;
    }
}
