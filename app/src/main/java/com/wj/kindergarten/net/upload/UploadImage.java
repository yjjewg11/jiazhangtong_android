package com.wj.kindergarten.net.upload;

/**
 * Created by zoupengqiang on 14-5-19.
 * 上传图片接口
 */
public interface UploadImage {
    void success(Result result);

    void failure(String message);
}
