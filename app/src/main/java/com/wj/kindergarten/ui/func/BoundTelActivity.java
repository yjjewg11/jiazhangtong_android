package com.wj.kindergarten.ui.func;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.compounets.CountDownButton;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import java.util.List;

public class BoundTelActivity extends BaseActivity{

    @ViewInject(id = R.id.bound_tel_send,click = "onClick")
    CountDownButton bound_tel_send;
    @ViewInject(id = R.id.bound_tel_confirm,click = "onClick")
    TextView bound_tel_confirm;
    @ViewInject(id = R.id.bound_tel_sms)
    EditText bound_tel_sms;
    @ViewInject(id = R.id.bound_tel_phone)
    EditText bound_tel_phone;
    String access_token;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_bound_tel;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        setTitleText("绑定手机");
        FinalActivity.initInjectedView(this);
        access_token = getIntent().getStringExtra("access_token");
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.bound_tel_send:
                sengSms();
                break;
            case R.id.bound_tel_confirm:
                boundTel();
                break;
        }
    }

    private void boundTel() {
        if(!checkData()) return;
        UserRequest.boundTel(this, access_token,
                bound_tel_phone.getText().toString(), bound_tel_sms.getText().toString(),
                CGSharedPreference.getlogin_type(), new RequestResultI() {
                    @Override
                    public void result(BaseModel domain) {
                        ToastUtils.showMessage("绑定成功!");
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void result(List<BaseModel> domains, int total) {

                    }

                    @Override
                    public void failure(String message) {

                    }
                });
    }

    private boolean checkData() {
        if (Utils.stringIsNull(bound_tel_phone.getText().toString()) && bound_tel_phone.getText().length() != 11) {
            Utils.showToast(CGApplication.getInstance(), "请输入手机号码");
            return false;
        } else if (!Utils.isMobiPhoneNum(bound_tel_phone.getText().toString())) {
            ToastUtils.showMessage("您输入的手机号码有误");
            return false;
        }
        if(Utils.stringIsNull(bound_tel_sms.getText().toString())){
            ToastUtils.showMessage("验证码不能为空!");
            return false;
        }
        return true;
    }

    private void sengSms() {
        if (Utils.stringIsNull(bound_tel_phone.getText().toString()) && bound_tel_phone.getText().length() != 11) {
            Utils.showToast(CGApplication.getInstance(), "请输入手机号码");
            return;
        } else if (!Utils.isMobiPhoneNum(bound_tel_phone.getText().toString())) {
            ToastUtils.showMessage("您输入的手机号码有误");
            return;
        }

        UserRequest.getSmsCode(mContext, bound_tel_phone.getText().toString(), 4, new RequestResultI() {
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
                bound_tel_send.stopCountDown();
            }
        });
        bound_tel_send.startCountDown();
    }
}
