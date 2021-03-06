package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.PfAlbumListSun;
import com.wj.kindergarten.ui.mine.photofamilypic.PfAlbumListActivity;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/1/18.
 */
public class PfAlbumListAdapter extends BaseAdapter {
    private List<PfAlbumListSun> list = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;
    public int getSize(){
        return list.size();
    }

    public PfAlbumListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<PfAlbumListSun> list) {
        if(list != null){
            this.list.clear();
            this.list.addAll(list);
        }
        this.list.add(new PfAlbumListSun("你好，第一张"));
        this.list.add(new PfAlbumListSun("你好，第二张"));
        this.list.add(new PfAlbumListSun("邀请新成员"));
        notifyDataSetChanged();
    }

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
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.pf_mine_album,null);
            viewHolder = new ViewHolder();
            viewHolder.pf_mine_image = (ImageView) convertView.findViewById(R.id.pf_mine_image);
            viewHolder.pf_mine_tv = (TextView) convertView.findViewById(R.id.pf_mine_tv);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        if(position == list.size() - 1){
            viewHolder.pf_mine_image.setImageResource(R.drawable.tianjia_jpxc);
            viewHolder.pf_mine_tv.setText("添加新成员");
//            addListener(viewHolder.pf_mine_image);
        }else {
//            changListener(viewHolder.pf_mine_image);
            PfAlbumListSun sun = list.get(position);
            if(sun != null){
                viewHolder.pf_mine_tv.setText(""+sun.getTitle());
                ImageLoaderUtil.displayMyImage(sun.getHerald(),viewHolder.pf_mine_image);
            }
        }
        return convertView;
    }

    private void changListener(ImageView pf_mine_image) {
        pf_mine_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void addListener(ImageView pf_mine_image) {
        pf_mine_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入邀请成员页面
                ((PfAlbumListActivity)context).addFamilyMember("");
            }
        });
    }

    class ViewHolder{
        ImageView pf_mine_image;
        TextView pf_mine_tv;
    }
}
