package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.PfAlbumListSun;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/1/18.
 */
public class PfAlbumListAdapter extends BaseAdapter {
    private List<PfAlbumListSun> list = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public PfAlbumListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<PfAlbumListSun> list) {
        this.list.clear();
        this.list.addAll(list);
        this.list.add(new PfAlbumListSun("邀请新成员"));
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
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.pf_mine_album,null);
            viewHolder = new ViewHolder();
            viewHolder.pf_mine_image = (ImageView) convertView.findViewById(R.id.pf_mine_image);
            viewHolder.pf_mine_tv = (TextView) convertView.findViewById(R.id.pf_mine_tv);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        PfAlbumListSun sun = list.get(position);
        if(sun != null){
            viewHolder.pf_mine_tv.setText(""+sun.getTitle());
            ImageLoaderUtil.displayMyImage(sun.getHerald(),viewHolder.pf_mine_image);
        }
        return convertView;
    }

    class ViewHolder{
        ImageView pf_mine_image;
        TextView pf_mine_tv;
    }
}
