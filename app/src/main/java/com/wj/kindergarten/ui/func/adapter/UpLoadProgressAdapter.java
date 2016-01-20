package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/1/20.
 */
public class UpLoadProgressAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> list = new ArrayList<>();

    public void changeDataSource(List<String> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public boolean isEmpty(){
        return list.size() == 0;
    }

    public UpLoadProgressAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
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
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.upload_progress_item,null);
            viewHolder = new ViewHolder();
            viewHolder.up_load_progress_image = (ImageView) convertView.findViewById(R.id.up_load_progress_image);
            viewHolder.up_load_progressBar = (ProgressBar) convertView.findViewById(R.id.up_load_progressBar);
            viewHolder.up_Load_wait = (ImageView) convertView.findViewById(R.id.up_Load_wait);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageLoaderUtil.displayMyImage(list.get(position),viewHolder.up_load_progress_image);
        return convertView;
    }

    class ViewHolder {
        ImageView up_load_progress_image,up_Load_wait;
        ProgressBar up_load_progressBar;
    }
}
