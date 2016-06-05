package com.wj.kindergarten.ui.recuitstudents.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by tangt on 2015/12/1.
 */
public class SchoolIntroduceFragmentAdapter extends FragmentPagerAdapter {


    public SchoolIntroduceFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
