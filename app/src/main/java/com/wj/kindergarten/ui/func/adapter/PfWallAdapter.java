package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.compounets.NestedGridView;


import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/1/13.
 */
public class PfWallAdapter extends BaseAdapter {
    private List<List<AllPfAlbumSunObject>> lists = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;

    public PfWallAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setImageList(List<List<AllPfAlbumSunObject>> lists) {
        this.lists.clear();
        this.lists.addAll(lists);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return lists.size();
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
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.pf_classic_by_date_album, null);
            viewHolder.pf_tv_date_time = (TextView) convertView.findViewById(R.id.pf_tv_date_time);
            viewHolder.pf_pic_count = (TextView) convertView.findViewById(R.id.pf_pic_count);
//            viewHolder.pf_fusion_out_ll = (LinearLayout) convertView.findViewById(R.id.pf_fusion_out_ll);
//            viewHolder.pf_album_linearLayout_left = (LinearLayout) convertView.findViewById(R.id.pf_album_linearLayout_left);
//            viewHolder.pf_album_linearLayout_center = (LinearLayout) convertView.findViewById(R.id.pf_album_linearLayout_center);
//            viewHolder.pf_album_linearLayout_right = (LinearLayout) convertView.findViewById(R.id.pf_album_linearLayout_right);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.pf_album_linearLayout_left.setVisibility(View.VISIBLE);
        viewHolder.pf_album_linearLayout_center.setVisibility(View.VISIBLE);
        viewHolder.pf_album_linearLayout_right.setVisibility(View.VISIBLE);

        viewHolder.pf_album_linearLayout_left.removeAllViews();
        viewHolder.pf_album_linearLayout_center.removeAllViews();
        viewHolder.pf_album_linearLayout_right.removeAllViews();

        if (lists != null && lists.size() > 0 &&
                lists.get(position) != null && lists.get(position).size() > 0) {
            viewHolder.pf_pic_count.setText("共" + lists.get(position).size() + "张");
            viewHolder.pf_tv_date_time.setText("" +
                    TimeUtil.getYMDTimeFromYMDHMS(lists.get(position).get(0).getPhoto_time()));
            if (lists.get(position).size() == 1) {
                viewHolder.pf_album_linearLayout_center.setVisibility(View.GONE);
                viewHolder.pf_album_linearLayout_right.setVisibility(View.GONE);
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                viewHolder.pf_fusion_out_ll.addView(imageView, setLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                ImageLoaderUtil.displayMyImage(lists.get(position).get(0).getPath(), imageView);
            } else if (lists.get(position).size() > 1 && lists.get(position).size() <= 3) {
                viewHolder.pf_album_linearLayout_center.setVisibility(View.GONE);
                viewHolder.pf_album_linearLayout_right.setVisibility(View.GONE);
                for (AllPfAlbumSunObject object : lists.get(position)) {
                    ImageView imageView = new ImageView(context);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.weight = 1;
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    viewHolder.pf_fusion_out_ll.addView(imageView, params);
                    ImageLoaderUtil.displayMyImage(object.getPath(), imageView);
                }
            } else if (lists.get(position).size() > 3) {
                //给容器中添加3个垂直linearLayout用来添加图片
                int count = 0;
                for (AllPfAlbumSunObject object : lists.get(position)) {
                    if(count == 3){
                        count = 0;
                    }
                    count ++;
                    ImageView imageView = new ImageView(context);
                    ImageLoaderUtil.displayMyImage(object.getPath(),imageView);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    if(count == 0){
                        viewHolder.pf_album_linearLayout_left.addView(imageView, params);
                    }else if(count == 1){
                        viewHolder.pf_album_linearLayout_center.addView(imageView, params);
                    }else if(count == 2){
                        viewHolder.pf_album_linearLayout_right.addView(imageView, params);
                    }

                }


            }
        }

        return convertView;
    }

    class ViewHolder {
        TextView pf_tv_date_time, pf_pic_count;
        LinearLayout pf_fusion_out_ll, pf_album_linearLayout_left,
                pf_album_linearLayout_center, pf_album_linearLayout_right;
    }

    public ViewGroup.LayoutParams setLayoutParams(int width, int height) {
        return new ViewGroup.LayoutParams(width, height);
    }
}
