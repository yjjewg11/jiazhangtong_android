package com.wj.kindergarten.ui.mine;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.compounets.CountDownButton;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.Utils;

import java.util.List;

/**
 * ForgetPwdActivity
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/20 0:24
 */
public class ForgetPwdActivity extends BaseActivity {
    private EditText mobileEt;
    private EditText pwdEt;
    private EditText pwdDEt;
    private EditText smsEt;
    private CountDownButton smsCDB;
    private TextView actionTv;


    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_register;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        setTitleText("忘记密码");

        initViews();
    }

    private void initViews() {
        mobileEt = (EditText) findViewById(R.id.register_mobile);
        pwdEt = (EditText) findViewById(R.id.register_pwd);
        pwdDEt = (EditText) findViewById(R.id.register_pwd_double);
        smsEt = (EditText) findViewById(R.id.register_sms);
        smsCDB = (CountDownButton) findViewById(R.id.register_count_down);
        actionTv = (TextView) findViewById(R.id.register_action);
        TextView pwdTv = (TextView) findViewById(R.id.register_pwd_text);
        pwdTv.setText("新密码");

        smsCDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.stringIsNull(mobileEt.getText().toString()) && mobileEt.getText().length() != 11) {
                    Utils.showToast(CGApplication.getInstance(), "请输入电话号码");
                    return;
                }

                smsCDB.startCountDown();

            }
        });

        actionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkData()) {
                    toRegister();
                }
            }
        });
    }

    private boolean checkData() {
        return true;
    }

    private void toRegister() {
        showProgressDialog("修改密码中，请稍候...");
        UserRequest.forgetPwd(mContext, mobileEt.getText().toString(), pwdEt.getText().toString(),
                smsEt.getText().toString(), new RequestResultI() {
                    @Override
                    public void result(BaseModel domain) {
                        hideProgressDialog();

                        finish();
                    }

                    @Override
                    public void result(List<BaseModel> domains, int total) {

                    }

                    @Override
                    public void failure(String message) {
                        hideProgressDialog();
                        Utils.showToast(mContext, message);
                    }
                });
    }
}
