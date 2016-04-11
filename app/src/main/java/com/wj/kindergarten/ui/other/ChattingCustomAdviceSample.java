package com.wj.kindergarten.ui.other;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.mobileim.aop.BaseAdvice;
import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMChattingPageUI;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.wenjie.jiazhangtong.R;


/**
 * Created by tanghongbin on 16/4/11.
 */
public class ChattingCustomAdviceSample extends IMChattingPageUI {
    public View getCustomTitleView(final Fragment fragment, final Context context, LayoutInflater inflater, YWConversation conversation) {
        YWConversationType type = conversation.getConversationType();

        if (type == YWConversationType.SHOP) {
            View cusView = inflater.inflate(R.layout.my_default_chatting_title, null);
            TextView title = (TextView) cusView.findViewById(R.id.my_title);
            TextView aliwx_title_button = (TextView) cusView.findViewById(R.id.my_back);
            aliwx_title_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.getActivity().finish();
                }
            });
            title.setText("问界科技客服");
            return cusView;
        }
        return super.getCustomTitleView(fragment, context, inflater, conversation);
    }

    public ChattingCustomAdviceSample(Pointcut pointcut) {
        super(pointcut);
    }
}
