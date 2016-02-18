package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.PfModeName;
import com.wj.kindergarten.bean.PfModeNameObject;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/2/18.
 */
public class BoutiqueModeAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;

    public BoutiqueModeAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    public void setList(List<PfModeNameObject> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }
    private List<PfModeNameObject> list = new ArrayList<>();

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView == null){
            holder = new Holder();
            convertView = inflater.inflate(R.layout.boutique_album_edit_item,null);
            holder.modeImg = (ImageView) convertView.findViewById(R.id.boutique_album_edit_imageView);
            holder.modeName = (TextView) convertView.findViewById(R.id.boutique_album_edit_titlesss);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        PfModeNameObject pfModeNameObject = list.get(position);
        if(pfModeNameObject != null){
            holder.modeName.setText(""+ Utils.isNull(pfModeNameObject.getTitle()));
            ImageLoaderUtil.displayImage(pfModeNameObject.getHerald(),holder.modeImg);
        }
        return convertView;
    }
    class Holder{
        TextView modeName;
        ImageView modeImg;
    }
}
