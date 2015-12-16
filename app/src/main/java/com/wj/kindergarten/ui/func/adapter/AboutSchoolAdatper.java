package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.TrainSchoolInfo;
import com.wj.kindergarten.ui.other.RatingBarView;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2015/11/27.
 */
public class AboutSchoolAdatper extends BaseAdapter {
    private List<TrainSchoolInfo> list  = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public AboutSchoolAdatper(Context context) {
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
        //todo
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            viewHolder.ll_school_medal_one = (LinearLayout)convertView.findViewById(R.id.contain_include_medal);
            viewHolder.item_class_name = (TextView) convertView.findViewById(R.id.item_class_name);
            viewHolder.item_special_course_list_view_tv_adresss = (TextView) convertView.findViewById(R.id.item_special_course_list_view_tv_adresss);
            viewHolder.item_special_course_list_view_tv_distance = (TextView) convertView.findViewById(R.id.recruit_student_tv_distance);
            viewHolder.item_special_course_list_view__rating_bar = (RatingBarView)convertView.findViewById(R.id.item_special_course_list_view__rating_bar);
            viewHolder.item_special_course_list_view_image_view = (ImageView)convertView.findViewById(R.id.item_special_course_list_view_image_view);
            viewHolder.tv_study_people = (TextView)convertView.findViewById(R.id.tv_study_people);
            viewHolder.iv_madle = (ImageView)convertView.findViewById(R.id.iv_madle);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
          convertView.setBackgroundColor(context.getResources().getColor(R.color.special_gray));

        TrainSchoolInfo schoolInfo = list.get(position);

        if(schoolInfo != null){

            if(TextUtils.isEmpty(schoolInfo.getDistance())){
                viewHolder.item_special_course_list_view_tv_distance.setVisibility(View.INVISIBLE);
            }else{
                viewHolder.item_special_course_list_view_tv_distance.setVisibility(View.VISIBLE);
            }
            ImageLoaderUtil.displayMyImage(schoolInfo.getImg(), viewHolder.item_special_course_list_view_image_view);
            viewHolder.item_class_name.setText(schoolInfo.getBrand_name());
            viewHolder.item_special_course_list_view_tv_adresss.setText(schoolInfo.getAddress());
            viewHolder.item_special_course_list_view_tv_distance.setText(schoolInfo.getDistance());
            viewHolder.item_special_course_list_view__rating_bar.setFloatStar(schoolInfo.getCt_stars(), true);
            String text ;
            if(schoolInfo.getCt_study_students() != 0){
//                String text = "<font  color='#ff4966'>"+object.getCt_study_students()+"</font>"+"人已学";
                text = "<font  color='#ff4966'>"+schoolInfo.getCt_study_students()+"人就读"+"</font>";

            }else{
                text = "<font  color='#ff4966'>"+"0"+"</font>";
            }
            viewHolder.tv_study_people.setText(Html.fromHtml(text));



            if(viewHolder.ll_school_medal_one.getChildCount() <= 0 ) {
                if(schoolInfo.getSummary() != null && schoolInfo.getSummary().split(",") != null){
                    viewHolder.iv_madle.setVisibility(View.VISIBLE);
                    viewHolder.ll_school_medal_one.setVisibility(View.VISIBLE);
                    int size =  schoolInfo.getSummary().split(",").length;
                    if(size > 0){
                        for(int i = 0;i < (size > 3 ? 3 : size);i++){
                            TextView textView = new TextView(context);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                            textView.setText(schoolInfo.getSummary().split(",")[i]);
                            textView.setTextSize(11);
                            textView.setTextColor(context.getResources().getColor(R.color.text_green));
                            viewHolder.ll_school_medal_one.addView(textView,params);
                        }
                    }
                }else{
                    viewHolder.iv_madle.setVisibility(View.GONE);
                    viewHolder.ll_school_medal_one.setVisibility(View.GONE);
                }


            }
        }





        return convertView;
    }

    class ViewHolder{

         LinearLayout ll_school_medal_one;
         ImageView item_special_course_list_view_image_view,iv_madle;
         RatingBarView item_special_course_list_view__rating_bar;

         TextView item_class_name,item_special_course_list_view_tv_adresss,
                 item_special_course_list_view_tv_distance,tv_study_people;

    }
}
