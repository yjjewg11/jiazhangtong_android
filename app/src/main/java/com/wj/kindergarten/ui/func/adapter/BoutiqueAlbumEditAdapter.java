package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.ui.mine.photofamilypic.BoutiqueAlbumEditActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.SinglePfEditActivity;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/1/31.
 */
public class BoutiqueAlbumEditAdapter extends BaseAdapter {
    private Context context;
    private List<AllPfAlbumSunObject> objectList = new ArrayList<>();
    public BoutiqueAlbumEditAdapter(Context context) {
        this.context = context;
    }

    public void setObjectList(List<AllPfAlbumSunObject> objectList) {
        this.objectList.clear();
        this.objectList.addAll(objectList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return objectList == null ? 0 : objectList.size();
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
            convertView = View.inflate(context, R.layout.boutique_album_edit_item,null);
            viewHolder.boutique_album_edit_image = (ImageView) convertView.findViewById(R.id.boutique_album_edit_image);
            viewHolder.boutique_album_edit_imageView = (ImageView) convertView.findViewById(R.id.boutique_album_edit_imageView);
            viewHolder.boutique_album_edit_title = (TextView) convertView.findViewById(R.id.boutique_album_edit_titlesss);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
            final AllPfAlbumSunObject object = objectList.get(position);
        if(object != null){
            ImageLoaderUtil.displayMyImage(object.getPath(),viewHolder.boutique_album_edit_imageView);
            if(Utils.stringIsNull(object.getNote())){
                viewHolder.boutique_album_edit_image.setVisibility(View.VISIBLE);
            }else{
                viewHolder.boutique_album_edit_image.setVisibility(View.GONE);
                viewHolder.boutique_album_edit_title.setText("" + object.getNote());
            }
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editObj(object);
                }
            };
            viewHolder.boutique_album_edit_title.setOnClickListener(listener);
            viewHolder.boutique_album_edit_image.setOnClickListener(listener);

        }
        return convertView;
    }

    private void editObj(AllPfAlbumSunObject object) {
        BoutiqueAlbumEditActivity activity =  (BoutiqueAlbumEditActivity) context;
        activity.startForResult(object);
    }

    class ViewHolder{
        TextView boutique_album_edit_title;
        ImageView boutique_album_edit_imageView;
        ImageView boutique_album_edit_image;
    }
}
