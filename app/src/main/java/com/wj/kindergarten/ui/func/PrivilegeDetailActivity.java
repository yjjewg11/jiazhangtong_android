package com.wj.kindergarten.ui.func;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.ArticleDetail;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.CallTransfer;
import com.wj.kindergarten.bean.PrivilegeActive;
import com.wj.kindergarten.common.Constants;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.more.CallUtils;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ShareUtils;
import com.wj.kindergarten.utils.Utils;

import java.util.List;

/**
 * AritcleListActivity
 *
 * @Description:优惠活动详情
 * @Author: thb
 * @CreateDate: 2015/8/4 23:03
 */
public class PrivilegeDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView titleTv;
    private WebView contentTv;
    private TextView nameTv;
    private TextView timeTv;
    private Context context;


    private TextView tvZan = null;
    private TextView tvSHare = null;
    private TextView tvStore = null;
    private TextView tvComment = null;
    private HintInfoDialog dialog = null;

    private boolean formStore = false;
    private final static int COMMENT = 1;

    private WebSettings webSettings;
    private String uuid;
    private String pauuid;
    private ArticleDetail article;
    private RelativeLayout privelege_ask_info;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_privilege;
    }

    @Override
    protected void setNeedLoading() {
        isNeedLoading = true;
    }

    @Override
    protected void loadData() {

        pauuid =  getIntent().getStringExtra("pauuid");
//        formStore = getIntent().getBooleanExtra("fromStore", false);
        getArticle();
    }

    @Override
    protected void onCreate() {

        context = this;
        setTitleText("活动详情");
        init();
        success();
    }

    private void init() {
        contentTv = (WebView) findViewById(R.id.article_content);
        contentTv.getSettings().setJavaScriptEnabled(true);
        contentTv.setBackgroundColor(0);
        webSettings = contentTv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //支持缩放
        webSettings.setBuiltInZoomControls(true);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        //开启缓存数据库功能set
        webSettings.setDatabaseEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
//        webSettings.setBlockNetworkImage(true);
        //设置缓存模式

        //设置缓存路径
//        webSettings.setAppCachePath(appCachePath+"/clear");
        //允许访问文件
        webSettings.setAllowFileAccess(true);

        contentTv.setAlpha(1);

        contentTv.setWebChromeClient(new WebChromeClient());
        contentTv.setWebViewClient(new WebViewClient());

        contentTv.loadUrl(article.getShare_url());

        tvZan = (TextView) findViewById(R.id.textview_1);
        tvSHare = (TextView) findViewById(R.id.textview_2);
        tvStore = (TextView) findViewById(R.id.textview_3);
        tvComment = (TextView) findViewById(R.id.textview_4);
        privelege_ask_info = (RelativeLayout)findViewById(R.id.privelege_ask_info);

        tvZan.setOnClickListener(this);
        tvSHare.setOnClickListener(this);
        tvStore.setOnClickListener(this);
        tvComment.setOnClickListener(this);
        privelege_ask_info.setOnClickListener(this);
    }

    private void getArticle() {
        UserRequest.getPrivilegeActive(mContext, pauuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                article = (ArticleDetail) domain;
                if (null != article && article.getData() != null) {
                    loadSuc();
                } else {
                    loadEmpty();
                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                loadEmpty();
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(PrivilegeDetailActivity.this, message);
                }
            }
        });
    }

    private void success() {
        if (null != article) {
            if (!article.isFavor()) {
                store1();
            } else {
                store2();
            }

            if (article.getData().getDianzan() != null) {
                if (!article.getData().getDianzan().isCanDianzan()) {
                    zan1();
                } else {
                    zan2();
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopWebview(contentTv);
    }


    //

    private void zan1() {
        Drawable drawable = getResources().getDrawable(R.drawable.zan2);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        tvZan.setCompoundDrawables(null, drawable, null, null);
        tvZan.setText("已点赞");
        tvZan.setTextColor(getResources().getColor(R.color.title_bg));
        article.getData().getDianzan().setCanDianzan(false);
    }

    private void zan2() {
        Drawable drawable = getResources().getDrawable(R.drawable.zan1);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        tvZan.setCompoundDrawables(null, drawable, null, null);
        tvZan.setText("点赞");
        tvZan.setTextColor(getResources().getColor(R.color.color_929292));
        article.getData().getDianzan().setCanDianzan(true);
    }

    private void store1() {
        Drawable drawable = getResources().getDrawable(R.drawable.store2);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        tvStore.setCompoundDrawables(null, drawable, null, null);
        tvStore.setText("已收藏");
        tvStore.setTextColor(getResources().getColor(R.color.title_bg));
        article.setIsFavor(false);
    }

    private void store2() {
        Drawable drawable = getResources().getDrawable(R.drawable.store1);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        tvStore.setCompoundDrawables(null, drawable, null, null);
        tvStore.setText("收藏");
        tvStore.setTextColor(getResources().getColor(R.color.color_929292));
        article.setIsFavor(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COMMENT) {
            boolean isComment = data.getBooleanExtra("isComment", false);
            if (isComment) {
                Drawable drawable = getResources().getDrawable(R.drawable.comment2);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                tvComment.setCompoundDrawables(null, drawable, null, null);
            }
        }
        /**使用SSO授权必须添加如下代码 */
        UMSocialService mController = ShareUtils.getInstance().mController;
        if (null != mController) {
            UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
            if (ssoHandler != null) {
                ssoHandler.authorizeCallBack(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textview_1://赞
                if ("点赞".equals(tvZan.getText().toString())) {
                    zan();
                } else {
                    cacelZan();
                }
                break;
            case R.id.textview_2://分享
                CGLog.d("share_url " + article.getShare_url());

                String content = article.getData().getContent();
                if (Utils.isNull(content) == null || content == null) {
                              content = article.getData().getTitle();
                }

                ShareUtils.showShareDialog(PrivilegeDetailActivity.this, tvSHare, article.getData().getTitle(),
                content, article.getData().getUrl(), article.getShare_url(),false);


                break;
            case R.id.textview_3://收藏
                if ("收藏".equals(tvStore.getText().toString())) {
                    store();
                } else {
                    cancelStore();
                }
                break;
            case R.id.textview_4://评论
                Intent intent = new Intent(mContext, NormalReplyListActivity.class);
                intent.putExtra("type", NormalReplyListActivity.PRIVILEGE_ACTIVE);
                intent.putExtra("replyId", article.getData().getUuid());
                startActivityForResult(intent, COMMENT);
                break;
            case R.id.privelege_ask_info:
                CallUtils.showCall(this,article.getLink_tel().split(GloablUtils.SPLIT_STIRNG_MOBILE_NUMBER),new CallTransfer(article.getData().getUuid(),85));
                break;
            default:
                break;
        }
    }

    private void zan() {
        dialog = new HintInfoDialog(PrivilegeDetailActivity.this, "点赞中，请稍后...");
        dialog.show();
        UserRequest.zan(context, article.getData().getUuid(), NormalReplyListActivity.PRIVILEGE_ACTIVE, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                dialog.dismiss();
                Utils.showToast(context, "点赞成功");
                zan1();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(context, message);
                }
                dialog.dismiss();
            }
        });
    }

    private void cacelZan() {
        dialog = new HintInfoDialog(context, "取消点赞中，请稍后...");
        dialog.show();
        UserRequest.zanCancel(context, article.getData().getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                dialog.dismiss();
                Utils.showToast(context, "取消点赞成功");
                zan2();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                dialog.dismiss();
            }
        });
    }

    private void store() {
        dialog = new HintInfoDialog(context, "收藏中，请稍后...");
        dialog.show();
        UserRequest.store(context, article.getData().getTitle(), NormalReplyListActivity.PRIVILEGE_ACTIVE, article.getData().getUuid(), "", new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                dialog.dismiss();
                Utils.showToast(context, "收藏成功");
                store1();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(context, message);
                }
                dialog.dismiss();
            }
        });
    }

    private void cancelStore() {
        dialog = new HintInfoDialog(context, "取消收藏中，请稍后...");
        dialog.show();
        UserRequest.cancelStore(formStore, context, article.getData().getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                dialog.dismiss();
                Utils.showToast(context, "取消收藏成功");
                store2();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(context, message);
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void titleLeftButtonListener() {
        if (formStore) {
            if (article != null) {
                Constants.isStore = !article.isFavor();
            }
        } else {
            Intent ownIntent = new Intent();
            setResult(RESULT_OK, ownIntent);
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (formStore) {
                if (article != null) {
                    Constants.isStore = !article.isFavor();
                }
            } else {
                Intent ownIntent = new Intent();
                setResult(RESULT_OK, ownIntent);
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}

