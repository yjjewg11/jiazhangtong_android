package com.wj.kindergarten.ui.func.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.ui.mine.photofamilypic.pffragment.PFSingleObjectInfoFragment;
import com.wj.kindergarten.ui.mine.photofamilypic.pffragment.PfSingleInfoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/2/23.
 */
public class PfInfoFragmentAdapter extends FragmentStatePagerAdapter {

    private int mcount;
    private ViewPager viewPager;
    private PfSingleInfoFragment pfSingleInfoFragment;

    public PfInfoFragmentAdapter(FragmentManager fm,ViewPager viewPager,PfSingleInfoFragment pfSingleInfoFragment) {
        super(fm);
        this.viewPager = viewPager;
        this.pfSingleInfoFragment = pfSingleInfoFragment;
    }
    private List<AllPfAlbumSunObject> objectList = new ArrayList<>();

    public void setObjectList(List<AllPfAlbumSunObject> objectList){
           this.objectList.clear();
           this.objectList.addAll(objectList);
           notifyDataSetChanged();
    }

    public AllPfAlbumSunObject getPositionObject(int position){

         return objectList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return new PFSingleObjectInfoFragment(this,pfSingleInfoFragment,position);
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
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public void notifyDataSetChanged() {
        mcount = getCount();
        super.notifyDataSetChanged();
    }

}
