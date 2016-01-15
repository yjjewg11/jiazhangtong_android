package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
    private List<AllPfAlbumSunObject> list;
    private Context context;
    private LayoutInflater inflater;

    public PfNestedGridAdapter(Context context, List<AllPfAlbumSunObject> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
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
        ScaleImageView imageView = new ScaleImageView(context);
        AllPfAlbumSunObject object = list.get(position);
        if(object != null){
            ImageLoaderUtil.displayMyImage(object.getPath(),imageView);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PfGalleryActivity.class);
                intent.putExtra("list",(ArrayList)list);
                context.startActivity(intent);
            }
        });
    return imageView;
    }

    class ViewHolder{
        ScaleImageView imageView;
    }
}
