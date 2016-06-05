package com.wj.kindergarten.ui.more;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.utils.Utils;

/**
 * Created by tangt on 2016/3/28.
 */
public class MyProgressDialog extends Dialog{

    public MyProgressDialog(Context context) {
        super(context, R.style.Dialog);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_progress_dialog);
    }

}
