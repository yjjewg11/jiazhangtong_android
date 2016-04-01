package com.wj.kindergarten.ui.func.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.QueryGroupCount;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.imagescan.PhotoDirModel;
import com.wj.kindergarten.ui.mine.photofamilypic.BoutiqueGalleryActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.PfUpGalleryActivity;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.List;


/**
 * PhotoPopAdapter
 *
 * @author weiwu.song
 * @data: 2015/1/4 17:06
 * @version: v1.0
 */
public class BoutiquePopAdapter extends BaseAdapter {
    private List<QueryGroupCount> dataList;
    private FinalDb db;

    public BoutiquePopAdapter(BaseActivity baseActivity, List<QueryGroupCount> dataList) {
        this.dataList = dataList;
        db = FinalDb.create(baseActivity, GloablUtils.FAMILY_UUID_OBJECT);
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
            convertView = View.inflate(parent.getContext(), R.layout.imagescan_upload_pf, null);
            holder.time = (TextView) convertView.findViewById(R.id.imagescan_upload_time);
            holder.count = (TextView) convertView.findViewById(R.id.imagescan_upload_count);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imagescna_upload_image);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        QueryGroupCount queryGroupCount = dataList.get(position);
        if(queryGroupCount != null){
            holder.time.setText(""+queryGroupCount.getDate());
            holder.count.setText("共" + queryGroupCount.getCount()+"张");
            if(position == 0){
                holder.imageView.setVisibility(View.INVISIBLE);
                holder.count.setText("");
            }else{
                holder.imageView.setVisibility(View.VISIBLE);
                String path = queryHearld(queryGroupCount);
                ImageLoaderUtil.displayMyImage(path, holder.imageView);
            }

        }
        return convertView;
    }

    public String queryHearld(QueryGroupCount queryGroupCount){
        String sql = "strftime('%Y-%m-%d',create_time) = '"+queryGroupCount.getDate()+"' limit 1";
        List<AllPfAlbumSunObject> list = db.findAllByWhere(AllPfAlbumSunObject.class, sql);
        if(list != null && list.size() > 0){
            return list.get(0).getPath();
        }
        return null;
    }

    class Holder {
        TextView time;
        TextView count;
        ImageView imageView;
    }
}
