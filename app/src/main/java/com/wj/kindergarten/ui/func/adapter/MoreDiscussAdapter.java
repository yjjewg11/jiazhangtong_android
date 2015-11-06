package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.MoreDiscuss;
<<<<<<< HEAD
import com.wj.kindergarten.ui.other.RatingBarView;
import com.wj.kindergarten.utils.TimeUtil;
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0

import java.util.ArrayList;
import java.util.List;

public class MoreDiscussAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private List<MoreDiscuss> list = new ArrayList<>();

    public void setList(List<MoreDiscuss> list){
<<<<<<< HEAD
        this.list.clear();
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
        this.list.addAll(list);
        notifyDataSetChanged();
    }
    public MoreDiscussAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.more_discuss_item,null);
            viewHolder = new ViewHolder();
<<<<<<< HEAD
            viewHolder.ratingBarView = (RatingBarView)convertView.findViewById(R.id.item_special_course_list_view__rating_bar);
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
            viewHolder.more_discuss_content = (TextView) convertView.findViewById(R.id.more_discuss_content);
            viewHolder.more_discuss_mobile_number = (TextView) convertView.findViewById(R.id.more_discuss_mobile_number);
            viewHolder.more_discuss_time = (TextView) convertView.findViewById(R.id.more_discuss_time);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MoreDiscuss md = list.get(position);
        if(md != null){
<<<<<<< HEAD
            viewHolder.ratingBarView.setFloatStar(md.getScore(),true);
            viewHolder.more_discuss_content.setText(""+md.getContent());
            viewHolder.more_discuss_mobile_number.setText(""+md.getCreate_user());
            viewHolder.more_discuss_time.setText(""+ TimeUtil.getYMDTimeFromYMDHMS(md.getCreate_time()));
=======
            viewHolder.more_discuss_content.setText(""+md.getContent());
            viewHolder.more_discuss_mobile_number.setText(""+md.getScore());
            viewHolder.more_discuss_time.setText(""+md.getCreate_time());
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
        }else{

        }
        return convertView;
    }

    class ViewHolder{
        TextView more_discuss_mobile_number,more_discuss_time,more_discuss_content;
<<<<<<< HEAD
        RatingBarView ratingBarView;
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
    }
}
