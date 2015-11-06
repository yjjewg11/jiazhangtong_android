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
import com.wj.kindergarten.bean.TrainSchoolInfo;
import com.wj.kindergarten.ui.other.RatingBarView;
<<<<<<< HEAD
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
=======

>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
import java.util.List;

public class SchoolCourseAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private Context context;
<<<<<<< HEAD
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

=======
    private List<TrainSchoolInfo> list;
    public SchoolCourseAdapter(Context context,List<TrainSchoolInfo> list){
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
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
<<<<<<< HEAD
            ImageLoaderUtil.displayMyImage(info.getImg(), viewHolder.imageView);
            viewHolder.ratingBar.setFloatStar(info.getCt_stars(), true);
            viewHolder.item_class_name.setText("" + info.getBrand_name());
            viewHolder.item_special_course_list_view_tv_adresss.setText("" + info.getAddress());
            viewHolder.item_special_course_list_view_tv_distance.setText(""+info.getDistance());
            viewHolder.item_special_course_list_view_tv_edcucation.setText("" + Utils.isNull(info.getSummary()));
            String text = "<font color='#ff4966'>"+info.getCt_study_students()+"</font>"+"人感兴趣";
            viewHolder.item_special_course_list_view_tv_study_people.setText(Html.fromHtml(text));
        }else{
=======
            viewHolder.imageView.setImageResource(R.drawable.ic_launcher);
            viewHolder.ratingBar.setStarCount(4);
            viewHolder.item_class_name.setText("" + info.getBrand_name());
//            viewHolder.item_special_course_list_view_tv_adresss.setText("" + info.getCreatetime());
//            viewHolder.item_special_course_list_view_tv_distance.setText("" + info.getCreatetime());
            viewHolder.item_special_course_list_view_tv_edcucation.setText("" + info.getLink_tel());
            viewHolder.item_special_course_list_view_tv_study_people.setText(""+info.getUuid());
        }else{
            viewHolder.imageView.setImageResource(R.drawable.load2);
            viewHolder.ratingBar.setStarCount(3);
            viewHolder.item_class_name.setText("固定班级");
            viewHolder.item_special_course_list_view_tv_adresss.setText("固定地址");
            viewHolder.item_special_course_list_view_tv_distance.setText("固定距离");
            viewHolder.item_special_course_list_view_tv_edcucation.setText("固定教育中心");
            viewHolder.item_special_course_list_view_tv_study_people.setText("固定人数");
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
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
