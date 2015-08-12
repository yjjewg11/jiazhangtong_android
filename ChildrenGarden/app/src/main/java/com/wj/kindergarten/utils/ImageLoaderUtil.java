package com.wj.kindergarten.utils;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

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
    private static ImageLoader imageLoader = null;


    private static void initImageLoader(Context context, int loadingResource, int emptyResource, int failResource, String cachePath, int diskCacheSize, int roundeSize) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(
                context.getApplicationContext(), cachePath);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .diskCacheSize(diskCacheSize * 1024 * 1024).build();

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(loadingResource)
                .showImageForEmptyUri(emptyResource)
                .showImageOnFail(failResource).cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(roundeSize)).build();

        imageLoader = ImageLoader.getInstance();

        imageLoader.init(config);
    }

    public static void initImageLoader(Context context, int loadingResource, String cachePath, int diskCacheSize, int roundeSize) {
        initImageLoader(context, loadingResource, loadingResource,
                loadingResource, cachePath, diskCacheSize, roundeSize);
    }

    public static void displayImage(String url, ImageView imageView) {
        imageLoader.displayImage(url, imageView,options);
    }

    public static void displayImage(String url, ImageView imageView, DisplayImageOptions displayImageOptions) {
        if (displayImageOptions == null) {
            return;
        }
        imageLoader.displayImage(url, imageView, displayImageOptions);
    }

    public static void downLoadImageLoader(String url, ImageLoadingListener imageLoadingListener) {
        imageLoader.loadImage(url, imageLoadingListener);
    }
}
