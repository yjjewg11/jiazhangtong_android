package com.wj.kindergarten.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedVignetteBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.wenjie.jiazhangtong.R;

import java.io.File;

/**
 * ImageLoaderUtil
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/5/20 12:45
 */
public class ImageLoaderUtil {
    private static DisplayImageOptions options = null;
    public static ImageLoader imageLoader = null;
    private static DisplayImageOptions myOptions;
    private static DisplayImageOptions albumOptions;
    private static DisplayImageOptions zhanWeiOptions;


    private static void initImageLoader(Context context, int loadingResource, int emptyResource, int failResource, String cachePath, int diskCacheSize, int roundeSize) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context.getApplicationContext(), "CG/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(3)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .diskCacheSize(50 * 1024 * 1024)
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheFileCount(200)
                .build();

        ImageLoader.getInstance().init(config);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(loadingResource)
                .showImageForEmptyUri(emptyResource)
                .showImageOnFail(failResource)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(roundeSize))
                        .build();

        int drawable = R.drawable.zhanweitu;

        myOptions = new DisplayImageOptions.Builder()
                         .showImageForEmptyUri(drawable)
                         .showImageOnFail(drawable)
                         .showImageOnLoading(drawable)
                         .cacheInMemory(true)
//                         .bitmapConfig(Bitmap.Config.)
                         .cacheOnDisk(true)
                         .build();
//
        albumOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(loadingResource)
                .showImageForEmptyUri(emptyResource)
                .showImageOnFail(failResource)
                .cacheInMemory(true)
//                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheOnDisk(true)
//                .displayer(new RoundedVignetteBitmapDisplayer(roundeSize,1))
                .build();

        zhanWeiOptions  = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.zhanweitu)
                .showImageForEmptyUri(R.drawable.zhanweitu)
                .showImageOnFail(R.drawable.zhanweitu)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisk(true)
//                .displayer(new RoundedBitmapDisplayer(roundeSize))
                .build();


        imageLoader = ImageLoader.getInstance();
    }
    public static void displayZhanWeiChange(String path,ImageView imageView){
        imageLoader.displayImage(path,imageView,zhanWeiOptions);
    }


    public static void displayAlbumImage(String path,ImageView imageView){
        imageLoader.displayImage(path,imageView,albumOptions);
    }


    public static void displayMyImage(String uri,ImageView imageView){
        imageLoader.displayImage(uri,imageView,myOptions);
    }

    public static void initImageLoader(Context context, int loadingResource, String cachePath, int diskCacheSize, int roundeSize) {
        initImageLoader(context, loadingResource, loadingResource,
                loadingResource, cachePath, diskCacheSize, roundeSize);
    }

    public static void displayImage(String url, ImageView imageView) {
        imageLoader.displayImage(url, imageView, options);
    }

    public static void displayImage(String url, ImageView imageView, DisplayImageOptions displayImageOptions,
                                    ImageLoadingListener imageLoadingListener) {
        if (displayImageOptions == null) {
            return;
        }
        imageLoader.displayImage(url, imageView, displayImageOptions, imageLoadingListener);
    }

    public static void downLoadImageLoader(String url, ImageLoadingListener imageLoadingListener) {
        imageLoader.loadImage(url, imageLoadingListener);
    }


    public static Bitmap getImageFromCache(String imageUri) {
        Bitmap bitmap = imageLoader.getMemoryCache().get(imageUri);
        if (null != bitmap) {
            return bitmap;
        } else {
            String path = imageLoader.getDiskCache().get(imageUri).getPath();
            bitmap = BitmapFactory.decodeFile(path);
            return bitmap;
        }
    }
}
