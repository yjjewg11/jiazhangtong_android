package com.wj.kindergarten.ui.func.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;

public class MineCourseStatusAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;

    public MineCourseStatusAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return 3;
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

        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.course_status_item,null);
            viewHolder = new ViewHolder();
            viewHolder.trainSchoolName = (TextView)convertView.findViewById(R.id.course_status_train_school_name);
            viewHolder.student = (TextView)convertView.findViewById(R.id.course_status_student);
            viewHolder.className = (TextView)convertView.findViewById(R.id.course_status_class);
            viewHolder.schoolName = (TextView)convertView.findViewById(R.id.course_status_education);
            viewHolder.openTime = (TextView)convertView.findViewById(R.id.course_status_open_time);
            viewHolder.iv_school = (ImageView)convertView.findViewById(R.id.course_status_iv);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        viewHolder.trainSchoolName.setText("");
//        viewHolder.student.setText("");
//        viewHolder.schoolName.setText("");
//        viewHolder.className.setText("");
//        viewHolder.openTime.setText("");

        return convertView;
    }

    class ViewHolder{
        TextView trainSchoolName,student,className,schoolName,openTime;
        ImageView iv_school;
    }
}
