package com.wj.kindergarten.ui.mine;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.ImageLoaderUtil;

/**
 * EditChildActivity
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/26 1:37
 */
public class EditChildActivity extends BaseActivity implements View.OnClickListener {
    private CircleImage circleImage;
    private TextView headTv;
    private EditText nameEt;
    private EditText nickEt;
    private EditText birthEt;
    private RadioButton nanRB;
    private RadioButton nvRB;

    private String heaUrl = "";

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_edit_child;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        setTitleText("修改详细", "保存");

        initView();

        ImageLoaderUtil.displayImage(getIntent().getStringExtra("head"), circleImage);
        nameEt.setText(getIntent().getStringExtra("name"));
        nickEt.setText(getIntent().getStringExtra("nick"));
        birthEt.setText(getIntent().getStringExtra("birth"));
        if (getIntent().getIntExtra("sex", 0) == 0) {
            nanRB.setChecked(true);
            nvRB.setChecked(false);
        } else {
            nanRB.setChecked(false);
            nvRB.setChecked(true);
        }
    }

    private void initView() {
        circleImage = (CircleImage) findViewById(R.id.child_edit_head);
        headTv = (TextView) findViewById(R.id.child_edit_head_tv);
        nameEt = (EditText) findViewById(R.id.child_edit_name);
        nickEt = (EditText) findViewById(R.id.child_edit_nick);
        birthEt = (EditText) findViewById(R.id.child_edit_birth);
        nanRB = (RadioButton) findViewById(R.id.child_edit_nan);
        nvRB = (RadioButton) findViewById(R.id.child_edit_nv);

        circleImage.setImageResource(R.drawable.main_item);

        circleImage.setOnClickListener(this);
        headTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.child_edit_head:
            case R.id.child_edit_head_tv:

                break;
        }
    }

    @Override
    protected void titleRightButtonListener() {
        Intent ownIntent = new Intent();
        ownIntent.putExtra("head", heaUrl);
        ownIntent.putExtra("name", nameEt.getText().toString());
        ownIntent.putExtra("nick", nickEt.getText().toString());
        ownIntent.putExtra("sex", nanRB.isChecked() ? 0 : 1);
        ownIntent.putExtra("birth", birthEt.getText().toString());
        setResult(RESULT_OK, ownIntent);
        finish();
    }
}
