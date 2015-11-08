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
import com.wj.kindergarten.bean.TrainSchoolInfo;
import com.wj.kindergarten.ui.other.RatingBarView;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;

import java.util.List;

public class SchoolCourseAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private Context context;
    private List<TrainSchoolInfo> list = new ArrayList<>();
    public SchoolCourseAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<TrainSchoolInfo> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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

        TrainSchoolInfo info = list.get(position);

        if (info != null){
            ImageLoaderUtil.displayMyImage(info.getImg(), viewHolder.imageView);
            viewHolder.ratingBar.setFloatStar(info.getCt_stars(), true);
            viewHolder.item_class_name.setText("" + info.getBrand_name());
            viewHolder.item_special_course_list_view_tv_adresss.setText("" + info.getAddress());
            viewHolder.item_special_course_list_view_tv_distance.setText(""+info.getDistance());
            viewHolder.item_special_course_list_view_tv_edcucation.setText("" + Utils.isNull(info.getSummary()));
            String text = "<font color='#ff4966'>"+info.getCt_study_students()+"</font>"+"人感兴趣";
            viewHolder.item_special_course_list_view_tv_study_people.setText(Html.fromHtml(text));
        }else{
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
