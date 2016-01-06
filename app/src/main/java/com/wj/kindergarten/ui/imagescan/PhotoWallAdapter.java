package com.wj.kindergarten.ui.imagescan;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.webview.ChooseTwoCode;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;


/**
 * PhotoWallAdapter
 *
 * @author weiwu.song
 * @data: 2014/12/30 14:14
 * @version: v1.0
 */
public class PhotoWallAdapter extends PagerAdapter {
    private Context context;



    private ArrayList<String> photoList = new ArrayList<String>();

    public PhotoWallAdapter(ArrayList<String> photoArrayList,Context context) {
        this.photoList = photoArrayList;
        this.context = context;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);//删除页卡
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
        final PhotoView photoView = new PhotoView(container.getContext());
        String uri = photoList.get(position);
        if (!Utils.stringIsNull(uri)) {
            if (!uri.contains("http:")) {
                uri = "file://" + uri;
            } else {
                if (!(Utils.stringIsNull(uri)) && uri.contains("@")) {
                    uri = uri.substring(0, uri.indexOf("@"));
                }
            }
        }
        CGLog.d("photo wall->" + uri);
        //增加二维码识别
        final String finalUri = uri;
        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ChooseTwoCode chooseTwoCode = new ChooseTwoCode(context, finalUri);
                chooseTwoCode.tv_save.setVisibility(View.GONE);
                chooseTwoCode.choose(v);
                return false;
            }
        });
        photoView.setImageResource(R.drawable.friends_sends_pictures_no);
        ImageLoaderUtil.downLoadImageLoader(uri, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                photoView.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return photoView;
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
}
