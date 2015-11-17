package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.PrivilegeActive;
import com.wj.kindergarten.ui.mine.PrivilegeActiveActivity;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/29.
 */
public class PrivilegeActiveAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private List<PrivilegeActive> list = new ArrayList<>();
    public PrivilegeActiveAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public List<PrivilegeActive> getList() {
        return list;
    }

    public void setList(List<PrivilegeActive> list) {


            this.list.addAll(list);
            notifyDataSetChanged();


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
        ViewHolder viewHolder ;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.privilege_active_item,null);
            viewHolder = new ViewHolder();
            viewHolder.iv_left = (ImageView) convertView.findViewById(R.id.iv_left);
            viewHolder.tv_privilege_active = (TextView) convertView.findViewById(R.id.tv_privilege_active);
            viewHolder.tv_ecducation = (TextView) convertView.findViewById(R.id.tv_ecducation);
            viewHolder.tv_zan_people = (TextView) convertView.findViewById(R.id.tv_zan_people);
            viewHolder.tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PrivilegeActive pa = list.get(position);
        if(pa != null){
            ImageLoaderUtil.displayMyImage(pa.getGroup_img(), viewHolder.iv_left);
            viewHolder.tv_distance.setText("" + pa.getDistance());
            viewHolder.tv_ecducation.setText("" + pa.getGroup_name());
            viewHolder.tv_zan_people.setText(""+pa.getCount());
            viewHolder.tv_privilege_active.setText("" + pa.getTitle());

            Drawable drawable = context.getResources().getDrawable(R.drawable.guanzhuxiao);
            drawable.setBounds(0, 0, 20, 20);
            viewHolder.tv_zan_people.setCompoundDrawables(drawable, null, null, null);

            Drawable drawable2 = context.getResources().getDrawable(R.drawable.juli);
            drawable2.setBounds(0, 0, 20, 20);
            viewHolder.tv_distance.setCompoundDrawables(drawable2, null, null, null);
        }


        return convertView;
    }

    class ViewHolder{
        ImageView iv_left;
        TextView tv_privilege_active,tv_ecducation,tv_zan_people,tv_distance;
    }
}
