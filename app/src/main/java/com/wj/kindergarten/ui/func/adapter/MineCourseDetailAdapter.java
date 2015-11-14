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
import com.wj.kindergarten.bean.MineAllCourse;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MineCourseDetailAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private List<MineAllCourse> list = new ArrayList<>();

    public MineCourseDetailAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    boolean isSelected;
    long state = 100;
    int positionSeclected = -1;
    public void setList(List<MineAllCourse> list) {
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
            viewHolder.tv_training_classname_content = (TextView) convertView.findViewById(R.id.tv_training_classname_content);
            viewHolder.tv_training_classtime = (TextView) convertView.findViewById(R.id.tv_training_classtime);
            viewHolder.tv_trainning_adress = (TextView) convertView.findViewById(R.id.tv_trainning_adress);
            viewHolder.tv_trainning_preparething = (TextView) convertView.findViewById(R.id.tv_trainning_preparething);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MineAllCourse mac = list.get(position);

        if(mac!=null){
            viewHolder.classSort.setText("第 "+(position+1)+" 课");
            viewHolder.classContent.setText(""+mac.getName());
            viewHolder.time.setText(""+ TimeUtil.getYMDTimeFromYMDHMS(mac.getPlandate()));
            viewHolder.tv_training_classname_content.setText(""+Utils.isNull(mac.getName()));
            viewHolder.tv_training_classtime.setText(""+Utils.isNull(mac.getPlandate()));
            viewHolder.tv_trainning_adress.setText(""+Utils.isNull(mac.getAddress()));
            viewHolder.tv_trainning_preparething .setText(""+Utils.isNull(mac.getReadyfor()));
        }

        //把得到的时间值化成毫秒做对比，小则显示状态1，等于接近显示橙色，大于显示蓝色。
        state =  TimeUtil.compareTime(new Date(), mac.getPlandate());

        if(state<0){
            viewHolder.pop_rl.setBackgroundResource(R.color.course_over);
        }else if(state==0){
            viewHolder.pop_rl.setBackgroundResource(R.color.white);
        }else if(state>0){
            if(!isSelected){
                viewHolder.pop_rl.setBackgroundResource(R.color.course_been_down);
                isSelected = true;
                positionSeclected = position;
            }

            if(positionSeclected != position){
                viewHolder.pop_rl.setBackgroundResource(R.color.course_not_do);
            }else{
                viewHolder.pop_rl.setBackgroundResource(R.color.course_been_down);
            }

        }

        viewHolder.pop_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.show_fl.getVisibility() == View.GONE){
                    viewHolder.show_fl.setVisibility(View.VISIBLE);
                    viewHolder.iv_right.setImageResource(R.drawable.shangjiantou2hui);

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

        TextView tv_training_classname_content,tv_training_classtime,
                tv_trainning_adress,tv_trainning_preparething;

    }
}
