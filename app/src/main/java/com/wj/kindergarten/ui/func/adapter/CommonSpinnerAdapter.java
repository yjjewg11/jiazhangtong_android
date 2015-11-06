package com.wj.kindergarten.ui.func.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;

import java.util.List;

public class CommonSpinnerAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private List<String> list;

    public CommonSpinnerAdapter(Context context,List<String> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list  =  list;
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.one_item,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_spinner = (TextView) convertView.findViewById(R.id.tv_call);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_spinner.setText(""+list.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView tv_spinner;
    }
}
