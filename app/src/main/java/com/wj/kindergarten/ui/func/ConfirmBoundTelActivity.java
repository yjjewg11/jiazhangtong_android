package com.wj.kindergarten.ui.func;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.BaseActivity;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

public class ConfirmBoundTelActivity extends BaseActivity{

    @ViewInject(id = R.id.confirm_bound_tel_back,click = "onClick")
    RelativeLayout confirm_bound_tel_back;
    @ViewInject(id = R.id.bound_tel_confirm_now,click = "onClick")
    TextView bound_tel_confirm_now;
    @ViewInject(id = R.id.bound_tel_confirm_later,click = "onClick")
    TextView bound_tel_confirm_later;
    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_confirm_bound_tel;
    }

    @Override
    protected void setNeedLoading() {

    }

    String type = "绑定手机";
    @Override
    protected void onCreate() {
        FinalActivity.initInjectedView(this);
        String nowType =  getIntent().getStringExtra("type");
        if(nowType.equals("account")){
            type = "绑定账号";
        }
        bound_tel_confirm_now.setText(""+type);
    }
    public void onClick(View view){
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.bound_tel_confirm_now:
                intent.putExtra("status","now");
                break;
            case R.id.bound_tel_confirm_later:
                intent.putExtra("status","later");
                break;
            case R.id.confirm_bound_tel_back:
                intent.putExtra("status", "later");
                break;
        }
        setResult(RESULT_OK,intent);
        finish();
    }

}
