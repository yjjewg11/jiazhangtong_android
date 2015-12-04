package com.wj.kindergarten.ui.mine;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Login;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.EditTextCleanWatcher;
import com.wj.kindergarten.utils.HintInfoDialog;
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

    private RelativeLayout layoutLogin = null;
    private RelativeLayout layoutPassword = null;
    private ImageView ivCleanLogin = null;
    private ImageView ivCleanPassword = null;

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

        accEt = (EditText) findViewById(R.id.login_acc);
        pwdEt = (EditText) findViewById(R.id.login_pwd);
        checkBox = (CheckBox) findViewById(R.id.login_check);
        rememberTv = (TextView) findViewById(R.id.login_check_text);
        forgetTv = (TextView) findViewById(R.id.login_forget);
        actionLoginTv = (TextView) findViewById(R.id.login_action);
        registerTv = (TextView) findViewById(R.id.login_register);
        layoutLogin = (RelativeLayout) findViewById(R.id.layout_clean_4);
        layoutPassword = (RelativeLayout) findViewById(R.id.layout_clean_5);
        ivCleanLogin = (ImageView) findViewById(R.id.iv_clean_1);
        ivCleanPassword = (ImageView) findViewById(R.id.iv_clean_2);

        accEt.addTextChangedListener(new EditTextCleanWatcher(ivCleanLogin, accEt));
        pwdEt.addTextChangedListener(new EditTextCleanWatcher(ivCleanPassword, pwdEt));
        layoutLogin.setOnClickListener(this);
        layoutPassword.setOnClickListener(this);

        rememberTv.setOnClickListener(this);
        forgetTv.setOnClickListener(this);
        actionLoginTv.setOnClickListener(this);
        registerTv.setOnClickListener(this);

        //TODO test acc
        checkBox.setChecked(CGSharedPreference.getRemerberPassword());
        String[] str = CGSharedPreference.getLogin();
        accEt.setText(str[0]);
        if (CGSharedPreference.getRemerberPassword()) {
            pwdEt.setText(str[1]);
        } else {
            pwdEt.setText("");
        }

        circleImage.setImageResource(R.drawable.logo);
//        if (Utils.stringIsNull(str[2])) {
//            circleImage.setImageResource(R.drawable.touxiang);
//        } else {
//            ImageLoaderUtil.displayImage(str[2], circleImage);
//        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CGSharedPreference.saveRemerberPassword(isChecked);
            }
        });

        // accEt.setText("13628037996");
        // pwdEt.setText("123456");

        accEt.setSelection(accEt.getText().length());
        pwdEt.setSelection(pwdEt.getText().length());

        acc = accEt.getText().toString().trim();
        pwd = pwdEt.getText().toString().trim();
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
                CGLog.d("tag: " + checkBox.isChecked());
                CGSharedPreference.saveRemerberPassword(checkBox.isChecked());
                break;
            case R.id.login_forget:
                Intent intentF = new Intent(mContext, RegisterActivity.class);
                intentF.putExtra("from", "login_forget");
                startActivity(intentF);
                break;
            case R.id.login_action:
                acc = accEt.getText().toString().trim();
                pwd = pwdEt.getText().toString().trim();
                if (checkDataIsOk()) {
                    login();
                }
                break;
            case R.id.login_register:
                Intent intentL = new Intent(mContext, RegisterActivity.class);
                intentL.putExtra("from", "login_register");
                startActivity(intentL);
                break;
            case R.id.layout_clean_4:
                accEt.setText("");
                break;
            case R.id.layout_clean_5:
                pwdEt.setText("");
                break;
            default:
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
        final HintInfoDialog dialog = new HintInfoDialog(LoginActivity.this, "登录中，请稍后...");
        if(!dialog.isShowing())
        dialog.show();
        UserRequest.login(mContext, acc, pwd, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                Login login = (Login) domain;
                CGApplication.getInstance().setLogin((Login) domain);
                CGSharedPreference.setStoreJESSIONID(login.getJSESSIONID());
                UserRequest.getUserInfo(LoginActivity.this, CGSharedPreference.getStoreJESSIONID(),login.getMd5());

                String imgPath = "";
                if (null != login && null != login.getUserinfo()) {
                    imgPath = Utils.getText(login.getUserinfo().getImg());
                }
                CGSharedPreference.saveLogin(accEt.getText().toString(), pwdEt.getText().toString(), imgPath);
                if (CGSharedPreference.getNoticeState(1)) {
                    UserRequest.deviceSave(LoginActivity.this, 0);//注册设备
                } else {
                    UserRequest.deviceSave(LoginActivity.this, 2);//注册设备
                }
                CGSharedPreference.setLoginOut(false);
                dialog.dismiss();
                startActivity(new Intent(mContext, MainActivity.class));
                finish();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                dialog.dismiss();
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(mContext, message);
                }
            }
        });
    }

}
