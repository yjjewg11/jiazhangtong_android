package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;

/**
 * Created by tangt on 2016/3/7.
 */
public class FusionChooseItemAdapter extends BaseAdapter {
    private int position;
    private Context context;
    public FusionChooseItemAdapter(Context context, int position) {
        this.position = position;
        this.context = context;
    }


    @Override
    public int getCount() {
        return 2;
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
        Holder holder;
        if(convertView == null){
            holder = new Holder();
            convertView = View.inflate(context, R.layout.top_view_title_item,null);
            holder.tab = (TextView) convertView.findViewById(R.id.top_view_title);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        if(this.position == position){
            Drawable drawable = context.getResources().getDrawable(R.drawable.pf_item_choosed);
            drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
            holder.tab.setCompoundDrawablePadding(4);
            holder.tab.setCompoundDrawables(null,null,drawable,null);
        }else {
            Drawable zanWei = context.getResources().getDrawable(R.drawable.xiajiatou);
            zanWei.setBounds(0,0,zanWei.getMinimumWidth(),zanWei.getMinimumHeight());
            holder.tab.setCompoundDrawablePadding(4);
            holder.tab.setCompoundDrawables(null,null,zanWei,null);
        }
        if(position == 0){
            holder.tab.setText("浏览照片");
        }else if (position == 1){
            holder.tab.setText("浏览文件夹");
        }
        return convertView;
    }

    class Holder{
        TextView tab;
    }
}
