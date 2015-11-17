package com.wj.kindergarten.ui.emot;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.Emot;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import java.util.ArrayList;

/**
 * 显示表情
 */
public class FaceAdapter extends BaseAdapter {

    private ArrayList<Emot> data;
    private LayoutInflater inflater;
    private ChooseFace chooseFace;

    public FaceAdapter(Context context, ArrayList<Emot> list, ChooseFace chooseFace) {
        this.inflater = LayoutInflater.from(context);
        this.data = list;
        this.chooseFace = chooseFace;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Emot emot = data.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.view_address_book_image_item, null);
            viewHolder.iv_face = (ImageView) convertView.findViewById(R.id.iv_img);
            viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.ll_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (null != emot) {
            ImageLoaderUtil.displayImage(emot.getDescription(), viewHolder.iv_face);
            viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseFace.choose(emot);
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        ImageView iv_face;
        LinearLayout layout;
    }
}