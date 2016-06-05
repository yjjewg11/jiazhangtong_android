package com.wj.kindergarten.ui.more;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Administrator on 2015/11/4.
 */
public class CallDialog extends Dialog {
    public CallDialog(Context context) {
        super(context);
    }

    public CallDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
