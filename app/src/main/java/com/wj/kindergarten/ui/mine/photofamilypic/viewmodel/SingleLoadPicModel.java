package com.wj.kindergarten.ui.mine.photofamilypic.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.ui.imagescan.NativeImageLoader;
import com.wj.kindergarten.utils.FileUtil;
import com.wj.kindergarten.utils.FinalUtil;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.WindowUtils;

import net.tsz.afinal.FinalDb;

import java.io.File;

/**
 * Created by tangt on 2016/3/23.
 */
public class SingleLoadPicModel {
    private FinalDb dbObj;
    public SingleLoadPicModel(Context context) {
        dbObj = FinalUtil.getFamilyUuidObjectDb(context);
    }

    public void getBitmap(String lishipath, AllPfAlbumSunObject object, final ImageView imageView, LoadSuccessed loadSuccessed){
        //先通过路径判断内存有无bitmap
        //内存没有，则判断uuid是否为空，为空，则网络获取，不为空，则找到数据库对应的字段，拿出
        //它的path路径，生成bitmap对象，加入内存中,如果本地没有存储这张照片的地址，则从网络获取。
        //内存中存放照片的键用网络地址
        Bitmap bitmap =  NativeImageLoader.getInstance().getBitmapFromMemCache(lishipath);
        if(bitmap != null){
            loadSuccessed.loadSuccess();
            showBitmap(bitmap, imageView);
            return;
        }
        //内存中没有图片,看obj对象缓存的md5图片地址是否存在本地
        String localPath = object.getMd5();
        if(localPath != null && !TextUtils.isEmpty(localPath)){
            //先判断文件是否存在，存在则本地加载
            if(FileUtil.checkFileExits(localPath)){
               bitmap =  NativeImageLoader.getInstance().loadNativeImage(localPath, new NativeImageLoader.NativeImageCallBack() {
                    @Override
                    public void onImageLoader(Bitmap bitsmap, String path) {
                        showBitmap(bitsmap,imageView);
                        return;
                    }
                });
                if(bitmap != null){
                    showBitmap(bitmap,imageView);
                }
                loadSuccessed.loadSuccess();
                return;
            }
        }

        loadBitmap(lishipath,imageView,object.getMd5(),loadSuccessed);


    }

    private float convertFloat(int number) {
        return Float.valueOf(number);
    }

    private void showBitmap(Bitmap loadedImage,ImageView pf_gallery_image) {
        float height = (convertFloat(WindowUtils.dm.widthPixels) / convertFloat(loadedImage.getWidth())) * convertFloat(loadedImage.getHeight());
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) pf_gallery_image.getLayoutParams();
        params.height = (int) height;
        pf_gallery_image.setLayoutParams(params);
        pf_gallery_image.requestLayout();
        pf_gallery_image.setImageBitmap(loadedImage);
    }

    private void loadBitmap(final String path, final ImageView imageView, final String cachePath, final LoadSuccessed loadSuccessed) {
        ImageLoaderUtil.downLoadImageLoader(path, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                loadSuccessed.loadSuccess();
                ToastUtils.showMessage("加载失败!");
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                String tag = imageView.getTag().toString();
                if (tag != null && !tag.equals(path)) return;
                showBitmap(loadedImage,imageView);
                NativeImageLoader.getInstance().addBitmapToMemoryCache(path, loadedImage);
                loadSuccessed.loadSuccess();
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                loadSuccessed.loadSuccess();
            }
        });
    }
    public interface LoadSuccessed{
        void loadSuccess();
    }
}
