package com.wj.kindergarten.ui.mine;

import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.compounets.CountDownButton;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.EditTextCleanWatcher;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

import java.util.List;

/**
 * RigesterActivity
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/19 22:06
 */
public class RegisterActivity extends BaseActivity {
    private EditText mobileEt;
    private EditText pwdEt;
    private EditText pwdDEt;
    private EditText smsEt;
    private TextView pwdTv;
    private CountDownButton smsCDB;
    private TextView actionTv;
    private int type = 0;//0 register  1 forget


    private RelativeLayout layout1 = null;
    private RelativeLayout layout2 = null;
    private RelativeLayout layout3 = null;
    private RelativeLayout layout4 = null;
    private ImageView imageView1 = null;
    private ImageView imageView2 = null;
    private ImageView imageView3 = null;
    private ImageView imageView4 = null;

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_register;
    }

    @Override
    protected void onCreate() {
        if ("login_forget".equals(getIntent().getStringExtra("from"))) {
            setTitleText("忘记密码");
            type = 1;
        } else {
            setTitleText("用户注册");
        }

        initViews();

        if (type == 1) {
            pwdTv.setText("新密码");
        }
    }

    private void initViews() {
        mobileEt = (EditText) findViewById(R.id.register_mobile);
        pwdEt = (EditText) findViewById(R.id.register_pwd);
        pwdDEt = (EditText) findViewById(R.id.register_pwd_double);
        smsEt = (EditText) findViewById(R.id.register_sms);
        smsEt.setInputType(InputType.TYPE_CLASS_NUMBER);
        smsCDB = (CountDownButton) findViewById(R.id.register_count_down);
        actionTv = (TextView) findViewById(R.id.register_action);
        pwdTv = (TextView) findViewById(R.id.register_pwd_text);

        layout1 = (RelativeLayout) findViewById(R.id.layout_clean_6);
        layout2 = (RelativeLayout) findViewById(R.id.layout_clean_7);
        layout3 = (RelativeLayout) findViewById(R.id.layout_clean_8);
        layout4 = (RelativeLayout) findViewById(R.id.layout_clean_9);
        imageView1 = (ImageView) findViewById(R.id.iv_clean_6);
        imageView2 = (ImageView) findViewById(R.id.iv_clean_7);
        imageView3 = (ImageView) findViewById(R.id.iv_clean_8);
        imageView4 = (ImageView) findViewById(R.id.iv_clean_9);

//        mobileEt.addTextChangedListener(new EditTextCleanWatcher(imageView1,mobileEt));
//        pwdEt.addTextChangedListener(new EditTextCleanWatcher(imageView2,pwdEt));
//        pwdDEt.addTextChangedListener(new EditTextCleanWatcher(imageView3,pwdDEt));
//        smsEt.addTextChangedListener(new EditTextCleanWatcher(imageView4,smsEt));

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobileEt.setText("");
            }
        });

        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwdEt.setText("");
            }
        });

        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwdDEt.setText("");
            }
        });

        layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsEt.setText("");
            }
        });

        smsCDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.stringIsNull(mobileEt.getText().toString()) && mobileEt.getText().length() != 11) {
                    Utils.showToast(CGApplication.getInstance(), "请输入手机号码");
                    return;
                } else if (!Utils.isMobiPhoneNum(mobileEt.getText().toString())) {
                    Utils.showToast(RegisterActivity.this, "您输入的手机号码有误");
                    return;
                }

                UserRequest.getSmsCode(mContext, mobileEt.getText().toString(), type + 1, new RequestResultI() {
                    @Override
                    public void result(BaseModel domain) {

                    }

                    @Override
                    public void result(List<BaseModel> domains, int total) {

                    }

                    @Override
                    public void failure(String message) {
                        if (!Utils.stringIsNull(message)) {
                            Utils.showToast(mContext, message);
                        }
                        smsCDB.stopCountDown();
                    }
                });
                smsCDB.startCountDown();
            }
        });

        actionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkData()) {
                    if (type == 0) {
                        toRegister();
                    } else if (type == 1) {
                        forgetPwd();
                    }
                }
            }
        });
    }


    private boolean checkData() {
        if (Utils.stringIsNull(mobileEt.getText().toString())) {
            Utils.showToast(RegisterActivity.this, "请输入手机号码");
            return false;
        }

        if (Utils.stringIsNull(pwdEt.getText().toString())) {
            Utils.showToast(RegisterActivity.this, "请输入用户密码");
            return false;
        }

        if (Utils.stringIsNull(pwdDEt.getText().toString())) {
            Utils.showToast(RegisterActivity.this, "确认密码不能为空");
            return false;
        }

        if (!pwdEt.getText().toString().equals(pwdDEt.getText().toString())) {
            Utils.showToast(RegisterActivity.this, "用户密码和确认密码不一致");
            return false;
        }

        if (Utils.stringIsNull(smsEt.getText().toString())) {
            Utils.showToast(RegisterActivity.this, "验证码不能为空");
            return false;
        }

        if(pwdDEt.getText().toString().length() < 6 || pwdEt.getText().toString().length() < 6){
            ToastUtils.showMessage("密码长度不能小于6位数!");
            return false;
        }
        return true;
    }

    private void toRegister() {
        showProgressDialog("注册中，请稍候...");
        UserRequest.register(mContext, mobileEt.getText().toString(), pwdEt.getText().toString(),
                smsEt.getText().toString(), new RequestResultI() {
                    @Override
                    public void result(BaseModel domain) {
                        hideProgressDialog();
                        Utils.showToast(mContext, "注册成功");
                        finish();
                    }

                    @Override
                    public void result(List<BaseModel> domains, int total) {

                    }

                    @Override
                    public void failure(String message) {
                        hideProgressDialog();
                        if (!Utils.stringIsNull(message)) {
                            Utils.showToast(mContext, message);
                        }
                    }
                });
    }

    private void forgetPwd() {
        showProgressDialog("重置密码中，请稍候...");
        UserRequest.forgetPwd(mContext, mobileEt.getText().toString(), pwdEt.getText().toString(),
                smsEt.getText().toString(), new RequestResultI() {
                    @Override
                    public void result(BaseModel domain) {
                        hideProgressDialog();
                        Utils.showToast(RegisterActivity.this, "设置密码成功");
                        String[] str = CGSharedPreference.getLogin();
                        CGSharedPreference.saveLogin(str[0], "", str[2]);
                        finish();
                    }

                    @Override
                    public void result(List<BaseModel> domains, int total) {

                    }

                    @Override
                    public void failure(String message) {
                        hideProgressDialog();
                        ToastUtils.showMessage(message);
                    }
                });
    }
}
