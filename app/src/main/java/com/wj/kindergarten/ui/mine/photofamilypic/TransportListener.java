package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.wj.kindergarten.bean.AllPfAlbumSunObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/1/26.
 */
public class TransportListener implements View.OnClickListener {
    private int position;
    private List<AllPfAlbumSunObject> list;
    private Context context;


    public TransportListener(Context context,int position, List<AllPfAlbumSunObject> list) {
        this.position = position;
        this.list = list;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context,PfGalleryActivity.class);
        intent.putExtra("position",position);
        intent.putExtra("list",(ArrayList)list);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
