package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;

import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.QueryGroupCount;
import com.wj.kindergarten.utils.PopWindowUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/1/26.
 */
public class TransportListener implements View.OnClickListener,AdapterView.OnItemClickListener {
    private int position;
    private List<AllPfAlbumSunObject> list;
    private Context context;
    private List<QueryGroupCount> queryGroupCounts ;


    public TransportListener(Context context,int position, List<AllPfAlbumSunObject> list) {
        this.position = position;
        this.list = list;
        this.context = context;
    }

    public TransportListener(Context context, int position, List<AllPfAlbumSunObject> list, List<QueryGroupCount> queryGroupCounts) {
        this.position = position;
        this.list = list;
        this.context = context;
        this.queryGroupCounts = queryGroupCounts;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context,PfGalleryActivity.class);
        intent.putExtra("position",position);
        intent.putExtra("list",(ArrayList)list);
        intent.putExtra("countList",(ArrayList)queryGroupCounts);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(context,PfGalleryActivity.class);
        intent.putExtra("position",position);
        intent.putExtra("list",(ArrayList)list);
        intent.putExtra("countList",(ArrayList)queryGroupCounts);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
