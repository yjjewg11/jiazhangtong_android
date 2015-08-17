package com.wj.kindergarten.ui.mine;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Login;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.AddressBookRequest;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.TestActivity;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.utils.Utils;

import java.util.List;

/**
 * LoginActivity
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/17 11:00
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private CircleImage circleImage = null;
    private EditText accEt = null;
    private EditText pwdEt = null;
    private CheckBox checkBox = null;
    private TextView rememberTv = null;
    private TextView forgetTv = null;
    private TextView actionLoginTv = null;
    private TextView registerTv = null;

    private String acc;
    private String pwd;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_login;
    }

    @Override
    protected void setNeedLoading() {
    }

    @Override
    protected void onCreate() {
        hideActionbar();

        circleImage = (CircleImage) findViewById(R.id.login_head);
        circleImage.setImageResource(R.drawable.touxiang);

        accEt = (EditText) findViewById(R.id.login_acc);
        pwdEt = (EditText) findViewById(R.id.login_pwd);
        checkBox = (CheckBox) findViewById(R.id.login_check);
        rememberTv = (TextView) findViewById(R.id.login_check_text);
        forgetTv = (TextView) findViewById(R.id.login_forget);
        actionLoginTv = (TextView) findViewById(R.id.login_action);
        registerTv = (TextView) findViewById(R.id.login_register);

        rememberTv.setOnClickListener(this);
        forgetTv.setOnClickListener(this);
        actionLoginTv.setOnClickListener(this);
        registerTv.setOnClickListener(this);

        //TODO test acc
        accEt.setText("13628037996");
        pwdEt.setText("123456");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_check_text:
                checkBox.setChecked(!checkBox.isChecked());
                break;
            case R.id.login_forget:
                Intent intentF = new Intent(mContext, RegisterActivity.class);
                intentF.putExtra("from", "login_forget");
                startActivity(intentF);
                break;
            case R.id.login_action:
                acc = accEt.getText().toString();
                pwd = pwdEt.getText().toString();
                if (checkDataIsOk()) {
                    login();
                }
                break;
            case R.id.login_register:
                Intent intentL = new Intent(mContext, RegisterActivity.class);
                intentL.putExtra("from", "login_register");
                startActivity(intentL);
                break;
        }
    }

    private boolean checkDataIsOk() {
        if (acc.length() < 1) {
            Utils.showToast(mContext, "请输入账号");
            return false;
        }
        if (pwd.length() < 6 || pwd.length() > 16) {
            Utils.showToast(mContext, "请填写长度为6-16位的密码");
            return false;
        }
        return true;
    }

    private void login() {
        showProgressDialog("登录中...");
        UserRequest.login(mContext, acc, pwd, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                ((CGApplication) CGApplication.getInstance()).setLogin((Login) domain);
                hideProgressDialog();
                UserRequest.deviceSave(LoginActivity.this);//注册设备
                AddressBookRequest.getEmot(LoginActivity.this);//获取表情列表
                startActivity(new Intent(mContext, MainActivity.class));
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
