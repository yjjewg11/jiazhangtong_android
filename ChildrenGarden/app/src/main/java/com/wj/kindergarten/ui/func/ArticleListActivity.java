package com.wj.kindergarten.ui.func;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.Article;
import com.wj.kindergarten.bean.ArticleList;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.ArticleAdapter;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * AritcleListActivity
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/4 23:03
 */
public class ArticleListActivity extends BaseActivity {
    private PullToRefreshListView mListView;

    private List<Article> articles = new ArrayList<>();
    private ArticleAdapter articleAdapter;
    private int currentPage = 1;
    private Article article = null;
    private static final int ZAN = 1;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_article_list;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        setTitleText("精品文章");

        mListView = (PullToRefreshListView) findViewById(R.id.pulltorefresh_list);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentPage = 1;
                getArticleList(currentPage);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getArticleList(currentPage + 1);
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, ArticleActivity.class);
                article = articles.get(i - 1);
                if (null != article) {
                    intent.putExtra("uuid", article.getUuid());
                    intent.putExtra("fromStore", false);
                    startActivityForResult(intent, ZAN);
                }
            }
        });
        articleAdapter = new ArticleAdapter(mContext);
        mListView.setAdapter(articleAdapter);

        mHandler.sendEmptyMessageDelayed(1, 300);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ZAN:
                    getArticleList(1);
                    break;
            }
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (mListView != null && !mListView.isRefreshing()) {
                        mListView.setRefreshing();
                    }
                    break;
            }
        }
    };


    private void getArticleList(final int page) {
        UserRequest.getArticleList(mContext, page, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                if (mListView != null && mListView.isRefreshing()) {
                    mListView.onRefreshComplete();
                }
                ArticleList articleList = (ArticleList) domain;
                if (articleList != null && articleList.getList() != null && articleList.getList().getData() != null) {
                    if (page == 1) {
                        articles.clear();
                    }
                    articles.addAll(articleList.getList().getData());
                    articleAdapter.setArticles(articles);
                    currentPage = page;
                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (mListView != null && mListView.isRefreshing()) {
                    mListView.onRefreshComplete();
                }
                Utils.showToast(mContext, message);
            }
        });
    }
}
