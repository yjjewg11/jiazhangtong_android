package com.wj.kindergarten.ui.webview;

import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wj.kindergarten.utils.ImageLoaderUtil;

public abstract class DownListeners implements View.OnClickListener {
    private String imgUrl;

    public DownListeners(String imgurl) {
        this.imgUrl = imgurl;
    }

    public abstract void doOwnThing(String imageUri, Bitmap loadedImage);

    @Override
    public void onClick(View v) {
        downPic();
    }

    private void downPic() {
        ImageLoaderUtil.downLoadImageLoader(imgUrl,
                new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//
                        doOwnThing(imageUri, loadedImage);

//
//                                                ToastUtils.showMessage("图片保存成功");
//                                                MainActivity.instance.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File("/sdcard/Boohee/image.png"))));


                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }

                });
    }
}
