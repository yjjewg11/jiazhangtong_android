package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.TeacherCount;
import com.wj.kindergarten.ui.other.RatingBarView;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;


public class TeacherSpecialAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    public List<TeacherCount> list = new ArrayList<>();
    public TeacherSpecialAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<TeacherCount> list){
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
               ViewHolder viewHolder ;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.teacher_item,null);
            viewHolder = new ViewHolder();
            viewHolder.head = (ImageView)convertView.findViewById(R.id.teacher_item_head_img);
            viewHolder.tv_teacher = (TextView)convertView.findViewById(R.id.teacher_item_teacher_name);
            viewHolder.tv_teach_class = (TextView)convertView.findViewById(R.id.teacher_item_class_name);
            viewHolder.tv_introduce = (TextView)convertView.findViewById(R.id.teacher_item_introduce);
            viewHolder.rav = (RatingBarView)convertView.findViewById(R.id.item_special_course_list_view__rating_bar);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

            TeacherCount tc = list.get(position);
            if(tc!=null){
            ImageLoaderUtil.displayMyImage(tc.getImg(),viewHolder.head);
            viewHolder.tv_teacher.setText(""+tc.getName());
            viewHolder.tv_teach_class.setText("教授课程 ："+tc.getCourse_title());
            viewHolder.tv_introduce.setText(""+tc.getSummary());
            viewHolder.rav.setFloatStar(tc.getCt_stars(),true);
        }


        return convertView;
    }

    class ViewHolder{
        ImageView head;
        TextView tv_teacher,tv_teach_class,tv_introduce;
        RatingBarView rav;
    }
}
