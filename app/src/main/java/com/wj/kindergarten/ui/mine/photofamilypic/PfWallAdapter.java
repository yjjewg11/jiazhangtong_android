package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.compounets.NestedGridView;
import com.wj.kindergarten.ui.func.adapter.PfNestedGridAdapter;
import com.wj.kindergarten.ui.imagescan.PhotoWallActivity;
import com.wj.kindergarten.ui.other.ScaleImageView;
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
            viewHolder.gridView = (NestedGridView) convertView.findViewById(R.id.pf_fusion_grid);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (lists != null && lists.size() > 0 &&
                lists.get(position) != null && lists.get(position).size() > 0) {
            viewHolder.pf_pic_count.setText("共" + lists.get(position).size()+"张");
            viewHolder.pf_tv_date_time.setText("" +
                    TimeUtil.getYMDTimeFromYMDHMS(lists.get(position).get(0).getPhoto_time()));
            PfNestedGridAdapter pfNestedGridAdapter =
                    new PfNestedGridAdapter(context,lists.get(position));
            viewHolder.gridView.setAdapter(pfNestedGridAdapter);
        }

        return convertView;
    }

    class ViewHolder {
        TextView pf_tv_date_time, pf_pic_count;
        NestedGridView gridView;
    }
}
