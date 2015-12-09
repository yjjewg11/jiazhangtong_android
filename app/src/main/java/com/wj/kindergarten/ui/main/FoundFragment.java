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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view != null) return view;
        instance = this;
        view = inflater.inflate(R.layout.found_fragment,null);
        foundFragment = new FoundSunFragment();
        topicWebFragment = new TopicWebFragment();
        getFragmentManager().beginTransaction().add(R.id.found_two_fragment_ll,foundFragment)
                .add(R.id.found_two_fragment_ll,topicWebFragment).show(foundFragment).hide(topicWebFragment).commit();
        return view;
    }

    public TopicWebFragment showWeb(){
        getFragmentManager().beginTransaction().show(topicWebFragment).hide(foundFragment).commit();
        return topicWebFragment;
    }

    public void cancleWeb(){
        getFragmentManager().beginTransaction().show(foundFragment).hide(topicWebFragment).commit();
    }

    public boolean webIsShow(){
        return topicWebFragment.isVisible();
    }

    public FoundSunFragment getFoundFragment() {
        return foundFragment;
    }

    public TopicWebFragment getTopicWebFragment() {
        return topicWebFragment;
    }
}
