package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/1/28.
 */
public class RecycleAdapter
//        extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder>
{
    private Context context;
    private List<AllPfAlbumSunObject> list = new ArrayList<>();

    public void setList(List<AllPfAlbumSunObject> list) {
        this.list.addAll(list);
    }

    public RecycleAdapter(Context context) {
        this.context = context;
    }

//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = View.inflate(context, R.layout.pf_recycle_image_item,null);
//        return new MyViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position) {
//        if(list.get(position) == null) return;
//        ImageLoaderUtil.displayMyImage(list.get(position).getPath(),holder.imageView);
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    class MyViewHolder extends RecyclerView.ViewHolder{
//        private ImageView imageView;
//
//        public MyViewHolder(View itemView) {
//            super(itemView);
//            imageView = (ImageView) itemView.findViewById(R.id.pf_recycle_image);
//        }
//    }
}
