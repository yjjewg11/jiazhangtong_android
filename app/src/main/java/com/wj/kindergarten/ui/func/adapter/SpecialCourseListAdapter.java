package com.wj.kindergarten.ui.func.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.SpecialCourseInfoObject;
import com.wj.kindergarten.ui.other.RatingBarView;

import java.util.ArrayList;
import java.util.List;

public class SpecialCourseListAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private List<SpecialCourseInfoObject> list = new ArrayList<>();
    public SpecialCourseListAdapter(Context context) {
        this.context = context;
        inflater =  LayoutInflater.from(context);
    }

    public List<SpecialCourseInfoObject> getSpecialCourseInfoObjectList(){
        return this.list;
    }

    public void setSepcialList(List<SpecialCourseInfoObject> list){
        //清空数据
        this.list.clear();
        if(list!=null&&list.size()>0) {
            this.list.addAll(list);
            notifyDataSetChanged();
        }
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
            convertView = inflater.inflate(R.layout.item_special_course_list_view,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_special_course_list_view_image_view);
            viewHolder.ratingBar = (RatingBarView) convertView.findViewById(R.id.item_special_course_list_view__rating_bar);
            viewHolder.item_special_course_list_view_tv_adresss = (TextView) convertView.findViewById(R.id.item_special_course_list_view_tv_adresss);
            viewHolder.item_class_name = (TextView) convertView.findViewById(R.id.item_class_name);
            viewHolder.item_special_course_list_view_tv_distance = (TextView) convertView.findViewById(R.id.item_special_course_list_view_tv_distance);
            viewHolder.item_special_course_list_view_tv_edcucation = (TextView) convertView.findViewById(R.id.item_special_course_list_view_tv_edcucation);
            viewHolder.item_special_course_list_view_tv_study_people = (TextView) convertView.findViewById(R.id.item_special_course_list_view_tv_study_people);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SpecialCourseInfoObject object = list.get(position);
        if (object != null) {
            viewHolder.ratingBar.setStar(3);
            viewHolder.imageView.setImageResource(R.drawable.ic_launcher);
            viewHolder.item_class_name.setText("" + object.getTitle());
            viewHolder.item_special_course_list_view_tv_adresss.setText(""+object.getSubtype());
            //TODO
            viewHolder.item_special_course_list_view_tv_distance.setText("设置距离：130");
            viewHolder.item_special_course_list_view_tv_edcucation.setText("设置教育中心");
            viewHolder.item_special_course_list_view_tv_study_people.setText("学习人数");
        }

        return convertView;
    }

    class ViewHolder{

        TextView item_special_course_list_view_tv_distance,item_class_name,
                item_special_course_list_view_tv_edcucation,
                item_special_course_list_view_tv_adresss,
                item_special_course_list_view_tv_study_people;

        ImageView imageView;

        RatingBarView ratingBar;
    }
}
