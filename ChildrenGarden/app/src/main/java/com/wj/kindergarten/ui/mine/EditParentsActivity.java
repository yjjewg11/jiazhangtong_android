package com.wj.kindergarten.ui.mine;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.BaseActivity;

/**
 * EditParentsActivity
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/26 1:38
 */
public class EditParentsActivity extends BaseActivity {
    private EditText nameEt;
    private EditText telEt;
    private EditText workEt;
    private LinearLayout nameLayout;
    private LinearLayout workLayout;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_edit_parent;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        setTitleText("修改信息", "保存");

        initViews();

        if (getIntent().hasExtra("name")) {
            nameLayout.setVisibility(View.VISIBLE);
            nameEt.setText(getIntent().getStringExtra("name"));
        } else {
            nameLayout.setVisibility(View.GONE);
        }
        if (getIntent().hasExtra("work")) {
            workLayout.setVisibility(View.VISIBLE);
            workEt.setText(getIntent().getStringExtra("work"));
        } else {
            workLayout.setVisibility(View.GONE);
        }
        telEt.setText(getIntent().getStringExtra("tel"));
    }

    private void initViews() {
        nameEt = (EditText) findViewById(R.id.child_edit_name);
        telEt = (EditText) findViewById(R.id.child_edit_tel);
        workEt = (EditText) findViewById(R.id.child_edit_work);
        nameLayout = (LinearLayout) findViewById(R.id.child_edit_name_layout);
        workLayout = (LinearLayout) findViewById(R.id.child_edit_work_layout);
    }

    @Override
    protected void titleRightButtonListener() {
        Intent intent = new Intent();
        if (nameLayout.getVisibility() == View.VISIBLE) {
            intent.putExtra("name", nameEt.getText().toString());
        }
        intent.putExtra("tel", telEt.getText().toString());
        if (workLayout.getVisibility() == View.VISIBLE) {
            intent.putExtra("work", workEt.getText().toString());
        }
        setResult(RESULT_OK, intent);
        finish();
    }
}
