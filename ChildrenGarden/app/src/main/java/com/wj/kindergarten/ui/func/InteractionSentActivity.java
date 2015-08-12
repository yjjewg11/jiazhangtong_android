package com.wj.kindergarten.ui.func;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.ChildInfo;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.Utils;

import java.util.List;

/**
 * InteractionSentActivity
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/26 21:28
 */
public class InteractionSentActivity extends BaseActivity {
    private EditText editText;
    private ImageView addIv;
    private String newUUID = "";


    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_interaction_send;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        editText = (EditText) findViewById(R.id.interaction_send_edit);
        addIv = (ImageView) findViewById(R.id.interaction_send_add);
        addIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        if (getIntent().hasExtra("uuid")) {
            setTitleText("发表回复", "发表");
            addIv.setVisibility(View.GONE);
            newUUID = getIntent().getStringExtra("uuid");
        } else {
            setTitleText("发表互动", "发表");
        }
    }

    @Override
    protected void titleRightButtonListener() {
        String content = "";
        content = editText.getText().toString();
        CGApplication cgApplication = (CGApplication) CGApplication.getInstance();
        ChildInfo childInfo = null;
        if (cgApplication.getLogin() != null && cgApplication.getLogin().getList() != null &&
                cgApplication.getLogin().getList().size() > 0) {
            childInfo = cgApplication.getLogin().getList().get(0);
        }
        String cu = "";
        if (childInfo != null) {
            cu = childInfo.getClassuuid();
        }
        if (Utils.stringIsNull(newUUID)) {
            sendInteraction(cu, content);
        } else {
            sendReply(cu, content);
        }
    }

    private void sendInteraction(String uuid, String content) {
        UserRequest.sendInteraction(mContext, "标题", uuid, "", content, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                Utils.showToast(mContext, domain.getResMsg().getMessage());
                finish();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                Utils.showToast(mContext, message);
            }
        });
    }

    private void sendReply(String uuid, String content) {
        UserRequest.reply(mContext, newUUID, content, uuid, 1, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                Utils.showToast(mContext, domain.getResMsg().getMessage());
                finish();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                Utils.showToast(mContext, message);
            }
        });
    }
}
