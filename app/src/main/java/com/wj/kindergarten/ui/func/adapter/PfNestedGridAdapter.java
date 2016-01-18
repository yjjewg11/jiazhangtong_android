package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbum;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.ui.imagescan.PhotoWallActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.PfGalleryActivity;
import com.wj.kindergarten.ui.other.ScaleImageView;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/1/14.
 */
public class PfNestedGridAdapter extends BaseAdapter {
    private List<AllPfAlbumSunObject> list = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public PfNestedGridAdapter(Context context, List<AllPfAlbumSunObject> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list.clear();
        this.list.addAll(list);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = new DynamicHeightImageView(context);
            convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        final DynamicHeightImageView imageView = (DynamicHeightImageView) convertView;
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        final AbsListView.LayoutParams params  = (AbsListView.LayoutParams) imageView.getLayoutParams();

        AllPfAlbumSunObject object = list.get(position);
        if(object != null){
            ImageLoaderUtil.downLoadImageLoader(object.getPath(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    double scale = params.width / loadedImage.getWidth();
                    params.height = (int) (loadedImage.getHeight()*scale);
                    imageView.setLayoutParams(params);
                    imageView.setImageBitmap(loadedImage);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PfGalleryActivity.class);
                intent.putExtra("list",(ArrayList)list);
                context.startActivity(intent);
            }
        });
    return convertView;
    }

    class ViewHolder{
        ScaleImageView imageView;
    }
}
