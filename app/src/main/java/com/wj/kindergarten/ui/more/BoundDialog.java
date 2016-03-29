package com.wj.kindergarten.ui.more;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.func.BoundTelActivity;
import com.wj.kindergarten.utils.Utils;

/**
 * Created by tangt on 2016/3/28.
 */
public class BoundDialog extends Dialog{
    private TextView bound_tel_confirm_now;
    private TextView bound_tel_confirm_later;

    public BoundDialog(Context context, String info) {
        super(context, R.style.Dialog);
    }

    public BoundDialog(Context context) {
        super(context, R.style.Dialog);

    }
    AfterListener afterListener;
    public BoundDialog(Context context,AfterListener afterListener) {
        super(context, R.style.Dialog);

        this.afterListener = afterListener;
    }
    String title;
    public BoundDialog(Context context,String title,AfterListener afterListener) {
        super(context, R.style.Dialog);
        this.afterListener = afterListener;
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bound_tel_dialog);
        bound_tel_confirm_now = (TextView) findViewById(R.id.bound_tel_confirm_now);
        bound_tel_confirm_later = (TextView) findViewById(R.id.bound_tel_confirm_later);
        if(!Utils.stringIsNull(title)) bound_tel_confirm_now.setText(""+title);
        bound_tel_confirm_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
                if(afterListener != null) afterListener.bound();
            }
        });
        bound_tel_confirm_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
                if(afterListener != null) afterListener.igone();
            }
        });

    }

    public interface AfterListener{
        void bound();
        void igone();
    }

}
