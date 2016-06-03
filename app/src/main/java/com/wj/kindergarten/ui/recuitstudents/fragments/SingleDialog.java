package com.wj.kindergarten.ui.recuitstudents.fragments;

import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.utils.HintInfoDialog;

/**
 * Created by tangt on 2015/12/2.
 */
public class SingleDialog {
    private static HintInfoDialog singleDialog = new HintInfoDialog(CGApplication.context);

    public static HintInfoDialog getInstance() {
        return singleDialog;
    }

    private SingleDialog(){

    }
}
