package com.wj.kindergarten.ui.more;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by tangt on 2015/11/27.
 */
public class MyDialog extends Dialog {
    public MyDialog(Context context) {
        super(context);
    }

    public MyDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
