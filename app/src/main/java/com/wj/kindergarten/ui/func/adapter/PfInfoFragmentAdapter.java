package com.wj.kindergarten.ui.func.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.ui.mine.photofamilypic.pffragment.PFSingleObjectInfoFragment;
import com.wj.kindergarten.ui.mine.photofamilypic.pffragment.PfSingleInfoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/2/23.
 */
public class PfInfoFragmentAdapter extends FragmentPagerAdapter {

    private int mcount;
    private PfSingleInfoFragment pfSingleInfoFragment;

    public PfInfoFragmentAdapter(FragmentManager fm,PfSingleInfoFragment pfSingleInfoFragment) {
        super(fm);
        this.pfSingleInfoFragment = pfSingleInfoFragment;
    }
    private List<AllPfAlbumSunObject> objectList = new ArrayList<>();

    public void setObjectList(List<AllPfAlbumSunObject> objectList){
           this.objectList.clear();
           this.objectList.addAll(objectList);
           notifyDataSetChanged();

    }

    @Override
    public Fragment getItem(int position) {
        AllPfAlbumSunObject object = objectList.get(position);
        return new PFSingleObjectInfoFragment(object,pfSingleInfoFragment);
    }

    @Override
    public int getCount() {
        return objectList == null ? 0 : objectList.size();
    }
    @Override
    public int getItemPosition(Object object) {

            return POSITION_NONE;

//        return super.getItemPosition(object);
    }

    @Override
    public void notifyDataSetChanged() {
        mcount = getCount();
        super.notifyDataSetChanged();

    }

}
