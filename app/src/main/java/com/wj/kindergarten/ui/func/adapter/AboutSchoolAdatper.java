package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.other.RatingBarView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2015/11/27.
 */
public class AboutSchoolAdatper extends BaseAdapter {
    private List<String> list  = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public AboutSchoolAdatper(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<String> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        //todo
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
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.about_school_item,null);
            viewHolder = new ViewHolder();
            viewHolder.ll_school_medal_one = (LinearLayout)convertView.findViewById(R.id.ll_school_medal_one);
            viewHolder.item_class_name = (TextView) convertView.findViewById(R.id.item_class_name);
            viewHolder.item_special_course_list_view_tv_adresss = (TextView) convertView.findViewById(R.id.item_special_course_list_view_tv_adresss);
            viewHolder.item_special_course_list_view_tv_distance = (TextView) convertView.findViewById(R.id.item_special_course_list_view_tv_distance);
            viewHolder.item_special_course_list_view__rating_bar = (RatingBarView)convertView.findViewById(R.id.item_special_course_list_view__rating_bar);
            viewHolder.item_special_course_list_view_image_view = (ImageView)convertView.findViewById(R.id.item_special_course_list_view_image_view);
            viewHolder.iv_madle = (ImageView)convertView.findViewById(R.id.iv_madle);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
          convertView.setBackgroundColor(context.getResources().getColor(R.color.special_gray));
        for(int i = 0;i<3;i++){
            TextView textView = new TextView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            textView.setText("优秀学校!!!"+(i+1));
            textView.setTextSize(11);
            viewHolder.ll_school_medal_one.addView(textView,params);
        }


        return convertView;
    }

    class ViewHolder{

         LinearLayout ll_school_medal_one;
         ImageView item_special_course_list_view_image_view,iv_madle;
         RatingBarView item_special_course_list_view__rating_bar;

         TextView item_class_name,item_special_course_list_view_tv_adresss,
                 item_special_course_list_view_tv_distance;

    }
}
