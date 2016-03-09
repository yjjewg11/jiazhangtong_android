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
import com.wj.kindergarten.bean.BoutiqueAlbumListSun;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/1/31.
 */
public class BoutiqueAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private List<BoutiqueAlbumListSun> list = new ArrayList<>();

    public void setList(List<BoutiqueAlbumListSun> list) {

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
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.boutique_album_itme,null);
            viewHolder.boutique_album_item_dianzan = (TextView) convertView.findViewById(R.id.boutique_album_item_dianzan);
            viewHolder.boutique_album_item_title = (TextView) convertView.findViewById(R.id.boutique_album_item_title);
            viewHolder.boutique_album_item_people = (TextView) convertView.findViewById(R.id.boutique_album_item_title);
            viewHolder.boutique_album_item_count = (TextView) convertView.findViewById(R.id.boutique_album_item_count);
            viewHolder.boutique_album_item_image = (ImageView) convertView.findViewById(R.id.boutique_album_item_image);
            viewHolder.boutique_bottom_delete = (FrameLayout) convertView.findViewById(R.id.boutique_bottom_delete);
            viewHolder.boutique_bottom_edit = (FrameLayout) convertView.findViewById(R.id.boutique_bottom_edit);
            viewHolder.boutique_bottom_pinglun = (FrameLayout) convertView.findViewById(R.id.boutique_bottom_pinglun);
            viewHolder.boutique_bottom_edit2 = (FrameLayout) convertView.findViewById(R.id.boutique_bottom_edit2);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BoutiqueAlbumListSun sun = list.get(position);
        if(sun != null){
            ImageLoaderUtil.displayImage(sun.getHerald(),viewHolder.boutique_album_item_image);
            viewHolder.boutique_album_item_title.setText("" + sun.getTitle());
            viewHolder.boutique_album_item_people.setText(sun.getCreate_username());
            viewHolder.boutique_album_item_count.setText(""+sun.getPhoto_count());
        }

        return convertView;
    }
    class ViewHolder {
        TextView boutique_album_item_title,boutique_album_item_people,
                boutique_album_item_count,boutique_album_item_dianzan;
        ImageView boutique_album_item_image;
        FrameLayout boutique_bottom_delete,boutique_bottom_edit,boutique_bottom_pinglun,boutique_bottom_edit2;
    }
}
