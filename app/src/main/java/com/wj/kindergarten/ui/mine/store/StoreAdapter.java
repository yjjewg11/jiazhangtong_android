package com.wj.kindergarten.ui.mine.store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.MsgDataModel;
import com.wj.kindergarten.bean.Store;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.IntervalUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;

/**
 * MessageAdapter
 *
 * @Description:收藏adapter
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-17 10:31
 */
public class StoreAdapter extends BaseAdapter {
    private ArrayList<Store> list;
    private Context context;

    public StoreAdapter(Context context, ArrayList<Store> list) {
        this.list = list;
        this.context = context;
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.view_store_item, null);
            viewHolder.head = (ImageView) convertView.findViewById(R.id.image_head);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_1);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_2);
            viewHolder.date = (TextView) convertView.findViewById(R.id.tv_3);
            viewHolder.content = (TextView) convertView.findViewById(R.id.tv_4);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Store store = list.get(position);
        if (null != store) {
            if (store.getType() == 3) {
                viewHolder.textView.setVisibility(View.VISIBLE);
            } else {
                ImageLoaderUtil.displayImage(store.getShow_img(), viewHolder.head);
                viewHolder.textView.setVisibility(View.GONE);
            }
            viewHolder.title.setText(Utils.getText(store.getShow_name()));
            viewHolder.date.setText(IntervalUtil.getInterval(store.getCreatetime()));
            viewHolder.content.setText(Utils.getText(store.getTitle()));
        }
        return convertView;
    }

    class ViewHolder {
        ImageView head;
        TextView textView;
        TextView title;
        TextView date;
        TextView content;
    }
}
