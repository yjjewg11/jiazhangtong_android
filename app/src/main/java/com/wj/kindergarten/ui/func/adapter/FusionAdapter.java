package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.QueryGroupCount;
import com.wj.kindergarten.compounets.NestedGridView;
import com.wj.kindergarten.utils.GloablUtils;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tangt on 2016/3/1.
 */
public class FusionAdapter extends BaseAdapter{
    private Context context;
    public FusionAdapter(Context context) {
        this.context = context;
    }
    private Map<String,List<AllPfAlbumSunObject>> map = new HashMap<>();
    private List<QueryGroupCount> queryGroupCounts = new ArrayList<>();

    public void setQueryMap(Map<String,List<AllPfAlbumSunObject>> map,List<QueryGroupCount> queryGroupCounts){
        this.queryGroupCounts.clear();
        this.queryGroupCounts.addAll(queryGroupCounts);
        this.map.clear();
        this.map.putAll(map);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return map.size();
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
        Holder holder;
        if(convertView == null){
            holder = new Holder();
            convertView = View.inflate(context, R.layout.pf_family_first_time_item,null);
            holder.pf_family_first_item_time = (TextView) convertView.findViewById(R.id.pf_family_first_item_time);
            holder.pf_family_first_item_count = (TextView) convertView.findViewById(R.id.pf_family_first_item_count);
            holder.pf_family_first_item_grid = (NestedGridView) convertView.findViewById(R.id.pf_family_first_item_grid);
            holder.adapter = new PfFirstTimeAdapter(context);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }

        QueryGroupCount count = queryGroupCounts.get(position);
        holder.pf_family_first_item_time.setText("" + count.getDate());
        holder.pf_family_first_item_count.setText("共" + count.getCount() + "张");
        holder.adapter.setObjectList(map.get(count.getDate()));
//                new PfFirstTimeAdapter(context,map.get(count.getDate()));
        holder.pf_family_first_item_grid.setAdapter(holder.adapter);
        return convertView;
    }

    class Holder{
        PfFirstTimeAdapter adapter;
        TextView pf_family_first_item_time;
        TextView pf_family_first_item_count;
        NestedGridView pf_family_first_item_grid;
    }
}
