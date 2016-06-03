package com.wj.kindergarten.ui.coursefragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wenjie.jiazhangtong.R;

/**
 * Created by tanghongbin on 16/4/5.
 */
public class MessageAddressFragment extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view != null)return view;
        view = inflater.inflate(R.layout.message_address_fragment,null);
        return view;
    }
}
