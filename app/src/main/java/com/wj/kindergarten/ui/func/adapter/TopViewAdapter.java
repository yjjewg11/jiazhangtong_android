package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
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

    public TopViewAdapter(Context context,List<PfAlbumListSun> list) {
        this.list = list;
        this.context = context;
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
        PfAlbumListSun sun = list.get(position);
        if(sun == null){
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
