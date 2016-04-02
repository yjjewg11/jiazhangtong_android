package com.wj.kindergarten.ui.imagescan;

import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wj.kindergarten.utils.ImageLoaderUtil;

/**
 * Created by tangt on 2016/2/25.
 */
public class BitmapCallBack {
    public static void loadBitmap(String url, final GetBitmapCallback getBitmapCallback){
        ImageLoaderUtil.downLoadImageLoader(url, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                          getBitmapCallback.callback(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    public interface GetBitmapCallback{
        void callback(Bitmap bitmap);
    }
}
