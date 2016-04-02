package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.AlreadySavePath;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import java.util.List;

/**
 * Created by tangt on 2016/1/18.
 */
public class PfPopPicAdapter extends BaseAdapter {
    private List<AllPfAlbumSunObject> list;
    private Context context;
    private LayoutInflater inflater;
    private int choosePosition = -1;

    public void setChoosePosition(int choosePosition) {
        this.choosePosition = choosePosition;
    }

    public PfPopPicAdapter(Context context, List<AllPfAlbumSunObject> list) {
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
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.collect_image,null);
             viewHolder.pic = (ImageView) convertView.findViewById(R.id.collect_item_image);
            viewHolder.choose = (ImageView) convertView.findViewById(R.id.common_pf_xuanzhong);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        viewHolder.choose.setVisibility(View.GONE);
        ImageLoaderUtil.displayMyImage(list.get(position).getPath(),viewHolder.pic);
        if(choosePosition == position){
            viewHolder.choose.setVisibility(View.VISIBLE);
        }


        return convertView;
    }

    class ViewHolder{
        ImageView pic;
        ImageView choose;
    }
}
