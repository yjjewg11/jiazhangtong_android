package com.wj.kindergarten.ui.mine;

import android.content.Intent;
import android.widget.EditText;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.BaseActivity;

/**
 * EditRemarkActivity
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/26 1:38
 */
public class EditRemarkActivity extends BaseActivity {
    private EditText remarkEt;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_edit_remark;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        setTitleText("修改备注", "保存");

        remarkEt = (EditText) findViewById(R.id.child_remark);

        remarkEt.setText(getIntent().getStringExtra("remark"));
        remarkEt.setSelection(remarkEt.getText().length());
    }

    @Override
    protected void titleRightButtonListener() {
        Intent parentIntent = new Intent();
        parentIntent.putExtra("remark", remarkEt.getText().toString());
        setResult(RESULT_OK, parentIntent);
        finish();
    }
}
