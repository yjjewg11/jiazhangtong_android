package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;

import com.wj.kindergarten.bean.MoreDiscuss;

import java.util.List;

/**
 * Created by tangt on 2015/12/18.
 */
public class RecruitAssessAdapter extends MoreDiscussAdapter {
    public RecruitAssessAdapter(Context context,List<MoreDiscuss> list) {
        super(context);
        this.list.addAll(list);
    }
}
