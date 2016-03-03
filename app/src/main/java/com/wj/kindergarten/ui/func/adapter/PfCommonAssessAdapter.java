package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.PfSingleAssessObject;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.ui.emot.EmotUtil;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.EmojiFilter;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.IntervalUtil;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/3/2.
 */
public class PfCommonAssessAdapter extends BaseAdapter {

    private View.OnClickListener bottomListener;

    public void setBottomListener(View.OnClickListener bottomListener) {
        this.bottomListener = bottomListener;
    }

    private Context context;
    List<PfSingleAssessObject> objectList = new ArrayList<>();

    public PfCommonAssessAdapter(Context context) {
        this.context = context;
    }

    public void setObjectList(List<PfSingleAssessObject> objectList) {
        this.objectList.clear();
        this.objectList.addAll(objectList);
        if(this.objectList.size() == 0){
            PfSingleAssessObject object = new PfSingleAssessObject();
            this.objectList.add(object);
        }
    }

    @Override
    public int getCount() {
        return objectList.size();
    }

    @Override
    public Object getItem(int position) {
        return objectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(context, R.layout.common_assess_item, null);
            holder.common_assess_item_rl = (RelativeLayout) convertView.findViewById(R.id.common_assess_item_rl);
            holder.common_assess_item_head_img = (CircleImage) convertView.findViewById(R.id.common_assess_item_head_img);
            holder.common_assess_item_name = (TextView) convertView.findViewById(R.id.common_assess_item_name);
            holder.common_assess_item_content = (TextView) convertView.findViewById(R.id.common_assess_item_content);
            holder.common_assess_item_time = (TextView) convertView.findViewById(R.id.common_assess_item_time);
            holder.common_assess_item_delete = (TextView) convertView.findViewById(R.id.common_assess_item_delete);
            holder.common_assess_item_write_assess = (TextView) convertView.findViewById(R.id.common_assess_item_write_assess);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.common_assess_item_write_assess.setVisibility(View.VISIBLE);
        holder.common_assess_item_write_assess.setOnClickListener(bottomListener);
        final PfSingleAssessObject object = objectList.get(position);
        if (object != null) {
            if (position == objectList.size() - 1) {
                holder.common_assess_item_write_assess.setVisibility(View.VISIBLE);
                holder.common_assess_item_write_assess.setOnClickListener(bottomListener);
            } else {
                holder.common_assess_item_write_assess.setVisibility(View.GONE);
            }
            if(TextUtils.isEmpty(object.getCreate_user())){
                holder.common_assess_item_write_assess.setVisibility(View.VISIBLE);
                holder.common_assess_item_rl.setVisibility(View.INVISIBLE);
            }else {
                holder.common_assess_item_rl.setVisibility(View.VISIBLE);
            ImageLoaderUtil.displayImage(object.getCreate_img(), holder.common_assess_item_head_img);
            holder.common_assess_item_name.setText("" + object.getCreate_user());
            holder.common_assess_item_content.setText(EmotUtil.getEmotionContent(context, object.getContent()));
            String time = object.getCreate_time();
            if (Utils.isNull(time) != null && !TextUtils.isEmpty(time)) {
                holder.common_assess_item_time.setText(IntervalUtil.getInterval(time));
            }
            holder.common_assess_item_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objectList.remove(object);
                    notifyDataSetChanged();
                }
            });
            }

        }
        return convertView;
    }

    class Holder {
        CircleImage common_assess_item_head_img;
        TextView common_assess_item_name;
        TextView common_assess_item_content;
        TextView common_assess_item_time;
        TextView common_assess_item_delete;
        TextView common_assess_item_write_assess;
        RelativeLayout common_assess_item_rl;
    }
}
