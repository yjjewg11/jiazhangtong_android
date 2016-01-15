package com.wj.kindergarten.ui.imagescan;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

/**
 * Created by tangt on 2016/1/15.
 */
public class AutoDownLoadListener implements ImageLoadingListener {
    private Context context;

    public AutoDownLoadListener(Context context) {
        this.context = context;
    }

    @Override
    public void onLoadingStarted(String imageUri, View view) {
        ToastUtils.showMessage("图片保存失败");
    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        String fileName = System.currentTimeMillis() + ".jpg";
        Utils.saveImageToGallery(fileName, context, loadedImage);
    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {

    }
}
