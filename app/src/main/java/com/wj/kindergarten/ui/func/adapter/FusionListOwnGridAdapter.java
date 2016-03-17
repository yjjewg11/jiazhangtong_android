package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.QueryGroupCount;
import com.wj.kindergarten.ui.mine.photofamilypic.TransportListener;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.TimeUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tangt on 2016/3/4.
 */
public class FusionListOwnGridAdapter extends BaseAdapter implements
        StickyGridHeadersSimpleAdapter {
    private Map<String,List<AllPfAlbumSunObject>> map = new HashMap<>();
    private List<QueryGroupCount> queryGroupCounts = new ArrayList<>();
    private List<AllPfAlbumSunObject> allObjects = new ArrayList<>();

    private Context context;
    private LayoutInflater inflater;
    public FusionListOwnGridAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setQueryMap(Map<String,List<AllPfAlbumSunObject>> map,List<QueryGroupCount> queryGroupCounts){
        this.queryGroupCounts.clear();
        this.queryGroupCounts.addAll(queryGroupCounts);
        this.map.clear();
        this.map.putAll(map);
        notifyDataSetChanged();
    }
    public void setQueryList(List<QueryGroupCount> queryGroupCounts,List<AllPfAlbumSunObject> allObjects){
        this.queryGroupCounts.clear();
        this.allObjects.clear();
        this.queryGroupCounts.addAll(queryGroupCounts);
        this.allObjects.addAll(allObjects);
        notifyDataSetChanged();
    }

    @Override
    public long getHeaderId(int position) {
        long time = -1;
        try {
            String ymdTime =  TimeUtil.getYMDTimeFromYMDHMS(allObjects.get(position).getPhoto_time());
            time = TimeUtil.formatYMD.parse(ymdTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeadHolder headHolder;
        if(convertView == null){
            headHolder = new HeadHolder();
            convertView = inflater.inflate(R.layout.pf_family_fusion_head,null);
            headHolder.pf_family_first_item_time = (TextView) convertView.findViewById(R.id.pf_family_first_item_time);
            headHolder.pf_family_first_item_count = (TextView) convertView.findViewById(R.id.pf_family_first_item_count);
            convertView.setTag(headHolder);
        }else {
            headHolder = (HeadHolder) convertView.getTag();
        }
        AllPfAlbumSunObject object = allObjects.get(position);
        if(object != null){
            headHolder.pf_family_first_item_time.setText(""+ TimeUtil.getYMDTimeFromYMDHMS(object.getPhoto_time()));
            String time = TimeUtil.getYMDTimeFromYMDHMS(object.getPhoto_time());
            int count = -1;
            if(mapSize.size() > 0 && mapSize.containsKey(time)){
                count = mapSize.get(time);
            }
            headHolder.pf_family_first_item_count.setText(""+(count > 0 ? "共"+count+"张" : ""));
        }
        return convertView;
    }

    private HashMap<String,Integer> mapSize = new HashMap<>();
    public void setqueryCount(HashMap<String, Integer> mapSize) {
        mapSize.putAll(mapSize);
    }

    class HeadHolder{
        TextView pf_family_first_item_time;
        TextView pf_family_first_item_count;
    }


    @Override
    public int getCount() {
        return allObjects.size();
    }

    @Override
    public Object getItem(int position) {
        return allObjects.get(position);
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
            convertView = View.inflate(context, R.layout.boutique_single_pic,null);
            holder.iv = (ImageView) convertView.findViewById(R.id.boutiqe_single_pic);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        AllPfAlbumSunObject o = allObjects.get(position);
        if(o != null){
            ImageLoaderUtil.displayAlbumImage(o.getPath(), holder.iv);
//            holder.iv.setOnClickListener( new TransportListener(context,position,allObjects,null));
        }
        return convertView;
    }
    class Holder{
        ImageView iv;
    }

}
