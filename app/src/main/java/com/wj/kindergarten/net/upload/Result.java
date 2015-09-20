package com.wj.kindergarten.net.upload;

import com.google.gson.annotations.Expose;
import com.wj.kindergarten.bean.BaseModel;

/**
 * Result
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-25 16:35
 */
public class Result extends BaseModel {
    @Expose
    private Image data;
    @Expose
    private String imgUrl;

    public Image getData() {
        return data;
    }

    public void setData(Image data) {
        this.data = data;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
