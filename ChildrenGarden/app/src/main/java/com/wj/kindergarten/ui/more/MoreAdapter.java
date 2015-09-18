package com.wj.kindergarten.ui.more;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.More;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;

/**
 * MoreAdapter
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-17 16:43
 */
public class MoreAdapter extends BaseAdapter {
    private ArrayList<More> list;
    private Context context;

    public MoreAdapter(Context context, ArrayList<More> list) {
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
            convertView = View.inflate(context, R.layout.view_more_item, null);
            viewHolder.ivHead = (ImageView) convertView.findViewById(R.id.more_img);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.more_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        More more = list.get(position);
        if (null != more) {
            viewHolder.tvTitle.setText(Utils.getText(more.getName()));
            ImageLoaderUtil.displayImage(more.getIconUrl(),viewHolder.ivHead);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView ivHead;
        TextView tvTitle;
    }
}
