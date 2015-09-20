package com.wj.kindergarten.ui.func;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.ArticleDetail;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.common.Constants;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ShareUtils;
import com.wj.kindergarten.utils.URLImageParser;
import com.wj.kindergarten.utils.Utils;

import java.util.List;

/**
 * AritcleListActivity
 *
 * @Description:文章详情
 * @Author: Wave
 * @CreateDate: 2015/8/4 23:03
 */
public class ArticleActivity extends BaseActivity implements View.OnClickListener {
    private TextView titleTv;
    private WebView contentTv;
    private TextView nameTv;
    private TextView timeTv;

    private String uuid = "";
    private ArticleDetail article = null;

    private TextView tvZan = null;
    private TextView tvSHare = null;
    private TextView tvStore = null;
    private TextView tvComment = null;
    private HintInfoDialog dialog = null;

    private boolean formStore = false;
    private final static int COMMENT = 1;
    private TextView tvContent = null;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_article;
    }

    @Override
    protected void setNeedLoading() {
        isNeedLoading = true;
    }

    @Override
    protected void loadData() {
        uuid = getIntent().getStringExtra("uuid");
        formStore = getIntent().getBooleanExtra("fromStore", false);
        getArticle();
    }

    @Override
    protected void onCreate() {
        setTitleText("精品文章");
        init();
        success();
    }

    private void init() {
        titleTv = (TextView) findViewById(R.id.article_title);
        contentTv = (WebView) findViewById(R.id.article_content);
        nameTv = (TextView) findViewById(R.id.article_name);
        timeTv = (TextView) findViewById(R.id.article_time);

        tvContent = (TextView) findViewById(R.id.tv_con);
        tvContent.setText(Html.fromHtml(article.getData().getMessage(), new URLImageParser(tvContent, mContext), null));

        titleTv.setText(article.getData().getTitle());
        contentTv.getSettings().setJavaScriptEnabled(true);
        contentTv.setBackgroundColor(0);
        contentTv.setAlpha(1);
        contentTv.loadDataWithBaseURL(null, article.getData().getMessage(), "text/html", "utf-8", null);
        nameTv.setText(article.getData().getCreate_user());
        timeTv.setText(article.getData().getCreate_time());

        tvZan = (TextView) findViewById(R.id.textview_1);
        tvSHare = (TextView) findViewById(R.id.textview_2);
        tvStore = (TextView) findViewById(R.id.textview_3);
        tvComment = (TextView) findViewById(R.id.textview_4);

        tvZan.setOnClickListener(this);
        tvSHare.setOnClickListener(this);
        tvStore.setOnClickListener(this);
        tvComment.setOnClickListener(this);
    }

    private void getArticle() {
        UserRequest.getArticle(mContext, uuid, new RequestResultI() {
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
                loadFailed();
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(ArticleActivity.this, message);
                }
            }
        });
    }

    private void success() {
        if (null != article) {
            CGLog.d("isFavour" + article.isFavor());
            if (!article.isFavor()) {
                store1();
            } else {
                store2();
            }

            if (article.getData() != null && article.getData().getDianzan() != null) {
                if (!article.getData().getDianzan().isCanDianzan()) {
                    zan1();
                } else {
                    zan2();
                }
            }
        }
    }

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
                if (null != article) {
                    CGLog.d("c: " + tvContent.getText().toString());
                    CGLog.d("c:" + tvContent.getText().toString().length());
                    if (!Utils.stringIsNull(tvContent.getText().toString()) && !article.getData().getMessage().contains("<img")) {
                        ShareUtils.showShareDialog(ArticleActivity.this, tvSHare, titleTv.getText().toString(),
                                tvContent.getText().toString(), "", article.getShare_url(),false);
                    } else {
                        ShareUtils.showShareDialog(ArticleActivity.this, tvSHare, titleTv.getText().toString(),
                                titleTv.getText().toString(), "", article.getShare_url(),false);
                    }
                }
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
                intent.putExtra("type", NormalReplyListActivity.REPLY_TYPE_ARTICLE);
                intent.putExtra("replyId", article.getData().getUuid());
                startActivityForResult(intent, COMMENT);
                break;
            default:
                break;
        }
    }

    private void zan() {
        dialog = new HintInfoDialog(ArticleActivity.this, "点赞中，请稍后...");
        dialog.show();
        UserRequest.zan(ArticleActivity.this, article.getData().getUuid(), NormalReplyListActivity.REPLY_TYPE_ARTICLE, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                dialog.dismiss();
                Utils.showToast(ArticleActivity.this, "点赞成功");
                zan1();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(ArticleActivity.this, message);
                }
                dialog.dismiss();
            }
        });
    }

    private void cacelZan() {
        dialog = new HintInfoDialog(ArticleActivity.this, "取消点赞中，请稍后...");
        dialog.show();
        UserRequest.zanCancel(ArticleActivity.this, article.getData().getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                dialog.dismiss();
                Utils.showToast(ArticleActivity.this, "取消点赞成功");
                zan2();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(ArticleActivity.this, message);
                }
                dialog.dismiss();
            }
        });
    }

    private void store() {
        dialog = new HintInfoDialog(ArticleActivity.this, "收藏中，请稍后...");
        dialog.show();
        UserRequest.store(ArticleActivity.this, article.getData().getTitle(), 3, article.getData().getUuid(), "", new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                dialog.dismiss();
                Utils.showToast(ArticleActivity.this, "收藏成功");
                store1();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(ArticleActivity.this, message);
                }
                dialog.dismiss();
            }
        });
    }

    private void cancelStore() {
        dialog = new HintInfoDialog(ArticleActivity.this, "取消收藏中，请稍后...");
        dialog.show();
        UserRequest.cancelStore(formStore, ArticleActivity.this, article.getData().getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                dialog.dismiss();
                Utils.showToast(ArticleActivity.this, "取消收藏成功");
                store2();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(ArticleActivity.this, message);
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