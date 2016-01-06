package com.wj.kindergarten.ui.webview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wj.kindergarten.ui.func.SchoolDetailInfoActivity;
import com.wj.kindergarten.ui.specialcourse.TeachersSpecialFragment;
import com.wj.kindergarten.wrapper.DoOwnThing;
import com.wj.kindergarten.wrapper.MoveWeb;

/**
 * Created by tangt on 2016/1/6.
 */
public class SchoolFragment extends BasicWebFragment {
    private MoveWeb moveWeb;
    private SchoolDetailInfoActivity activity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null) {
            activity = (SchoolDetailInfoActivity)getActivity();
            super.onCreateView(inflater,container,savedInstanceState);
            moveWeb = new MoveWeb(webView);
            moveWeb.setDoOwnThing(new MyDothing());
            webView.setOnTouchListener(moveWeb);
            webView.loadUrl(activity.getDetailList().getObj_url());
        }
        return view;
    }

    class MyDothing implements DoOwnThing{

        @Override
        public void pullFromTop() {
            activity.getAnimTop().reverse();
        }

        @Override
        public void pullFromEnd() {
            activity.getAnimTop().start();
        }
    }
}
