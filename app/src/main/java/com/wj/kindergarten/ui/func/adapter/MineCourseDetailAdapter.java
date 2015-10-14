package com.wj.kindergarten.ui.func.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;

public class MineCourseDetailAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    public MineCourseDetailAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return 5;
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
        final ViewHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.mine_course_detail_item,null);
            viewHolder = new ViewHolder();
            viewHolder.show_fl = (FrameLayout)convertView.findViewById(R.id.show_more_fl);
            viewHolder.pop_rl = (RelativeLayout)convertView.findViewById(R.id.pop_more_class_info);
            viewHolder.classSort = (TextView) convertView.findViewById(R.id.mine_item_class_sort);
            viewHolder.classContent = (TextView) convertView.findViewById(R.id.mine_item_content);
            viewHolder.time = (TextView) convertView.findViewById(R.id.mine_item_time);
            viewHolder.iv_right = (ImageView) convertView.findViewById(R.id.mine_item_iv_right);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        viewHolder.classSort.setText("");
//        viewHolder.classContent.setText("");
//        viewHolder.time.setText("");
//        viewHolder.iv_right.setText("");
        viewHolder.pop_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.show_fl.getVisibility() == View.GONE){
                    viewHolder.show_fl.setVisibility(View.VISIBLE);
                    viewHolder.iv_right.setImageResource(R.drawable.shangjiantou);
                }else{
                    viewHolder.iv_right.setImageResource(R.drawable.youjiantou);
                    viewHolder.show_fl.setVisibility(View.GONE);
                }

            }
        });
        return convertView;
    }

    class ViewHolder{
        RelativeLayout pop_rl;
        FrameLayout show_fl;
        TextView classSort,classContent,time;
        ImageView iv_right;
    }
}
