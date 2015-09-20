package com.wj.kindergarten.ui.message;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.MsgDataModel;
import com.wj.kindergarten.utils.IntervalUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;

/**
 * MessageAdapter
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-17 10:31
 */
public class MessageAdapter extends BaseAdapter {
    private ArrayList<MsgDataModel> msgs;
    private Context context;

    public MessageAdapter(Context context, ArrayList<MsgDataModel> list) {
        this.msgs = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return msgs.size();
    }

    @Override
    public Object getItem(int position) {
        return msgs.get(position);
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
            convertView = View.inflate(context, R.layout.view_message_item, null);
            viewHolder.ivHead = (ImageView) convertView.findViewById(R.id.iv_picture);
            viewHolder.ivQuan = (ImageView) convertView.findViewById(R.id.iv_read);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.message_title);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.message_content);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.message_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MsgDataModel msg = msgs.get(position);
        if (null != msg) {
            if (msg.getIsread() == 0) {
                viewHolder.ivQuan.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ivQuan.setVisibility(View.GONE);
            }
            viewHolder.tvTitle.setText(Utils.getText(msg.getTitle()));
            viewHolder.tvContent.setText(Utils.getText(msg.getMessage()));
            viewHolder.tvDate.setText(IntervalUtil.getInterval(msg.getCreate_time()));
        }
        return convertView;
    }

    class ViewHolder {
        ImageView ivHead;
        ImageView ivQuan;
        TextView tvTitle;
        TextView tvContent;
        TextView tvSchool;
        TextView tvDate;
    }
}
