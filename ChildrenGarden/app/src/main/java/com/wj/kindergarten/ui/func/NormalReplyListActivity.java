package com.wj.kindergarten.ui.func;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Reply;
import com.wj.kindergarten.bean.ReplyList;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.NormalReplyAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * VoiceListActivity
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/2 17:40
 */
public class NormalReplyListActivity extends BaseActivity {
    private PullToRefreshListView mListView = null;
    private static final int PAGE_SIZE = 20;
    private int currentPage = 1;
    private String replyId;
    private NormalReplyAdapter adapter;
    private List<Reply> replies = new ArrayList<>();

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_pulltorefresh;
    }

    @Override
    protected void setNeedLoading() {
    }

    @Override
    protected void onCreate() {
        replyId = getIntent().getStringExtra("replyId");
        setTitleText("回复列表");
        mListView = (PullToRefreshListView) findViewById(R.id.pulltorefresh_list);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentPage = 1;
                getReplyList(currentPage);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (!mListView.isRefreshing()) {
                    getReplyList(currentPage + 1);

                }
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
        adapter = new NormalReplyAdapter(mContext);
        mListView.setAdapter(adapter);

        mHandler.sendEmptyMessageDelayed(1, 200);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (!mListView.isRefreshing()) {
                        mListView.setRefreshing();
                    }
                    break;
            }
        }
    };

    private void getReplyList(final int page) {
        UserRequest.getReplyList(mContext, replyId, page, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                ReplyList replyList = (ReplyList) domain;
                if (replyList.getList().getData() != null) {
                    replies.addAll(replyList.getList().getData());
                    adapter.setReplies(replies);
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


}
