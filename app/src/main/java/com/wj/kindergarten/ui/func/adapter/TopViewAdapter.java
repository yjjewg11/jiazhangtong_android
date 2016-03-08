package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.PfAlbumListSun;
import com.wj.kindergarten.utils.Utils;

import java.util.List;

/**
 * Created by tangt on 2016/3/7.
 */
public class TopViewAdapter extends BaseAdapter{
    private List<PfAlbumListSun> list;
    private Context context;
    int position;
    public TopViewAdapter(Context context,List<PfAlbumListSun> list,int position) {
        this.list = list;
        this.context = context;
        this.position = position;
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
        Holder holder;
        if(convertView == null){
            holder = new Holder();
            convertView = View.inflate(context, R.layout.top_view_title_item,null);
            holder.title = (TextView) convertView.findViewById(R.id.top_view_title);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        if(this.position == position){
            Drawable drawable = context.getResources().getDrawable(R.drawable.pf_item_choosed);
            drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
            holder.title.setCompoundDrawablePadding(4);
            holder.title.setCompoundDrawables(null,null,drawable,null);
        }else {
            Drawable zanWei = context.getResources().getDrawable(R.drawable.xiajiatou);
            zanWei.setBounds(0,0,zanWei.getMinimumWidth(),zanWei.getMinimumHeight());
            holder.title.setCompoundDrawablePadding(4);
            holder.title.setCompoundDrawables(null,null,zanWei,null);
        }
        PfAlbumListSun sun = list.get(position);
        if(sun != null){
            holder.title.setText(""+ Utils.isNull(sun.getTitle()));
        }else {
            holder.title.setText("家庭相册--"+(position+1));
        }
        return convertView;
    }

    class Holder{
        TextView title;
    }
}
