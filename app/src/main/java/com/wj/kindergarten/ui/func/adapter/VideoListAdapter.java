package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.SingleCameraInfo;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/5/31.
 */
public class VideoListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<SingleCameraInfo> list = new ArrayList<>();
    private String rel_uuid;

    public void setRel_uuid(String rel_uuid) {
        this.rel_uuid = rel_uuid;
    }

    public void setList(List<SingleCameraInfo> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public VideoListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
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
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.video_list_item,null);
            viewHolder.view_list_item_img = (ImageView) convertView.findViewById(R.id.view_list_item_img);
            viewHolder.view_list_item_line_status = (TextView) convertView.findViewById(R.id.view_list_item_line_status);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final SingleCameraInfo sun = list.get(position);
        if(sun != null){
            ImageLoaderUtil.displayImage(sun.getPicUrl(),viewHolder.view_list_item_img);
            viewHolder.view_list_item_line_status.setText((sun.isOnline() ? "在线" : "离线"));
        }
        return convertView;
    }

    class ViewHolder {
        ImageView view_list_item_img;
        TextView view_list_item_line_status;
    }
}
