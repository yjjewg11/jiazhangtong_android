package com.wj.kindergarten.ui.func;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Reply;
import com.wj.kindergarten.bean.ReplyList;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.emot.SendMessage;
import com.wj.kindergarten.ui.emot.ViewEmot2;
import com.wj.kindergarten.ui.func.adapter.NormalReplyAdapter;
import com.wj.kindergarten.utils.Utils;

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
    public static int RECRUIT_STUDENT = 4;
    private PullToRefreshListView mListView = null;
    private LinearLayout layout = null;

    public static final int REPLY_TYPE_SCHOOL_NOTICE = 0;//校园公告
    public static final int REPLY_TYPE_TEACHER_NOTICE = 1;//老师公告
    public static final int REPLY_TYPE_ARTICLE = 3;//精品文章
    public static final int REPLY_TYPE_ZHAOSHENG = 4;//招生计划
    public static final int REPLY_TYPE_JINGPING = 5;//精品课程
    public static final int REPLY_TYPE_FOOD = 6;//食谱
    public static final int REPLY_TYPE_COURSE = 7;//课程表
    public static final int REPLY_TYPE_JIESHAO = 8;//幼儿园介绍
    public static final int REPLY_TYPE_INTERACTION = 99;//互动
    public static final int REPLY_TYPE_HTML = 10;//html类型
    public static final int REPLY_TYPE_TEACHER_XIN = 11;//信件 老师
    public static final int REPLY_TYPE_BOSS_XIN = 12;//信件  园长
    public static final int REPLY_TYPE_TRAIN_CLASS = 14;
    private static final int PAGE_SIZE = 20;
    public static final int TRAIN_SCHOOL = 81;//培训机构
    public static final int TRAIN_COURSE= 82;//培训课程
    public static final int PRIVILEGE_ACTIVE= 85;//培训课程

    private int currentPage = 1;
    private String replyId;
    private NormalReplyAdapter adapter;
    private List<Reply> replies = new ArrayList<>();
    private int type = -1;//点赞的类型

    private ViewEmot2 emot2 = null;

    private LinearLayout layoutHide = null;
    private boolean isComment = false;
    private TextView tvWriteComment = null;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_normal_reply;
    }

    @Override
    protected void setNeedLoading() {
    }

    @Override
    protected void onCreate() {
        replyId = getIntent().getStringExtra("replyId");
        type = getIntent().getIntExtra("type", 0);
        setTitleText("评论");
        mListView = (PullToRefreshListView) findViewById(R.id.pulltorefresh_list);
        mListView.setClickable(false);
        layout = (LinearLayout) findViewById(R.id.bottom);

        emot2 = new ViewEmot2(this, new SendMessage() {
            @Override
            public void send(String message) {
                sendReply(message);
            }
        });

        layoutHide = (LinearLayout) findViewById(R.id.layout_hide);
        layoutHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emot2.hideFaceLayout();
            }
        });

        tvWriteComment = (TextView) findViewById(R.id.tv_write_comment);
        tvWriteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeAllViews();
                layout.addView(emot2);
                emot2.showInput();
            }
        });

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
                emot2.hideFaceLayout();
            }
        });
        adapter = new NormalReplyAdapter(type, mContext);
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
                if (mListView.isRefreshing()) {
                    mListView.onRefreshComplete();
                }
                if (page == 1) {
                    replies.clear();
                }
                ReplyList replyList = (ReplyList) domain;
                if (replyList.getList().getData() != null) {
                    replies.addAll(replyList.getList().getData());
                    adapter.setReplies(replies);
                }
                currentPage = page;
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (mListView.isRefreshing()) {
                    mListView.onRefreshComplete();
                }
                Utils.showToast(mContext, message);
            }
        });
    }

    private void sendReply(String message) {
        showProgressDialog();
        UserRequest.reply(mContext, replyId, message, "", type, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                hideProgressDialog();
                getReplyList(1);
                emot2.cleanEditText();
                isComment = true;
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                hideProgressDialog();
                Utils.showToast(mContext, message);
            }
        });
    }

    @Override
    protected void titleLeftButtonListener() {
        Intent ownIntent = new Intent();
        ownIntent.putExtra("isComment", isComment);
        setResult(RESULT_OK, ownIntent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent ownIntent = new Intent();
            ownIntent.putExtra("isComment", isComment);
            setResult(RESULT_OK, ownIntent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
