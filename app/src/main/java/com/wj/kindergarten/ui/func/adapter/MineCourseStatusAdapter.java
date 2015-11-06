package com.wj.kindergarten.ui.func.adapter;


import android.content.Context;
<<<<<<< HEAD
import android.text.Html;
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
<<<<<<< HEAD
import com.wj.kindergarten.bean.StudyStateObject;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0

public class MineCourseStatusAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
<<<<<<< HEAD
    private List<StudyStateObject> list = new ArrayList<>();
=======
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0

    public MineCourseStatusAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

<<<<<<< HEAD
    public void setList(List<StudyStateObject> list){
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
        return 3;
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
    }

    @Override
    public Object getItem(int position) {
<<<<<<< HEAD
        return list.get(position);
=======
        return null;
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
    }

    @Override
    public long getItemId(int position) {
<<<<<<< HEAD
        return position;
=======
        return 0;
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
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
<<<<<<< HEAD

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

=======
//        viewHolder.trainSchoolName.setText("");
//        viewHolder.student.setText("");
//        viewHolder.schoolName.setText("");
//        viewHolder.className.setText("");
//        viewHolder.openTime.setText("");
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0

        return convertView;
    }

    class ViewHolder{
        TextView trainSchoolName,student,className,schoolName,openTime;
        ImageView iv_school;
    }
}
