package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;
import com.wj.kindergarten.ui.BaseActivity;

import java.util.List;

/**
 * Created by tangt on 2016/2/29.
 */
public class BoutiqueAlbumList extends BaseModel{
    @Expose
    private int pageSize;@Expose
    private int pageNo;@Expose
    private List<BoutiqueAlbumListSun> data;

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

    public List<BoutiqueAlbumListSun> getData() {
        return data;
    }

    public void setData(List<BoutiqueAlbumListSun> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BoutiqueAlbumList{" +
                "pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                ", data=" + data +
                '}';
    }
}
