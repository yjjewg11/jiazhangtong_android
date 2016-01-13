package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by tangt on 2016/1/13.
 */
public class AllPfAlbumSun extends BaseModel{
    @Expose
    private int pageSize;@Expose
    private int pageNo;@Expose
    private List<AllPfAlbumSunObject> data;

    @Override
    public String toString() {
        return "AllPfAlbumSun{" +
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

    public List<AllPfAlbumSunObject> getData() {
        return data;
    }

    public void setData(List<AllPfAlbumSunObject> data) {
        this.data = data;
    }
}
