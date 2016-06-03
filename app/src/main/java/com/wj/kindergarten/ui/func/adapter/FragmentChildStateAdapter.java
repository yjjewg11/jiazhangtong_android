package com.wj.kindergarten.ui.func.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.wj.kindergarten.bean.ChildInfo;
import com.wj.kindergarten.ui.mine.ChildInfoFragment;

import java.util.List;

/**
 * Created by tanghongbin on 16/4/6.
 */
public class FragmentChildStateAdapter extends FragmentStatePagerAdapter {
    List<ChildInfo> childInfos;

    public FragmentChildStateAdapter(FragmentManager fm, List<ChildInfo> childInfos) {
        super(fm);
        this.childInfos = childInfos;
    }

    public FragmentChildStateAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new ChildInfoFragment(this,position);
    }

    @Override
    public int getCount() {
        return childInfos.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public ChildInfo getChildInfo(int position){
        return childInfos.get(position);
    }

}
