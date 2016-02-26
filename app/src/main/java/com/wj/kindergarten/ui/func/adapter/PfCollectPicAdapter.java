package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.ui.mine.photofamilypic.ConllectPicActivity;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import java.util.List;

/**
 * Created by tangt on 2016/1/18.
 */
public class PfCollectPicAdapter extends BaseAdapter {
    private List<AllPfAlbumSunObject> list;
    private Context context;
    private LayoutInflater inflater;

    public PfCollectPicAdapter(Context context, List<AllPfAlbumSunObject> list) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(context, R.layout.collect_image,null);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.collect_item_image);
            convertView.setTag(imageView);
        }
        AllPfAlbumSunObject sunObject = list.get(position);
        if(sunObject != null){
            ImageView imageView = (ImageView) convertView.getTag();
            ImageLoaderUtil.displayMyImage(sunObject.getPath(),imageView);
        }else {

        }


        return convertView;
    }
}
