package com.wj.kindergarten.ui.addressbook;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.AddressBookMessage;
import com.wj.kindergarten.bean.AddressBookMsg;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Login;
import com.wj.kindergarten.bean.MyMessage;
import com.wj.kindergarten.bean.Teacher;
import com.wj.kindergarten.bean.TeacherInfo;
import com.wj.kindergarten.bean.UserInfo;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.AddressBookRequest;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.addressbook.scroll.MyScrollView;
import com.wj.kindergarten.ui.emot.EmotUtil;
import com.wj.kindergarten.ui.emot.SendMessage;
import com.wj.kindergarten.ui.emot.ViewEmot;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * LeaderMessageActivty
 *
 * @Description:老师消息
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-12 20:07
 */
public class TeacherMessageActivty extends BaseActivity {

    private LinearLayout layoutMessage = null;
    private Teacher teacher = null;
    private MyScrollView scrollView = null;
    private ArrayList<MyMessage> myMessages = new ArrayList<MyMessage>();
    private UserInfo userInfo = null;
    private LinearLayout layoutFaces = null;
    private ViewEmot viewEmot = null;
    private boolean sendSuccess = false;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_address_book_msg;
    }

    @Override
    protected void setNeedLoading() {
        isNeedLoading = true;
    }

    @Override
    protected void loadData() {
        teacher = (Teacher) getIntent().getSerializableExtra("teacher");
        setTitleText(teacher.getName());
        if (teacher.isFormMessage()) {
            queryTeacherInfo();
        }
        queryMessage();
    }

    @Override
    protected void onCreate() {
        setTitleText(teacher.getName());
        layoutMessage = (LinearLayout) findViewById(R.id.layout_message);
        scrollView = (MyScrollView) findViewById(R.id.msv_message);
        layoutFaces = (LinearLayout) findViewById(R.id.layout_ico);
        scrollView.setRankScrollInterface(new RankScrollInterfaceImpl());
        viewEmot = new ViewEmot(this, new SendMessageImpl());
        layoutFaces.addView(viewEmot);

        Login login = CGApplication.getInstance().getLogin();
        if (null != login) {
            userInfo = login.getUserinfo();
        } else {
            userInfo = new UserInfo();
        }


        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                viewEmot.hideFaceLayout();
                return false;
            }
        });

    }

    private void queryTeacherInfo() {
        UserRequest.queryTeacherInfo(TeacherMessageActivty.this, teacher.getTeacher_uuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                TeacherInfo userInfo = (TeacherInfo) domain;
                if (null != userInfo && userInfo.getData() != null) {
                    teacher.setName(userInfo.getData().getName());
                    setTitleText(userInfo.getData().getName());
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


    //请求老师 的消息
    private void queryMessage() {
        AddressBookRequest.getTeacherMessage(TeacherMessageActivty.this, teacher.getTeacher_uuid(), 1, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                AddressBookMessage bookMessage = (AddressBookMessage) domain;
                if (null != bookMessage) {
                    AddressBookMsg bookMsg = bookMessage.getList();
                    ArrayList<MyMessage> messages = bookMsg.getData();
                    if (null != messages && messages.size() > 0) {
                        myMessages.clear();
                        myMessages.addAll(messages);
                        handler.sendEmptyMessage(2);
                    }
                }
                loadSuc();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(TeacherMessageActivty.this, message);
                }
                loadFailed();
            }
        });
    }

    private void success() {
        if (null == userInfo) {
            userInfo = new UserInfo();
        }
        for (MyMessage mm : myMessages) {
            if (null != mm && mm.getSend_useruuid().equals(userInfo.getUuid())) {
                addRight(mm, userInfo.getImg());
            } else {
                addLeft(mm);
            }
        }
    }

    private void addRight(MyMessage m, String headImg) {
        if (null != m) {
            View view = LayoutInflater.from(TeacherMessageActivty.this).inflate(R.layout.view_my_message, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_my_message);
            CircleImage head = (CircleImage) view.findViewById(R.id.iv_my_photo);
            ImageLoaderUtil.displayImage(headImg, head);
            ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.send_message_progress);
            progressBar.setVisibility(View.GONE);
            ImageView falure = (ImageView) view.findViewById(R.id.iv_send_failure);
            falure.setVisibility(View.GONE);
            textView.setText(EmotUtil.getEmotionContent(TeacherMessageActivty.this, m.getMessage()));
            layoutMessage.addView(view);
        }
    }

    private void addLeft(MyMessage m) {
        if (null != m) {
            View view = LayoutInflater.from(TeacherMessageActivty.this).inflate(R.layout.view_teacher_message, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_teacher_message);
            CircleImage head = (CircleImage) view.findViewById(R.id.iv_teacher_photo);
            ImageLoaderUtil.displayImage(teacher.getImg(), head);
            textView.setText(EmotUtil.getEmotionContent(TeacherMessageActivty.this, m.getMessage()));
            layoutMessage.addView(view);
        }
    }


    /**
     * 我发送消息
     *
     * @param imageView
     * @param bar
     * @param textView
     */
    private void sendMessage(String msg, final ImageView imageView, final ProgressBar bar, final TextView textView) {
        String message = "";
        if (!Utils.stringIsNull(msg)) {
            message = EmotUtil.getEmotNames(msg);
        }

        AddressBookRequest.sendMessageToTeacher(TeacherMessageActivty.this, teacher.getTeacher_uuid(), message, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                bar.setVisibility(View.GONE);
                handler.sendEmptyMessage(3);
                sendSuccess = true;
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(TeacherMessageActivty.this, message);
                }

                imageView.setVisibility(View.VISIBLE);
                bar.setVisibility(View.GONE);
                sendSuccess = false;
            }
        });
    }

    /**
     * 显示消息
     */
    private void addMessage(final String msg) {
        sendSuccess = false;
        View view = LayoutInflater.from(TeacherMessageActivty.this).inflate(R.layout.view_my_message, null);
        final TextView textView = (TextView) view.findViewById(R.id.tv_my_message);
        ImageView head = (ImageView) view.findViewById(R.id.iv_my_photo);
        if (null != userInfo && !Utils.stringIsNull(userInfo.getImg())) {
            ImageLoaderUtil.displayImage(userInfo.getImg(), head);
        }
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.send_message_progress);
        final ImageView falure = (ImageView) view.findViewById(R.id.iv_send_failure);
        textView.setText(EmotUtil.getEmotionContent(TeacherMessageActivty.this, msg));
        layoutMessage.addView(view);
        sendMessage(msg, falure, progressBar, textView);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sendSuccess) {
                    progressBar.setVisibility(View.VISIBLE);
                    falure.setVisibility(View.GONE);
                    sendMessage(msg, falure, progressBar, textView);
                }
            }
        });

        handler.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                sort(myMessages);
                success();
                return;
            } else if (msg.what == 3) {
                viewEmot.cleanEditText();
                return;
            }
        }
    };

    //排序
    private List<MyMessage> sort(List<MyMessage> list) {
        if (list != null && list.size() > 1) {
            CompareMessage compareMessage = new CompareMessage();
            Collections.sort(list, compareMessage);
        }
        return list;
    }

    //发送消息
    private class SendMessageImpl implements SendMessage {

        @Override
        public void send(String message) {
            addMessage(message);
        }
    }


    private class RankScrollInterfaceImpl implements MyScrollView.RankScrollInterface {

        @Override
        public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldX, int oldY) {

        }

        @Override
        public void onScrollMove(int my) {

        }

        @Override
        public void onScrollBottom(MyScrollView scrollView, int x, int y, int oldX, int oldY) {

        }
    }
}
