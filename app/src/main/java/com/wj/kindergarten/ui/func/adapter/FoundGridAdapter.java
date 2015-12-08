package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.FoundGridItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tangt on 2015/12/8.
 */
public class FoundGridAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private Context context;
    private List<FoundGridItem> list = Arrays.asList(new FoundGridItem[]{
            new FoundGridItem("精品文章",R.drawable.jingpinwenzhang_big,38),
            new FoundGridItem("话题",R.drawable.huati_big,12),
            new FoundGridItem("优惠活动",R.drawable.youhuihuodong_bg,69),
    });
    public FoundGridAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<FoundGridItem> list){
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
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.found_fragment_grid_item,null);
            viewHolder = new ViewHolder();
            viewHolder.iv_found_grid_item = (ImageView) convertView.findViewById(R.id.iv_found_grid_item);
            viewHolder.tv_found_grid_name = (TextView) convertView.findViewById(R.id.tv_found_grid_name);
            viewHolder.tv_found_grid_count = (TextView) convertView.findViewById(R.id.tv_found_grid_count);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FoundGridItem item = list.get(position);
        if(item != null){
            viewHolder.iv_found_grid_item.setImageResource(item.getIv());
            viewHolder.tv_found_grid_name.setText(item.getName());
            viewHolder.tv_found_grid_count.setText(item.getCount()+"");
        }else{

        }
        return convertView;
    }

    class ViewHolder{
        ImageView iv_found_grid_item;
        TextView tv_found_grid_name,tv_found_grid_count;
    }
}
