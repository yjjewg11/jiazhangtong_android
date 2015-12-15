package com.wj.kindergarten.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wenjie.jiazhangtong.R;

/**
 * Created by tangt on 2015/12/9.
 */
public class FoundFragment extends Fragment {
    private View view;
    private TopicWebFragment topicWebFragment;
    private FoundSunFragment foundFragment;
    public static FoundFragment instance;
    private boolean webIsShow;

    public boolean webIsShow() {
        return webIsShow;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setText("发现");
        if(view != null) return view;
        instance = this;
        view = inflater.inflate(R.layout.found_fragment,null);
        foundFragment = new FoundSunFragment();
        topicWebFragment = new TopicWebFragment();
        getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).add(R.id.found_two_fragment_ll,foundFragment)
                .add(R.id.found_two_fragment_ll,topicWebFragment).show(foundFragment).hide(topicWebFragment).commit();
        return view;
    }

    public TopicWebFragment showWeb(){
        webIsShow = true;
        ((MainActivity)getActivity()).isTure = false;
        ((MainActivity)getActivity()).getSupportActionBar().hide();
        getFragmentManager().beginTransaction().show(topicWebFragment).hide(foundFragment).commit();
        return topicWebFragment;
    }

    public void cancleWeb(){
        webIsShow = false;
        ((MainActivity)getActivity()).getSupportActionBar().show();
        getFragmentManager().beginTransaction().show(foundFragment).hide(topicWebFragment).commit();
    }


    public FoundSunFragment getFoundFragment() {
        return foundFragment;
    }

    public TopicWebFragment getTopicWebFragment() {
        return topicWebFragment;
    }
}
