package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.PfMusic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/1/21.
 */
public class ShowMusicAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<PfMusic> oneList = new ArrayList<>();

    public ShowMusicAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    private List<PfMusic> list = new ArrayList<>();

    public void setList(List<PfMusic> list) {
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
        final PfMusic pfMusic = list.get(position);
        if(oneList.size() > 0){
            if (pfMusic.equals(oneList.get(0))){
                Drawable drawable = context.getDrawable(R.drawable.xuanzhong_pf);
                viewHolder.pf_item_music_text.setCompoundDrawables(null, null, null, drawable);
            }
        }
        if(pfMusic != null ){
            viewHolder.pf_item_music_text.setText(""+pfMusic.getTitle());
            viewHolder.pf_item_music_text.setCompoundDrawables(null, null, null, null);
            viewHolder.pf_item_music_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    oneList.clear();
                    oneList.add(pfMusic);
                    notifyDataSetChanged();
                }
            });
        }
        return convertView;
    }

    class ViewHolder{
        LinearLayout pf_item_music_linear;
        TextView pf_item_music_text;
    }
}
