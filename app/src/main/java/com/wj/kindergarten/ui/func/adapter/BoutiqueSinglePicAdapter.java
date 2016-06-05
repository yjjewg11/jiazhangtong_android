package com.wj.kindergarten.ui.func.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wj.kindergarten.bean.AllPfAlbumSunObject;

import java.util.List;

/**
 * Created by tangt on 2016/3/1.
 */
public class BoutiqueSinglePicAdapter extends BaseAdapter {
    private List<AllPfAlbumSunObject> list;
    private boolean loadFromHttp = true;

    public void setLoadFromHttp(boolean loadFromHttp) {
        this.loadFromHttp = loadFromHttp;
    }

    public BoutiqueSinglePicAdapter(List<AllPfAlbumSunObject> list) {
        this.list = list;
    }

    public BoutiqueSinglePicAdapter() {
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
        return null;
    }
}
