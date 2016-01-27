package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import java.util.List;

/**
 * Created by tangt on 2016/1/27.
 */
public class PfAlbumRecycleAdapter extends RecyclerView.Adapter<PfAlbumRecycleAdapter.MViewHolder>{
    private Context context;
    private List<AllPfAlbumSunObject> list;

    public PfAlbumRecycleAdapter(Context context, List<AllPfAlbumSunObject> list) {
        this.context = context;
        this.list = list;
    }

    public PfAlbumRecycleAdapter(Context context) {
        this.context = context;
    }

    @Override
    public PfAlbumRecycleAdapter.MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.pf_album_recycle_item,null);
        return new MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MViewHolder holder, int position) {
        ViewGroup.LayoutParams params = holder.imageView.getLayoutParams();
        params.height = getRandomHeight();
        holder.imageView.setLayoutParams(params);
        AllPfAlbumSunObject object = list.get(position);
        if(object != null){
            ImageLoaderUtil.displayMyImage(object.getPath(),holder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return (list.size() >= 6 ? 6 : list.size());
    }

   class MViewHolder extends RecyclerView.ViewHolder {

       ImageView imageView;
       public MViewHolder(View itemView) {
           super(itemView);
           imageView = (ImageView) itemView.findViewById(R.id.pf_album_recycle_item_image);
           imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100));
       }

   }

    private int getRandomHeight(){
        return (int) (100+50*Math.random());
    }
}
