package com.wj.kindergarten.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.UMSsoHandler;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.IOStoreData.StoreDataInSerialize;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Login;
import com.wj.kindergarten.bean.NeedBoundPhoneResult;
import com.wj.kindergarten.bean.ThreeInfo;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.BoundTelActivity;
import com.wj.kindergarten.ui.func.ConfirmBoundTelActivity;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.ui.more.DoEveryThing;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.EditTextCleanWatcher;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

import java.util.List;

/**
 * LoginActivity
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/17 11:00
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, DoEveryThing {
    private CircleImage circleImage = null;
    private EditText accEt = null;
    private EditText pwdEt = null;
    private TextView forgetTv = null;
    private TextView actionLoginTv = null;
    private TextView registerTv = null;
    private String loginType;

    private String acc;
    private String pwd;

    private RelativeLayout layoutLogin = null;
    private RelativeLayout layoutPassword = null;
    private ImageView ivCleanLogin = null;
    private ImageView ivCleanPassword = null;
    private UMSocialService mController;
    private ImageView login_three_qq;
    private ImageView login_three_weixin;
    private ImageView login_three_weibo;
    private String access_token;
    private String openid;

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


        mController = UMServiceFactory.getUMSocialService("com.umeng.login");

        circleImage = (CircleImage) findViewById(R.id.login_head);
        login_three_qq = (ImageView) findViewById(R.id.login_three_qq);
        login_three_weixin = (ImageView) findViewById(R.id.login_three_weixin);
        login_three_weibo = (ImageView) findViewById(R.id.login_three_weibo);

        login_three_qq.setOnClickListener(this);
        login_three_weixin.setOnClickListener(this);
        login_three_weibo.setOnClickListener(this);
        accEt = (EditText) findViewById(R.id.login_acc);
        pwdEt = (EditText) findViewById(R.id.login_pwd);
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
        forgetTv.setOnClickListener(this);
        actionLoginTv.setOnClickListener(this);
        registerTv.setOnClickListener(this);

        //TODO test acc
        String[] str = CGSharedPreference.getLogin();
        accEt.setText(str[0]);
        pwdEt.setText(str[1]);
//        if (CGSharedPreference.getRemerberPassword()) {
//            pwdEt.setText(str[1]);
//        } else {
//            pwdEt.setText("");
//        }

        circleImage.setImageResource(R.drawable.logo);
//        if (Utils.stringIsNull(str[2])) {
//            circleImage.setImageResource(R.drawable.touxiang);
//        } else {
//            ImageLoaderUtil.displayImage(str[2], circleImage);
//        }


        // accEt.setText("13628037996");
        // pwdEt.setText("123456");

        accEt.setSelection(accEt.getText().length());
        pwdEt.setSelection(pwdEt.getText().length());

        acc = accEt.getText().toString().trim();
        pwd = pwdEt.getText().toString().trim();
    }

    private void successGetAssess_token() {
        UserRequest.validateBanPhone(this, openid, access_token, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                NeedBoundPhoneResult needBoundPhoneResult = (NeedBoundPhoneResult) domain;
                if (needBoundPhoneResult != null) {
                    String needBound = needBoundPhoneResult.getNeedBindTel();
                    if (needBound == null || TextUtils.isEmpty(needBound)) return;
                    //需要绑定电话
                    if(needBound.equals("1")){
                        judgeHaveData();
                        //不需要绑定电话,直接进入主页，调试暂屏蔽
                    }
                    else if (needBound.equals("0")){
                        getThreeInfoToMainPage();
                    }
                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    //判断是否有用户数据
    private void judgeHaveData() {
       String [] infos =  CGSharedPreference.getLogin();
        //有数据,进行账号绑定
        if(infos != null && !Utils.stringIsNull(infos[0]) && !Utils.stringIsNull(infos[1])){
            bindAccount(infos[0]);
            //没有登录数据进入确认绑定页面提示是否绑定
        }else {
            Intent intent = new Intent(this, ConfirmBoundTelActivity.class);
            startActivityForResult(intent
                    , GloablUtils.CONFIRM_BOUND_TEL
            );
        }
    }

    private void bindAccount(String info) {
        UserRequest.boundTel(this, access_token,
                info,"",
                CGSharedPreference.getlogin_type(), new RequestResultI() {
                    @Override
                    public void result(BaseModel domain) {
                        ToastUtils.showMessage("绑定成功!");
                        getThreeInfoToMainPage();
                    }

                    @Override
                    public void result(List<BaseModel> domains, int total) {

                    }

                    @Override
                    public void failure(String message) {

                    }
                });
    }


    //获取用户信息并进入主页面
    private void getThreeInfoToMainPage() {
        UserRequest.getThreeUserInfo(mContext, access_token, loginType,this);
    }

    private void applyQQ() {
        mController.doOauthVerify(this, SHARE_MEDIA.QQ, new SocializeListeners.UMAuthListener() {
            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {

            }

            @Override
            public void onComplete(Bundle value, SHARE_MEDIA platform) {
                access_token = value.getString("access_token");
                openid = value.getString("openid");
                CGLog.v("打印access_token : " + access_token);
                if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
                    ToastUtils.showMessage("授权成功");
                    successGetAssess_token();
                } else {
                    ToastUtils.showMessage("授权失败");
                }
            }
//成功获取assess_token


            @Override
            public void onCancel(SHARE_MEDIA platform) {
            }

            @Override
            public void onStart(SHARE_MEDIA platform) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

        if(resultCode != RESULT_OK) return;
        switch (requestCode){
            case GloablUtils.CONFIRM_BOUND_TEL:
                String status = data.getStringExtra("status");
                //要绑定，进入绑定页面
                if(status.equals("now")){
                    Intent intent = new Intent(LoginActivity.this, BoundTelActivity.class);
                    intent.putExtra("access_token",access_token);
                    startActivityForResult(intent, GloablUtils.BOUND_TEL);
                    //不绑定，直接登陆
                }else {
                    getThreeInfoToMainPage();
                }
                break;
            //绑定成功，获取用户数据进入主页面
            case GloablUtils.BOUND_TEL:
                getThreeInfoToMainPage();
                break;
        }
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
            case R.id.login_three_weixin:
                loginType = "weixin";
                break;
            case R.id.login_three_qq:
                loginType = "qq";
                applyQQ();
                break;
            case R.id.login_three_weibo:
                loginType = "weibo";
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
        if (!dialog.isShowing())
            dialog.show();
        UserRequest.login(mContext, acc, pwd, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                Login login = (Login) domain;
                CGApplication.getInstance().setLogin((Login) domain);
                CGSharedPreference.setStoreJESSIONID(login.getJSESSIONID());
                StoreDataInSerialize.storeUserInfo(login);
                UserRequest.getUserInfo(LoginActivity.this, CGSharedPreference.getStoreJESSIONID(), login.getMd5());

                String imgPath = "";
                if (null != login && null != login.getUserinfo()) {
                    imgPath = Utils.getText(login.getUserinfo().getImg());
                }
                CGSharedPreference.saveLogin(accEt.getText().toString(), pwdEt.getText().toString(), imgPath);
                dialog.dismiss();
                registerDevice();
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

    //登录成功后注册设备
    private void registerDevice() {
        if (CGSharedPreference.getNoticeState(1)) {
            UserRequest.deviceSave(LoginActivity.this, 0);//注册设备
        } else {
            UserRequest.deviceSave(LoginActivity.this, 2);//注册设备
        }
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

    @Override
    public void everyThing() {
        registerDevice();
    }

//    class BaseUiListener implements IUiListener {
//
//        protected void doComplete(JSONObject values) {
//            CGLog.v("TAG", "doComplete : " + values.toString());
//        }
//
//        @Override
//        public void onComplete(Object o) {
//            CGLog.v("TAG", "onComplete : " + o.toString());
//        }
//
//        @Override
//        public void onError(UiError e) {
//            CGLog.v("TAG", "onError");
//        }
//
//        @Override
//        public void onCancel() {
//            CGLog.v("TAG", "onCancle");
//        }
//    }
}
