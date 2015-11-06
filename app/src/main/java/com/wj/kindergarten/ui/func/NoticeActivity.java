package com.wj.kindergarten.ui.func;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.DianZan;
import com.wj.kindergarten.bean.NoticeDetail;
import com.wj.kindergarten.bean.Reply;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.emot.EmotUtil;
import com.wj.kindergarten.ui.emot.SendMessage;
import com.wj.kindergarten.ui.emot.ViewEmot2;
import com.wj.kindergarten.utils.ShareUtils;
import com.wj.kindergarten.utils.Utils;

import java.util.List;

/**
 * NoticeActivity
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/29 1:10
 */
public class NoticeActivity extends BaseActivity {
    private TextView titleTv;
    private WebView contentTv;
    private TextView orgTv;
    private TextView dateTv;
    private TextView seeTv;
    private TextView zanCountTv;
    private TextView iReplyEt;
    private ImageView zanIv;
    private ImageView replyIv;
    private LinearLayout replyLayout;
    private TextView showMoreReplyTv;

    public RelativeLayout rootView;
    private LinearLayout bottomLayou;
    private ViewEmot2 emot2 = null;

    private NoticeDetail notice;
    private String uuid = "";
    private boolean zanLock = false;
    private ImageView iv_share;
    private TextView tv_notice_date;
    public LinearLayout root_ll;


    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_notice;
    }

    @Override
    protected void setNeedLoading() {
        isNeedLoading = true;
    }

    @Override
    protected void loadData() {
        uuid = getIntent().getStringExtra("uuid");
        getNotice();
    }
    public static NoticeActivity instance;
    @Override
    protected void onCreate() {
        setTitleText("公告详情");
        initViews();
        instance = this;
    }

    private void initViews() {
        root_ll = (LinearLayout)findViewById(R.id.root_ll);
        rootView = (RelativeLayout) findViewById(R.id.notice_root);
        titleTv = (TextView) findViewById(R.id.notice_title);
        contentTv = (WebView) findViewById(R.id.notice_content);
        orgTv = (TextView) findViewById(R.id.notice_org);
        dateTv = (TextView) findViewById(R.id.notice_date);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideBottomLayout();
            }
        });

        titleTv.setText(notice.getData().getTitle());
        contentTv.getSettings().setJavaScriptEnabled(true);
        contentTv.setBackgroundColor(0);
        contentTv.setAlpha(1.0f);
        contentTv.loadDataWithBaseURL(null, notice.getData().getMessage(), "text/html", "utf-8", null);
        orgTv.setText(Utils.getGroupNameFromId(notice.getData().getGroupuuid()));
        dateTv.setText(notice.getData().getCreate_time());
        bottomLayou = (LinearLayout) findViewById(R.id.notice_bottom);
        emot2 = new ViewEmot2(this, new SendMessage() {
            @Override
            public void send(String message) {
                sendReply(message);
            }
        });
        bottomLayou.addView(emot2);

        seeTv = (TextView) findViewById(R.id.notice_see);
        zanIv = (ImageView) findViewById(R.id.notice_zan);
        replyIv = (ImageView) findViewById(R.id.notice_reply);
        zanCountTv = (TextView) findViewById(R.id.notice_zan_count);
        iReplyEt = (TextView) findViewById(R.id.notice_reply_edit);
        replyLayout = (LinearLayout) findViewById(R.id.notice_reply_content);
        showMoreReplyTv = (TextView) findViewById(R.id.notice_reply_more);
        iv_share = (ImageView)findViewById(R.id.iv_share);
        tv_notice_date = (TextView)findViewById(R.id.notice_date);
        //弹出分享
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD

                String content = notice.getData().getMessage();
                if (Utils.isNull(content) == null || content == null) {
                    content = notice.getData().getMessage();
                }

                ShareUtils.showShareDialog(NoticeActivity.this,tv_notice_date,
                        notice.getData().getTitle(),content,"",notice.getShare_url(),true);
=======
                ShareUtils.showShareDialog(NoticeActivity.this,tv_notice_date,
                        notice.getData().getTitle(),"","",notice.getShare_url(),true);
>>>>>>> f35649e243b26297a228b1a38efc35455400c0b0
            }
        });

        seeTv.setText("浏览" + notice.getCount() + "次");

        zanIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (zanLock) {
                    return;
                }
                zanLock = true;
                Drawable drawable = zanIv.getDrawable();
                if (mContext.getResources().getDrawable(R.drawable.interaction_zan_off).getConstantState()
                        .equals(drawable.getConstantState())) {
                    zan();
                } else {
                    cancelZan();
                }
            }
        });

        showMoreReplyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NormalReplyListActivity.class);
                intent.putExtra("replyId", notice.getData().getUuid());
                intent.putExtra("type", NormalReplyListActivity.REPLY_TYPE_SCHOOL_NOTICE);
                mContext.startActivity(intent);
            }
        });

        replyIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.sendEmptyMessageDelayed(1, 200);
            }
        });

        iReplyEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.sendEmptyMessageDelayed(1, 200);
            }
        });

        showMoreReplyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NormalReplyListActivity.class);
                intent.putExtra("type", NormalReplyListActivity.REPLY_TYPE_SCHOOL_NOTICE);
                intent.putExtra("replyId", notice.getData().getUuid());
                startActivity(intent);
            }
        });

        setZanData();
        setReplyData();
    }

    private void setZanData() {
        DianZan dianZan = notice.getData().getDianzan();
        if (dianZan.isCanDianzan()) {
            zanIv.setImageResource(R.drawable.interaction_zan_off);
        } else {
            zanIv.setImageResource(R.drawable.interaction_zan_on);
        }
        if (dianZan != null && dianZan.getCount() > 0) {
            String temp = "<font  color='#ff4966'>" + dianZan.getNames() + "</font>" + "等"
                    + dianZan.getCount() + "人觉得很赞";
            zanCountTv.setText(Html.fromHtml(temp));
        } else {
            zanCountTv.setText("0人觉得很赞");
        }
    }

    private void setReplyData() {
        if (notice.getData().getReplyPage() != null && notice.getData().getReplyPage().getData() != null) {
            List<Reply> replies = notice.getData().getReplyPage().getData();
            addReplyView(replyLayout, replies);

            if (notice.getData().getReplyPage().getTotalCount() > notice.getData().getReplyPage().getPageSize()) {
                showMoreReplyTv.setVisibility(View.VISIBLE);
            } else {
                showMoreReplyTv.setVisibility(View.GONE);
            }
        }
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    bottomLayou.setVisibility(View.VISIBLE);
                    emot2.showSoftKeyboard();
                    break;
            }
        }
    };

    public void hideBottomLayout() {
        emot2.hideFaceLayout();
        bottomLayou.setVisibility(View.GONE);
    }

    private void sendReply(final String message) {
        if (Utils.stringIsNull(message)) {
            Utils.showToast(mContext, "请输入评论内容");
            return;
        }
        showProgressDialog();
        UserRequest.reply(mContext, notice.getData().getUuid(), message, "",
                NormalReplyListActivity.REPLY_TYPE_SCHOOL_NOTICE, new RequestResultI() {
                    @Override
                    public void result(BaseModel domain) {
                        hideProgressDialog();
                        emot2.cleanEditText();
                        bottomLayou.setVisibility(View.GONE);
                        emot2.hideSoftKeyboard();

                        Reply reply = new Reply();
                        reply.setContent(message);
                        reply.setCreate_user(CGApplication.getInstance().getLogin().getUserinfo().getName());

                        addReplyView(replyLayout, reply);
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

    private void getNotice() {
        UserRequest.getNotice(mContext, uuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                notice = (NoticeDetail) domain;

                loadSuc();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                loadFailed();
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(NoticeActivity.this, message);
                }
            }
        });
    }

    private void addReplyView(LinearLayout replyContent, List<Reply> replies) {
        replyContent.removeAllViews();
        if (replies != null && replies.size() > 0) {
            for (Reply reply : replies) {
                if (replyContent.getChildCount() < 5) {
                    View view = View.inflate(mContext, R.layout.item_layout_reply_text, null);
                    TextView nameTv = (TextView) view.findViewById(R.id.item_reply_text_name);
                    String temp = reply.getCreate_user() + ":";
                    SpannableString spanString = new SpannableString(temp);
                    ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#ff4966"));
                    spanString.setSpan(span, 0, temp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    nameTv.setText(spanString);
                    TextView contentTTv = (TextView) view.findViewById(R.id.item_reply_text_content);
                    contentTTv.setText(EmotUtil.getEmotionContent(mContext, reply.getContent()));
                    replyContent.addView(view);
                } else {
                    break;
                }
            }
        }
    }

    private void addReplyView(LinearLayout replyContent, Reply reply) {
        View view = View.inflate(mContext, R.layout.item_layout_reply_text, null);
        TextView nameTv = (TextView) view.findViewById(R.id.item_reply_text_name);
        String temp = reply.getCreate_user() + ":";
        SpannableString spanString = new SpannableString(temp);
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#ff4966"));
        spanString.setSpan(span, 0, temp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        nameTv.setText(spanString);
        TextView contentTTv = (TextView) view.findViewById(R.id.item_reply_text_content);
        contentTTv.setText(EmotUtil.getEmotionContent(mContext, reply.getContent()));
        replyContent.addView(view, 0);

        if (replyContent.getChildCount() > 5) {
            replyContent.removeViewAt(5);
        }
    }

    private void zan() {
        UserRequest.zan(mContext, notice.getData().getUuid(), NormalReplyListActivity.REPLY_TYPE_SCHOOL_NOTICE
                , new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                zanIv.setImageResource(R.drawable.interaction_zan_on);
                if (Utils.stringIsNull(notice.getData().getDianzan().getNames())) {
                    notice.getData().getDianzan().setNames(CGApplication.getInstance().getLogin().getUserinfo().getName());
                } else {
                    notice.getData().getDianzan().setNames(notice.getData().getDianzan().getNames()
                            + "," + CGApplication.getInstance().getLogin().getUserinfo().getName());
                }
                notice.getData().getDianzan().setCount(notice.getData().getDianzan().getCount() + 1);
                notice.getData().getDianzan().setCanDianzan(false);
                zanLock = false;

                setZanData();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                zanLock = false;
                Utils.showToast(mContext, "点赞失败");
            }
        });
    }

    private void cancelZan() {
        UserRequest.zanCancel(mContext, notice.getData().getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                zanIv.setImageResource(R.drawable.interaction_zan_off);
                if (notice.getData().getDianzan().getNames().contains("," +
                        CGApplication.getInstance().getLogin().getUserinfo().getName())) {
                    notice.getData().getDianzan().setNames(notice.getData().getDianzan().getNames().replace(","
                            + CGApplication.getInstance().getLogin().getUserinfo().getName(), ""));
                } else if (notice.getData().getDianzan().getNames().contains(
                        CGApplication.getInstance().getLogin().getUserinfo().getName() + ",")) {
                    notice.getData().getDianzan().setNames(notice.getData().getDianzan().getNames().replace(
                            CGApplication.getInstance().getLogin().getUserinfo().getName() + ",", ""));
                } else if (notice.getData().getDianzan().getNames().contains(CGApplication.getInstance().getLogin().getUserinfo().getName())) {
                    notice.getData().getDianzan().setNames(notice.getData().getDianzan().getNames()
                            .replace(CGApplication.getInstance().getLogin().getUserinfo().getName(), ""));
                }
                notice.getData().getDianzan().setCount(notice.getData().getDianzan().getCount() - 1);
                notice.getData().getDianzan().setCanDianzan(true);
                zanLock = false;

                setZanData();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                zanLock = false;
                Utils.showToast(mContext, "取消点赞失败");
            }
        });
    }
}
