package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;
import com.videogo.openapi.bean.EZCameraInfo;

import java.util.List;

/**
 * Created by tangt on 2016/5/31.
 */
public class VideoListSun extends BaseModel{
    @Expose
    private int pageSize;@Expose
    private int pageNo;@Expose
    private List<SingleCameraInfo> data;

    public List<SingleCameraInfo> getData() {
        return data;
    }

    public void setData(List<SingleCameraInfo> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "VideoListSun{" +
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

}
