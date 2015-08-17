package com.wj.kindergarten.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;

/**
 * Created by zoupengqiang on 14-3-27.
 * 信息提示对话框
 */
public class HintInfoDialog extends Dialog {
    private TextView tvInfo;
    private String info;

    public HintInfoDialog(Context context, String info) {
        super(context, R.style.Dialog);
        this.info = info;
    }

    public HintInfoDialog(Context context){
        super(context,R.style.Dialog);
        this.info = context.getString(R.string.loading_content);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_hint_info);
        tvInfo = (TextView) findViewById(R.id.tv_hint_info);
        tvInfo.setText(info);
    }

}
