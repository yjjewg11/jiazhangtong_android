package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.PfMusic;
import com.wj.kindergarten.bean.PfMusicSunObject;
import com.wj.kindergarten.services.PlayMusicService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/1/21.
 */
public class ShowMusicAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<PfMusicSunObject> oneList = new ArrayList<>();

    public List<PfMusicSunObject> getOneList(){
        return oneList;
    }

    public void setOneList(List<PfMusicSunObject> oneList) {
        this.oneList.clear();
        this.oneList.addAll(oneList);
        notifyDataSetChanged();
    }

    public List<PfMusicSunObject> getList() {
        return list;
    }

    public ShowMusicAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    private List<PfMusicSunObject> list = new ArrayList<>();

    public void setList(List<PfMusicSunObject> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size() == 0 ? 0 : list.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.pf_item_music, null);
            viewHolder.pf_item_music_linear = (LinearLayout) convertView.findViewById(R.id.pf_item_music_linear);
            viewHolder.pf_item_music_text = (TextView) convertView.findViewById(R.id.pf_item_music_text);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final PfMusicSunObject object = list.get(position);

        if(object != null ){
            viewHolder.pf_item_music_text.setCompoundDrawables(null, null, null, null);
            viewHolder.pf_item_music_text.setText("" + object.getTitle());
            viewHolder.pf_item_music_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =  new Intent(context, PlayMusicService.class);
                    intent.putExtra("musicPath",object.getPath());
                    intent.putExtra("status",0);
                    context.startService(intent);
                    oneList.clear();
                    oneList.add(list.get(position));
                    notifyDataSetChanged();
                }
            });

            if(oneList.size() > 0){
                if(object.getPath().equals(oneList.get(0).getPath())){
                    Drawable drawable = context.getResources().getDrawable(R.drawable.xuanzhong_pf);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    viewHolder.pf_item_music_text.setCompoundDrawables(null, null, drawable, null);
                }
            }

        }
        return convertView;
    }

    class ViewHolder{
        LinearLayout pf_item_music_linear;
        TextView pf_item_music_text;
    }
}
