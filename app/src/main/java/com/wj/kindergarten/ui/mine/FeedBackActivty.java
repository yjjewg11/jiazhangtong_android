package com.wj.kindergarten.ui.mine;

import android.provider.Telephony;
import android.widget.EditText;

import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.SyncListener;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.Reply;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.Utils;

import java.util.List;

/**
 * FeedBackActivty
 *
 * @Description:意见反馈
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-13 23:59
 */
public class FeedBackActivty extends BaseActivity {
    private EditText editText = null;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_feed_back;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        setTitleText("意见反馈", "提交");
        editText = (EditText) findViewById(R.id.et_advise);
    }

    @Override
    protected void titleRightButtonListener() {
        if (!Utils.stringIsNull(editText.getText().toString().trim())) {
            advise();
        } else {
            Utils.showToast(FeedBackActivty.this, "意见不能为空");
        }
    }

    private void advise() {
        FeedbackAgent agent = new FeedbackAgent(FeedBackActivty.this);
        Conversation conversation = agent.getDefaultConversation();
        conversation.addUserReply(editText.getText().toString());
        sync(conversation);
    }

    // 数据同步
    private void sync(Conversation mComversation) {
        final HintInfoDialog dialog = new HintInfoDialog(FeedBackActivty.this,"反馈信息提交中，请稍后...");
        dialog.show();
        mComversation.sync(new SyncListener() {
            @Override
            public void onSendUserReply(List<Reply> replyList) {
                for (Reply reply : replyList) {
                    //通过判断该replyList中reply的status字段是否为Reply.STATUS_SENT来确认是否发送成功
                    if (Reply.STATUS_SENT.equals(reply.status)) {
                        dialog.dismiss();
                        Utils.showToast(FeedBackActivty.this,"反馈信息提交成功");
                        finish();
                        //发送成功
                    } else {
                        Utils.showToast(FeedBackActivty.this,"反馈信息提交失败");
                        dialog.dismiss();
                        //发送失败
                    }
                }
            }

            @Override
            public void onReceiveDevReply(List<Reply> replyList) {
                // 如果开发者没有新的回复数据，则返回
                if (replyList == null || replyList.size() < 1) {
                    return;
                } else {
                    //更新UI
                }
            }
        });
    }
}
