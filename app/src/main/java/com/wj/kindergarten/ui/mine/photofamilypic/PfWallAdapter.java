package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.ui.other.ScaleImageView;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/1/13.
 */
public class PfWallAdapter extends BaseAdapter {
    private List<AllPfAlbumSunObject> imageList = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;

    public PfWallAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setImageList(List<AllPfAlbumSunObject> imageList) {
        this.imageList.clear();
        this.imageList.addAll(imageList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScaleImageView scaleImageView = new ScaleImageView(context);
        AllPfAlbumSunObject object = imageList.get(position);
        if(object != null){
            ImageLoaderUtil.displayMyImage(object.getPath(),scaleImageView);
        }
        return scaleImageView;
    }
}
