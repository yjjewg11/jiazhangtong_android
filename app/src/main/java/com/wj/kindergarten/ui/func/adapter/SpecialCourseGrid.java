package com.wj.kindergarten.ui.func.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.socialize.utils.Log;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.SpecialCourseType;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.WindowUtils;

import java.util.ArrayList;
import java.util.List;

public class SpecialCourseGrid extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private List<SpecialCourseType> list = new ArrayList<>();
    public SpecialCourseGrid(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<SpecialCourseType> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (list.size() == 0 ? 0 : list.size());
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
        if (convertView == null) {
           convertView = inflater.inflate(R.layout.item_special_grid,null);
            viewHolder = new ViewHolder();
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv_special_pic);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.tv_special_class_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SpecialCourseType specialCourseType = list.get(position);
        if(specialCourseType!=null){
            String http = specialCourseType.getImg();
            if(TextUtils.isEmpty(http)){
                viewHolder.iv.setImageResource(R.drawable.main_item);
            }else{
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.iv.getLayoutParams();
                params.width = (int) (45*WindowUtils.getDesnity());
                params.height =  (int) (45 * (WindowUtils.getDesnity()));
                viewHolder.iv.setLayoutParams(params);
                ImageLoaderUtil.displayMyImage(specialCourseType.getImg(), viewHolder.iv);
            }

            viewHolder.tv.setText("" + specialCourseType.getDatavalue());
        }else{
            viewHolder.iv.setImageResource(R.drawable.main_item);
            viewHolder.tv.setText("暂无数据");
        }

        return convertView;
    }

    class ViewHolder{
        ImageView iv;
        TextView tv;
    }


}
