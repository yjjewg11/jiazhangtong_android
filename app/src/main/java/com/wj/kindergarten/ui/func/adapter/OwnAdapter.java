package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/26.
 */
public class OwnAdapter extends BaseAdapter {
    private List<String> list = new ArrayList<>();

    private Context context;
    public void setList(List<String> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public OwnAdapter(Context context) {
        this.context = context;
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context,R.layout.only_one_item,null);
        TextView textView = (TextView) view.findViewById(R.id.only_one_tv);
        textView.setText(list.get(position));
        return view;
    }
}
