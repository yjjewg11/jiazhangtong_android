package com.wj.kindergarten.compounets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;


/**
 * NormalProgressDialog 菊花对话框
 *
 * @author weiwu.song
 * @data: 2015/5/20
 * @version: v1.0
 */
public class NormalProgressDialog extends Dialog {
    private TextView tvInfo;
    private String info;

    public NormalProgressDialog(Context context, String info) {
        super(context, R.style.DialogStyle);
        this.info = info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_normal_progress);
        tvInfo = (TextView) findViewById(R.id.tv_info);
        tvInfo.setText(info);
    }

    /**
     * 提示文字信息
     *
     * @param info 先show() ,再调用此方法
     */
    public void setInfo(String info) {
        if (tvInfo != null && isShowing()) {
            tvInfo.setText(info);
        }
    }
}
