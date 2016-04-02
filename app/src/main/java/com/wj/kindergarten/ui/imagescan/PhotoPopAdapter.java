package com.wj.kindergarten.ui.imagescan;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * PhotoPopAdapter
 *
 * @author weiwu.song
 * @data: 2015/1/4 17:06
 * @version: v1.0
 */
public class PhotoPopAdapter extends BaseAdapter {
    private List<PhotoDirModel> dataList;
    private GalleryImagesActivity galleryImagesActivity = null;

    public PhotoPopAdapter(GalleryImagesActivity galleryImagesActivity, List<PhotoDirModel> dataList) {
        this.dataList = dataList;
        this.galleryImagesActivity = galleryImagesActivity;
    }

    @Override
    public int getCount() {
        return dataList != null ? dataList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return ((dataList != null) && dataList.size() > position) ? dataList.get(position) : null;
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
            convertView = View.inflate(parent.getContext(), R.layout.imagescan_photo_pop_item, null);
            holder.image = (ImageView) convertView.findViewById(R.id.photo_pop_item_image);
            holder.text = (TextView) convertView.findViewById(R.id.photo_pop_item_name);
            holder.sub = (TextView) convertView.findViewById(R.id.photo_pop_item_sub);
            holder.indicator = (ImageView) convertView.findViewById(R.id.photo_pop_item_indicator);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        PhotoDirModel item = dataList.get(position);
        String dirName = item.getDirName();
        ArrayList<String> paths = item.getPaths();
        if (paths != null) {
            holder.sub.setText(paths.size() + "张");
        } else {
            holder.sub.setText("0张");
        }
        holder.text.setText(dirName);
        if (parent.getContext().getString(R.string.all_photo).equals(dirName)) {
            if (paths != null && paths.size() > 1) {
                ImageLoaderUtil.displayImage("file://" + paths.get(1), holder.image);
            }
        } else {
            if (paths != null && paths.size() > 0) {
                ImageLoaderUtil.displayImage("file://" + paths.get(0), holder.image);
            }
        }
        if (galleryImagesActivity != null && !Utils.stringIsNull(dirName)) {
            String showDir = galleryImagesActivity.getShowWitchDir();
            if (dirName.equals(showDir)) {
                holder.indicator.setVisibility(View.VISIBLE);
            } else {
                holder.indicator.setVisibility(View.GONE);
            }
        } else {
            holder.indicator.setVisibility(View.GONE);
        }
        return convertView;
    }

    class Holder {
        ImageView image;
        TextView text;
        TextView sub;
        ImageView indicator;
    }
}
