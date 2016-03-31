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
import com.wj.kindergarten.abstractbean.RequestFailedResult;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Login;
import com.wj.kindergarten.bean.NeedBoundPhoneResult;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.GuideActivity;
import com.wj.kindergarten.ui.func.BoundTelActivity;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.ui.more.BoundDialog;
import com.wj.kindergarten.ui.more.DoEveryThing;
import com.wj.kindergarten.ui.more.MyProgressDialog;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.EditTextCleanWatcher;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
    private MyProgressDialog myProgressBar;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getOpenid() {
        return openid;
    }

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


        myProgressBar = new MyProgressDialog(this);
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
        if(loginType.equals("qq")){
            qqValidate();
        }else if(loginType.equals("weixin")){
            weixinValidate();
        }

    }

    private void weixinValidate() {
        UserRequest.validateBanPhoneWEIXIN(this, getOpenid(), getAccess_token(), new RequestFailedResult() {
            @Override
            public void result(BaseModel domain) {
                NeedBoundPhoneResult needBoundPhoneResult = (NeedBoundPhoneResult) domain;
                validateResult(needBoundPhoneResult);
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                super.failure(message);
//                ToastUtils.showDialog(LoginActivity.this, "微信", access_token+"   :   "+message, null);
            }
        });
    }

    private void qqValidate() {
        UserRequest.validateBanPhoneQQ(this, getOpenid(), getAccess_token(), new RequestFailedResult() {
            @Override
            public void result(BaseModel domain) {
                NeedBoundPhoneResult needBoundPhoneResult = (NeedBoundPhoneResult) domain;
                validateResult(needBoundPhoneResult);
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                super.failure(message);
                ToastUtils.showDialog(LoginActivity.this, "QQ", message, null);
            }
        });
    }

    private void validateResult(NeedBoundPhoneResult needBoundPhoneResult) {
        if (needBoundPhoneResult != null) {
            String needBound = needBoundPhoneResult.getNeedBindTel();
            if (needBound == null || TextUtils.isEmpty(needBound)) return;
            //需要绑定电话
            if (needBound.equals("1")) {
                judgeHaveData();
                //不需要绑定电话,直接进入主页，调试暂屏蔽
            } else if (needBound.equals("0")) {
                getThreeInfoToMainPage();
            }
        }
    }

    //判断是否有用户数据
    private void judgeHaveData() {
        String[] infos = CGSharedPreference.getLogin();
        //有数据,进行账号绑定
        if (infos != null && !Utils.stringIsNull(infos[0]) && !Utils.stringIsNull(infos[1])) {
            ToastUtils.showBoundDialog(this, "绑定账号", new BoundDialog.AfterListener() {
                @Override
                public void bound() {
                    UserRequest.boundAccount(LoginActivity.this,access_token,"",loginType, new RequestFailedResult(commonDialog) {
                        @Override
                        public void result(BaseModel domain) {
                            getThreeInfoToMainPage();
                        }

                        @Override
                        public void result(List<BaseModel> domains, int total) {

                        }

                        @Override
                        public void failure(String message) {
                            super.failure(message);
                            boundTel();
                        }
                    });
                }

                @Override
                public void igone() {
                    getThreeInfoToMainPage();
                }
            });
            //没有登录数据进入确认绑定页面提示是否绑定
        } else {
            boundTel();
        }

    }

    private void boundTel() {
        ToastUtils.showBoundDialog(this, "绑定手机", new BoundDialog.AfterListener() {
            @Override
            public void bound() {
                beginBound("tel", getAccess_token());
            }

            @Override
            public void igone() {
                getThreeInfoToMainPage();
            }
        });
    }

    private void cancleMyDialog(){
        if(myProgressBar.isShowing()) myProgressBar.cancel();
    }

    //获取用户信息并进入主页面
    private void getThreeInfoToMainPage() {
        UserRequest.getThreeUserInfo(mContext, getAccess_token(), loginType, this);
    }

    private void apply() {
        SHARE_MEDIA share_media = SHARE_MEDIA.QQ;
        if(loginType.equals("qq")){

        }else if (loginType.equals("weixin")) {
            share_media = SHARE_MEDIA.WEIXIN;
        }
        mController.doOauthVerify(this, share_media, new SocializeListeners.UMAuthListener() {
            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
                CGLog.v("Denglu  异常");
                e.printStackTrace();
                ToastUtils.showMessage("微信请求异常!!!");

            }

            @Override
            public void onComplete(Bundle value, SHARE_MEDIA platform) {
                cancleMyDialog();
//                StringBuilder builder = new StringBuilder();
//                Set<String> stringSet = value.keySet();
//                Iterator<String> ite = stringSet.iterator();
//
//                while (ite.hasNext()){
//                    builder.append("    --"+ite.next());
//                }
//                ToastUtils.showDialog(LoginActivity.this, "标题", "" +builder.toString(), null);
                CGSharedPreference.storelogin_type(loginType);
                        access_token = value.getString("access_token");
                openid = value.getString("openid");
                CGLog.v("打印access_token : " + access_token);
                if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
                    successGetAssess_token();
                } else {
                    ToastUtils.showMessage("授权失败");
                }
            }
//成功获取assess_token

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                CGLog.v("登录取消");
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

        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case GloablUtils.CONFIRM_BOUND_TEL:
                String status = data.getStringExtra("status");
                //要绑定，进入绑定页面
                if (status.equals("now")) {
                    beginBound("tel",getAccess_token());
                    //不绑定，直接登陆
                } else {
                    getThreeInfoToMainPage();
                }
                break;
            //绑定成功，判断绑定类型，如果是账号，则进行手机绑定，否则直接进入主页面
            case GloablUtils.BOUND_TEL:
                getThreeInfoToMainPage();
//                String type = getIntent().getStringExtra("type");
//                if(type.equals("tel")){
//                    getThreeInfoToMainPage();
//                }else if(type.equals("account")){
//                    ToastUtils.showBoundDialog(this, "绑定手机", new BoundDialog.AfterListener() {
//                        @Override
//                        public void bound() {
//                            beginBound("tel", access_token);
//                        }
//                        @Override
//                        public void igone() {
//
//                        }
//                    });
//                }

                break;
        }
    }

    private void beginBound(String type,String access_tokens) {
        Intent intent = new Intent(LoginActivity.this, BoundTelActivity.class);
        intent.putExtra("access_token", access_tokens);
        intent.putExtra("boundType",type);
        startActivityForResult(intent, GloablUtils.BOUND_TEL);
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
                myProgressBar.show();
                loginType = "weixin";
                apply();
                break;
            case R.id.login_three_qq:
                myProgressBar.show();
                loginType = "qq";
                apply();
                break;
            case R.id.login_three_weibo:
                loginType = "weibo";
                ToastUtils.showMessage("暂未开放此功能!");
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
        startActivity(new Intent(mContext, GuideActivity.class));
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
