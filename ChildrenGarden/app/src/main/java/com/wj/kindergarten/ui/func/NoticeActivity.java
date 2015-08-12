package com.wj.kindergarten.ui.func;

import android.text.Html;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.NoticeDetail;
import com.wj.kindergarten.bean.Reply;
import com.wj.kindergarten.bean.ReplyList;
import com.wj.kindergarten.bean.ZanItem;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;

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

    private NoticeDetail notice;
    private String uuid = "";


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

        getZanList();
        getReplyList();
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
        replyIv = (ImageView) findViewById(R.id.notice_reply);
        replyLayout = (LinearLayout) findViewById(R.id.notice_reply_content);

        titleTv.setText(notice.getData().getTitle());
        contentTv.setText(notice.getData().getMessage());
        orgTv.setText(notice.getData().getGroupuuid());
        dateTv.setText(notice.getData().getCreate_time());
        seeTv.setText("浏览" + notice.getCount() + "次");
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
        if (replies != null && replies.size() > 0) {
            replyContent.removeAllViews();
            for (Reply reply : replies) {
                TextView textView = new TextView(mContext);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(layoutParams);
                textView.setText(reply.getCreate_user() + ":" + Html.fromHtml(reply.getContent()));
                replyContent.addView(textView);
            }
        }
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
