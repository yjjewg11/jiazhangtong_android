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
<<<<<<< HEAD
import com.wj.kindergarten.bean.MineAllCourse;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0

public class MineCourseDetailAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
<<<<<<< HEAD
    private List<MineAllCourse> list = new ArrayList<>();
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
    public MineCourseDetailAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
<<<<<<< HEAD

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
=======
    @Override
    public int getCount() {
        return 5;
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
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
<<<<<<< HEAD
            viewHolder.tv_training_classname_content = (TextView) convertView.findViewById(R.id.tv_training_classname_content);
            viewHolder.tv_training_classtime = (TextView) convertView.findViewById(R.id.tv_training_classtime);
            viewHolder.tv_trainning_adress = (TextView) convertView.findViewById(R.id.tv_trainning_adress);
            viewHolder.tv_trainning_preparething = (TextView) convertView.findViewById(R.id.tv_trainning_preparething);

=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

<<<<<<< HEAD
        MineAllCourse mac = list.get(position);

        if(mac!=null){
            viewHolder.classSort.setText("第 "+(position+1)+" 课");
            viewHolder.classContent.setText(""+mac.getName());
            viewHolder.time.setText(""+ TimeUtil.getYMDTimeFromYMDHMS(mac.getPlandate()));
            viewHolder.tv_training_classname_content.setText(""+mac.getName());
            viewHolder.tv_training_classtime.setText(""+mac.getPlandate());
            viewHolder.tv_trainning_adress.setText(""+mac.getAddress());
            viewHolder.tv_trainning_preparething .setText(""+mac.getReadyfor());
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
=======
//        viewHolder.classSort.setText("");
//        viewHolder.classContent.setText("");
//        viewHolder.time.setText("");
//        viewHolder.iv_right.setText("");
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
        viewHolder.pop_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.show_fl.getVisibility() == View.GONE){
                    viewHolder.show_fl.setVisibility(View.VISIBLE);
<<<<<<< HEAD
                    viewHolder.iv_right.setImageResource(R.drawable.shangjiantou2hui);
=======
                    viewHolder.iv_right.setImageResource(R.drawable.shangjiantou);
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
                }else{
                    viewHolder.iv_right.setImageResource(R.drawable.youjiantou);
                    viewHolder.show_fl.setVisibility(View.GONE);
                }

            }
        });
        return convertView;
    }

<<<<<<< HEAD


=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
    class ViewHolder{
        RelativeLayout pop_rl;
        FrameLayout show_fl;
        TextView classSort,classContent,time;
        ImageView iv_right;
<<<<<<< HEAD
        TextView tv_training_classname_content,tv_training_classtime,
                tv_trainning_adress,tv_trainning_preparething;
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
    }
}
