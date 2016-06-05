package com.wj.kindergarten.ui.mine;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.EditTextCleanWatcher;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.Utils;

import java.util.List;

/**
 * UpdatePasswordActivity
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-13 21:22
 */
public class UpdatePasswordActivity extends BaseActivity implements View.OnClickListener {
    private EditText editText1 = null;
    private EditText editText2 = null;
    private EditText editText3 = null;
    private RelativeLayout layoutClean1 = null;
    private RelativeLayout layoutClean2 = null;
    private RelativeLayout layoutClean3 = null;
    private ImageView imageView1 = null;
    private ImageView imageView2 = null;
    private ImageView imageView3 = null;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.update_password_activity;
    }

    @Override
    protected void setNeedLoading() {
        isNeedLoading = false;
    }

    @Override
    protected void onCreate() {
        setTitleText("修改密码", "保存");
        init();
    }

    private void init() {
        editText1 = (EditText) findViewById(R.id.et_pw1);
        editText2 = (EditText) findViewById(R.id.et_pw2);
        editText3 = (EditText) findViewById(R.id.et_pw3);
        layoutClean1 = (RelativeLayout) findViewById(R.id.layout_clean_1);
        layoutClean2 = (RelativeLayout) findViewById(R.id.layout_clean_2);
        layoutClean3 = (RelativeLayout) findViewById(R.id.layout_clean_3);
        imageView1 = (ImageView) findViewById(R.id.iv_1);
        imageView2 = (ImageView) findViewById(R.id.iv_2);
        imageView3 = (ImageView) findViewById(R.id.iv_3);

        editText1.addTextChangedListener(new EditTextCleanWatcher(imageView1, editText1));
        editText2.addTextChangedListener(new EditTextCleanWatcher(imageView2, editText2));
        editText3.addTextChangedListener(new EditTextCleanWatcher(imageView3, editText3));
        layoutClean1.setOnClickListener(this);
        layoutClean2.setOnClickListener(this);
        layoutClean3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_clean_1:
                editText1.setText("");
                break;
            case R.id.layout_clean_2:
                editText2.setText("");
                break;
            case R.id.layout_clean_3:
                editText3.setText("");
                break;
            default:
                break;
        }
    }

    private boolean check() {
        if (Utils.stringIsNull(editText1.getText().toString())) {
            Utils.showToast(UpdatePasswordActivity.this, "原密码不能为空");
            return false;
        }

        if (Utils.stringIsNull(editText2.getText().toString())) {
            Utils.showToast(UpdatePasswordActivity.this, "新密码不能为空");
            return false;
        }

        if (editText2.getText().toString().length() < 6 || editText2.getText().toString().length() > 16) {
            Utils.showToast(mContext, "请填写长度为6-16位的密码");
            return false;
        }

        if (Utils.stringIsNull(editText3.getText().toString())) {
            Utils.showToast(UpdatePasswordActivity.this, "确认密码不能为空");
            return false;
        }

        if (!editText2.getText().toString().trim().equals(editText3.getText().toString().trim())) {
            Utils.showToast(UpdatePasswordActivity.this, "新密码和确认密码不一致");
            return false;
        }

        return true;
    }

    @Override
    protected void titleRightButtonListener() {
        if (check()) {
            updatePassword();
        }
    }

    private void updatePassword() {
        final HintInfoDialog dialog = new HintInfoDialog(UpdatePasswordActivity.this, "修改密码中，请稍后...");
        dialog.show();
        UserRequest.updatePassword(UpdatePasswordActivity.this, editText1.getText().toString().trim(), editText2.getText().toString().trim(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                dialog.dismiss();
                Utils.showToast(UpdatePasswordActivity.this, "修改密码成功");
                String[] str = CGSharedPreference.getLogin();
                CGSharedPreference.saveLogin(str[0], "", str[2]);
                finish();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (!Utils.stringIsNull(message)) {
                    if ("Oldpassword不匹配！".equals(message)) {
                        Utils.showToast(UpdatePasswordActivity.this, "原密码输入错误");
                    } else {
                        Utils.showToast(UpdatePasswordActivity.this, message);
                    }
                }
                dialog.dismiss();
            }
        });
    }

}
