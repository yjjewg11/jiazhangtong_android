package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AlreadySavePath;
import com.wj.kindergarten.ui.mine.photofamilypic.UpLoadActivity;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import java.util.List;

/**
 * Created by tangt on 2016/3/29.
 */
public class UpLoadAdapter extends BaseAdapter{
    List<AlreadySavePath> alreadySavePathsList;
    Context context;
    public UpLoadAdapter(Context context, List<AlreadySavePath> alreadySavePathsList) {
        this.alreadySavePathsList = alreadySavePathsList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return alreadySavePathsList.size();
    }

    @Override
    public Object getItem(int position) {
        return alreadySavePathsList.get(position);
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
            convertView = View.inflate(context,R.layout.upload_progress_item,null);
            holder.bar = (RoundCornerProgressBar) (convertView.findViewById(R.id.up_load_progressBar));
            holder.up_load_progress_image = (ImageView) convertView.findViewById(R.id.up_load_progress_image);
            holder.up_Load_wait = (ImageView) convertView.findViewById(R.id.up_Load_wait);
            holder.upload_tv_progress = (TextView) convertView.findViewById(R.id.upload_tv_progress);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }

        AlreadySavePath alreadySavePath = alreadySavePathsList.get(position);
        ImageLoaderUtil.displayMyImage("file://" + alreadySavePath.getLocalPath(), holder.up_load_progress_image);
        int progressUpdate =(int) (Double.valueOf(alreadySavePath.getProgress())/Double.valueOf(alreadySavePath.getTotal()) * 100);
        holder.upload_tv_progress.setText(""+progressUpdate+"%");
        holder.bar.setProgress(0);
        if(alreadySavePath.getStatus() == 0){
            holder.up_Load_wait.setImageDrawable(context.getResources().getDrawable(R.drawable.wait_translate));
        }else if(alreadySavePath.getStatus() == 1){
            holder.up_Load_wait.setImageDrawable(context.getResources().getDrawable(R.drawable.wait_translate));
        }else if(alreadySavePath.getStatus() == 3){
            holder.up_Load_wait.setImageDrawable(context.getResources().getDrawable(R.drawable.upload_failed));
        }
        if(progressUpdate > 1){
            holder.bar.setProgress(progressUpdate);
            holder.up_Load_wait.setVisibility(View.GONE);
            holder.upload_tv_progress.setVisibility(View.VISIBLE);
        }else {
            holder.up_Load_wait.setVisibility(View.VISIBLE);
            holder.upload_tv_progress.setVisibility(View.GONE);
        }

        return convertView;
    }
    class Holder{
        RoundCornerProgressBar bar;
        ImageView up_load_progress_image,up_Load_wait;
        TextView upload_tv_progress;
    }
}
