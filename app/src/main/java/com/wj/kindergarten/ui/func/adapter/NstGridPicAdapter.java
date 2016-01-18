package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.ui.imagescan.PhotoWallActivity;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class NstGridPicAdapter extends BaseAdapter {
    private List<String> mUrlList;
    private Context mContext;

    public NstGridPicAdapter(List<String> mUrlList, Context mContext) {
        super();
        this.mUrlList = mUrlList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mUrlList.size();
    }

    @Override
    public Object getItem(int position) {
        return mUrlList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_nest_grid, null);
            viewHolder.mPicIv = (ImageView) convertView.findViewById(R.id.iv_pic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mPicIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.carouselPic(mContext,position, (ArrayList<String>) mUrlList);
            }
        });

        String iconUrl = mUrlList.get(position);
        if (iconUrl != null) {
            ImageLoaderUtil.displayImage(iconUrl, viewHolder.mPicIv, CGApplication.options, null);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView mPicIv;
    }

}