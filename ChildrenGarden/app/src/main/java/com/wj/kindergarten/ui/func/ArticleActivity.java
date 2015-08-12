package com.wj.kindergarten.ui.func;

import android.text.Html;
import android.view.MotionEvent;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.ArticleDetail;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.URLImageParser;
import com.wj.kindergarten.utils.Utils;

import java.util.List;

/**
 * AritcleListActivity
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/4 23:03
 */
public class ArticleActivity extends BaseActivity {
    private TextView titleTv;
    private TextView contentTv;
    private TextView nameTv;
    private TextView timeTv;

    private String uuid = "";
    private ArticleDetail article;
    private float preY = 0;

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
        getArticle();
    }

    @Override
    protected void onCreate() {
        setTitleText("精品文章");
        titleTv = (TextView) findViewById(R.id.article_title);
        contentTv = (TextView) findViewById(R.id.article_content);
        nameTv = (TextView) findViewById(R.id.article_name);
        timeTv = (TextView) findViewById(R.id.article_time);

        titleTv.setText(article.getData().getTitle());
        contentTv.setText(Html.fromHtml(article.getData().getMessage(), new URLImageParser(contentTv, mContext), null));
        nameTv.setText(article.getData().getCreate_user());
        timeTv.setText(article.getData().getCreate_time());
    }

    private void getArticle() {
        UserRequest.getArticle(mContext, uuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                article = (ArticleDetail) domain;

                loadSuc();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                loadFailed();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float nowY = event.getY();
            if (nowY - preY > 10) {//down
                Utils.showToast(mContext, "hide");
            } else {//up
                Utils.showToast(mContext, "show");
            }
//            preY = nowY;
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            preY = event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            preY = 0;
        }
        return super.dispatchTouchEvent(event);
    }

}
