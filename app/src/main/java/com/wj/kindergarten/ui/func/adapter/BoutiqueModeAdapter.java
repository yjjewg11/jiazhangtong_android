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
    String key;

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
            holder.boutique_album_edit_tv_choose = (TextView) convertView.findViewById(R.id.boutique_album_edit_tv_choose);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        PfModeNameObject pfModeNameObject = list.get(position);
        if(pfModeNameObject != null){
            holder.modeName.setText(""+ Utils.isNull(pfModeNameObject.getTitle()));
            ImageLoaderUtil.displayImage(pfModeNameObject.getHerald(),holder.modeImg);
        }
        if(pfModeNameObject != null && pfModeNameObject.getKey() != null && key != null &&
                key.equals(pfModeNameObject.getKey())){
                holder.boutique_album_edit_tv_choose.setVisibility(View.VISIBLE);
        }else {
            holder.boutique_album_edit_tv_choose.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void setKey(String template_key) {
        key = template_key;
    }

    class Holder{
        TextView modeName,boutique_album_edit_tv_choose;
        ImageView modeImg;
    }
}
