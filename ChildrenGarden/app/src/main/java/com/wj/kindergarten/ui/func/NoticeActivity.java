package com.wj.kindergarten.ui.func;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.DianZan;
import com.wj.kindergarten.bean.NoticeDetail;
import com.wj.kindergarten.bean.Reply;
import com.wj.kindergarten.bean.ReplyList;
import com.wj.kindergarten.bean.ZanItem;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.URLImageParser;
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
    private TextView contentTv;
    private TextView orgTv;
    private TextView dateTv;
    private TextView seeTv;
    private TextView zanCountTv;
    private EditText iReplyEt;
    private ImageView zanIv;
    private ImageView replyIv;
    private LinearLayout replyLayout;
    private TextView showMoreReplyTv;
    private TextView sendTv;

    private NoticeDetail notice;
    private String uuid = "";
    private boolean isZanDoing = false;


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

    @Override
    protected void onCreate() {
        setTitleText("公告详情");

        initViews();

//        getZanList();
//        getReplyList();
    }

    private void initViews() {
        titleTv = (TextView) findViewById(R.id.notice_title);
        contentTv = (TextView) findViewById(R.id.notice_content);
        orgTv = (TextView) findViewById(R.id.notice_org);
        dateTv = (TextView) findViewById(R.id.notice_date);
        seeTv = (TextView) findViewById(R.id.notice_see);
        zanCountTv = (TextView) findViewById(R.id.notice_zan_count);
        iReplyEt = (EditText) findViewById(R.id.notice_reply_edit);
        zanIv = (ImageView) findViewById(R.id.notice_zan);
        zanIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isZanDoing) {
                    return;
                }
                Drawable drawable = zanIv.getDrawable();
                if (getResources().getDrawable(R.drawable.interaction_zan_off).getConstantState()
                        .equals(drawable.getConstantState())) {
                    zan();
                } else {
                    cancelZan();
                }
            }
        });
        replyIv = (ImageView) findViewById(R.id.notice_reply);
        replyIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.sendEmptyMessageDelayed(1, 200);
            }
        });
        replyLayout = (LinearLayout) findViewById(R.id.notice_reply_content);
        showMoreReplyTv = (TextView) findViewById(R.id.notice_reply_more);
        showMoreReplyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NormalReplyListActivity.class);
                intent.putExtra("replyId", notice.getData().getUuid());
                startActivity(intent);
            }
        });

        titleTv.setText(notice.getData().getTitle());
        contentTv.setText(Html.fromHtml(notice.getData().getMessage(), new URLImageParser(contentTv, mContext), null));
        orgTv.setText(Utils.getGroupNameFromId(notice.getData().getGroupuuid()));
        dateTv.setText(notice.getData().getCreate_time());
        seeTv.setText("浏览" + notice.getCount() + "次");
        sendTv = (TextView) findViewById(R.id.notice_reply_send);
        sendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReply();
            }
        });

        if (notice.getData() == null) {
            return;
        }
        DianZan dianZan = notice.getData().getDianzan();
        if (dianZan.isCanDianzan()) {
            zanIv.setImageResource(R.drawable.interaction_zan_off);
        } else {
            zanIv.setImageResource(R.drawable.interaction_zan_on);
        }
        if (dianZan != null && dianZan.getCount() > 0) {
            zanCountTv.setText(dianZan.getNames() + "等" + dianZan.getCount() + "人觉得很赞");
        } else {
            zanCountTv.setText("0人觉得很赞");
        }

        if (notice.getData().getReplyPage() != null && notice.getData().getReplyPage().getData() != null) {
            List<Reply> replies = notice.getData().getReplyPage().getData();
            addReplyView(replyLayout, replies);

            if (notice.getData().getReplyPage().getTotalCount() > notice.getData().getReplyPage().getPageNo()
                    * notice.getData().getReplyPage().getPageSize()) {
                showMoreReplyTv.setVisibility(View.VISIBLE);
            }
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    iReplyEt.requestFocus();
                    iReplyEt.setSelection(iReplyEt.getText().length());
                    Utils.inputMethod(mContext, true, iReplyEt);
                    break;
            }
        }
    };

    private void sendReply() {
        UserRequest.reply(mContext, notice.getData().getUuid(), iReplyEt.getText().toString().trim(), "", 0, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                Reply reply = new Reply();
                reply.setContent(iReplyEt.getText().toString().trim());
                reply.setCreate_user("我");
                iReplyEt.setText("");
                addReplyView(replyLayout, reply);
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

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

            }
        });
    }

    private void addReplyView(LinearLayout replyContent, List<Reply> replies) {
        replyContent.removeAllViews();
        if (replies != null && replies.size() > 0) {
            for (Reply reply : replies) {
                TextView textView = new TextView(mContext);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(layoutParams);
                String info = "<font  color='#ff4966'>" + reply.getCreate_user() + ":</font>"
                        + reply.getContent().trim().replace("<div>", "").replace("</div>", "");
                textView.setText(Html.fromHtml(info, new URLImageParser(textView, mContext), null), TextView.BufferType.SPANNABLE);
                textView.setMovementMethod(LinkMovementMethod.getInstance());
                replyContent.addView(textView);
            }
        }
    }

    private void addReplyView(LinearLayout replyContent, Reply reply) {
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);
        String info = "<font  color='#ff4966'>" + reply.getCreate_user() + ":</font>"
                + reply.getContent().trim();
        textView.setText(Html.fromHtml(info, new URLImageParser(textView, mContext), null), TextView.BufferType.SPANNABLE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        replyContent.addView(textView, 0);
    }

    private void zan() {
        isZanDoing = true;
        UserRequest.zan(mContext, notice.getData().getUuid(), "0", new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                isZanDoing = false;
                zanIv.setImageResource(R.drawable.interaction_zan_on);
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                isZanDoing = false;

            }
        });
    }

    private void cancelZan() {
        isZanDoing = true;
        UserRequest.zanCancel(mContext, notice.getData().getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                isZanDoing = false;
                zanIv.setImageResource(R.drawable.interaction_zan_off);
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                isZanDoing = false;

            }
        });
    }

    private void getReplyList() {
        UserRequest.getReplyList(mContext, notice.getData().getUuid(), 1, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                ReplyList replyList = (ReplyList) domain;
                addReplyView(replyLayout, replyList.getList().getData());
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    private void getZanList() {
        UserRequest.getZanList(mContext, notice.getData().getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                ZanItem zanItem = (ZanItem) domain;
                if (zanItem.getCount() > 0) {
                    zanCountTv.setText(zanItem.getNames() + "等" + zanItem.getCount() + "人觉得很赞");
                } else {
                    zanCountTv.setText("0人觉得很赞");
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
