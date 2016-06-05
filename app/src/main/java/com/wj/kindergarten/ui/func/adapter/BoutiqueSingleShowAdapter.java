package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/3/1.
 */
public class BoutiqueSingleShowAdapter extends BaseAdapter{
    private List<AllPfAlbumSunObject> list = new ArrayList<>();
    private Context context;

    public BoutiqueSingleShowAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<AllPfAlbumSunObject> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
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
        Holder holder;
        if(convertView == null){
            holder = new Holder();
            convertView = View.inflate(context, R.layout.boutique_single_pic,null);
            holder.boutiqe_single_pic = (ImageView) convertView.findViewById(R.id.boutiqe_single_pic);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        if(list.get(position) != null){
            String path = list.get(position).getPath();
            ImageLoaderUtil.displayImage(path,holder.boutiqe_single_pic);
        }
        return convertView;
    }
    class Holder{
        ImageView boutiqe_single_pic;
    }
}
