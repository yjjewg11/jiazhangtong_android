package com.wj.kindergarten.ui.func.adapter;


import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.StudyStateObject;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

public class MineCourseStatusAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private List<StudyStateObject> list = new ArrayList<>();

    public MineCourseStatusAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<StudyStateObject> list){
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
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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

        StudyStateObject sso = list.get(position);
        if(sso!=null){
            viewHolder.trainSchoolName.setText("学校:"+sso.getGroup_name());
            viewHolder.student.setText("学生:"+sso.getStudent_name());
            viewHolder.schoolName.setText(""+sso.getCourse_title());
            viewHolder.className.setText("班级:"+sso.getClass_name());
            if(sso.getPlandate() != null){
                String text = "<font color='#ff4966'>"+"近期上课:"+sso.getPlandate()+"</font>";
                viewHolder.openTime.setText(Html.fromHtml(text));
            }else{
                viewHolder.openTime.setText("时间暂定");
            }
            ImageLoaderUtil.displayMyImage(sso.getLogo(),viewHolder.iv_school);
        }


        return convertView;
    }

    class ViewHolder{
        TextView trainSchoolName,student,className,schoolName,openTime;
        ImageView iv_school;
    }
}
