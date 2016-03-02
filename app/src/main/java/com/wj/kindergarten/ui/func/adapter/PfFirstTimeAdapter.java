package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.ThreadManager;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tangt on 2016/3/1.
 */
public class PfFirstTimeAdapter extends BaseAdapter{
    private List<AllPfAlbumSunObject> objectList;
    public void notifyData(){
        notifyDataSetChanged();
    }
    private Context context;


    public PfFirstTimeAdapter(Context context,List<AllPfAlbumSunObject> objectList){
        this.objectList = objectList;
        this.context = context;
    }

    public PfFirstTimeAdapter(Context context) {
        this.context = context;
    }

    public void setObjectList(List<AllPfAlbumSunObject> objectList) {
        this.objectList = objectList;
        notifyData();
    }

    @Override
    public int getCount() {
        return objectList == null ? 0 : objectList.size();
    }

    @Override
    public Object getItem(int position) {
        return objectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView == null){
            holder = new Holder();
            convertView = View.inflate(context, R.layout.boutique_single_pic,null);
            holder.iv = (ImageView) convertView.findViewById(R.id.boutiqe_single_pic);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        AllPfAlbumSunObject o = objectList.get(position);
        if(o != null){
            ImageLoaderUtil.displayMyImage(o.getPath(),holder.iv);
        }
        return convertView;
    }

    class Holder{
        ImageView iv;
    }
}
