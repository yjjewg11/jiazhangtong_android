package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BoutiqueAlbum;

import java.util.List;

/**
 * Created by tangt on 2016/1/31.
 */
public class BoutiqueAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private List<BoutiqueAlbum> list;

    public void setList(List<BoutiqueAlbum> list) {

        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public BoutiqueAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        //TODO 添加的假数据
        return 10;
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
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.boutique_album_itme,null);
            viewHolder.boutique_album_item_title = (TextView) convertView.findViewById(R.id.boutique_album_item_title);
            viewHolder.boutique_album_item_people = (TextView) convertView.findViewById(R.id.boutique_album_item_title);
            viewHolder.boutique_album_item_time = (TextView) convertView.findViewById(R.id.boutique_album_item_time);
            viewHolder.boutique_album_item_image = (ImageView) convertView.findViewById(R.id.boutique_album_item_image);
            viewHolder.boutique_bottom_delete = (FrameLayout) convertView.findViewById(R.id.boutique_bottom_delete);
            viewHolder.boutique_bottom_edit = (FrameLayout) convertView.findViewById(R.id.boutique_bottom_edit);
            viewHolder.boutique_bottom_pinglun = (FrameLayout) convertView.findViewById(R.id.boutique_bottom_pinglun);
            viewHolder.boutique_bottom_edit2 = (FrameLayout) convertView.findViewById(R.id.boutique_bottom_edit2);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }
    class ViewHolder {
        TextView boutique_album_item_title,boutique_album_item_people,boutique_album_item_time;
        ImageView boutique_album_item_image;
        FrameLayout boutique_bottom_delete,boutique_bottom_edit,boutique_bottom_pinglun,boutique_bottom_edit2;
    }
}
