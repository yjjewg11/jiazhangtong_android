package com.wj.kindergarten.ui.func;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Course;
import com.wj.kindergarten.bean.CourseList;
import com.wj.kindergarten.bean.DianZan;
import com.wj.kindergarten.bean.Reply;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.utils.URLImageParser;
import com.wj.kindergarten.utils.Utils;

import java.util.List;

/**
 * CourseFragment
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/16 13:23
 */
public class CourseFragment extends Fragment {
    private LinearLayout rootView;

    private Context mContext;
    private String date;
    private boolean isZanDoing = false;
    private List<Course> courses;
    private SparseArray<TextView> allDaySA = new SparseArray<>();
    private Course currentCourse;

    public static CourseFragment buildCourseFragment(String date) {
        CourseFragment courseFragment = new CourseFragment();
        Bundle bundle = new Bundle();
        bundle.putString("date", date);
        courseFragment.setArguments(bundle);
        return courseFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date = getArguments().getString("date");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        rootView = (LinearLayout) View.inflate(mContext, R.layout.item_course_list, null);
        return rootView;
    }

    private void initZanReplyViews(View view) {
        TextView viewCountTv;
        final ImageView zanIv;
        ImageView replyIv;
        TextView zanCountTv;
        final EditText iReplyEt;
        TextView sendTv;
        TextView replyMoreTv;
        final LinearLayout replyLayout;
        LinearLayout replyRootView;
        viewCountTv = (TextView) view.findViewById(R.id.notice_see);
        zanIv = (ImageView) view.findViewById(R.id.notice_zan);
        replyIv = (ImageView) view.findViewById(R.id.notice_reply);
        zanCountTv = (TextView) view.findViewById(R.id.notice_zan_count);
        iReplyEt = (EditText) view.findViewById(R.id.notice_reply_edit);
        sendTv = (TextView) view.findViewById(R.id.notice_reply_send);
        replyMoreTv = (TextView) view.findViewById(R.id.notice_reply_more);
        replyLayout = (LinearLayout) view.findViewById(R.id.notice_reply_content);
        replyRootView = (LinearLayout) view.findViewById(R.id.layout_common_reply);

        replyRootView.setVisibility(View.GONE);
        viewCountTv.setVisibility(View.GONE);

        replyRootView.setVisibility(View.VISIBLE);
        if (currentCourse.getDianzan() != null) {
            DianZan dianZan = currentCourse.getDianzan();
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
        }

        if (currentCourse.getReplyPage() != null) {
            if (currentCourse.getReplyPage() != null && currentCourse.getReplyPage().getData() != null) {
                List<Reply> replies = currentCourse.getReplyPage().getData();
                addReplyView(replyLayout, replies);

                if (currentCourse.getReplyPage().getTotalCount() > currentCourse.getReplyPage().getPageSize()) {
                    replyMoreTv.setVisibility(View.VISIBLE);
                }
            }
        }

        zanIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isZanDoing) {
                    return;
                }
                Drawable drawable = zanIv.getDrawable();
                if (getResources().getDrawable(R.drawable.interaction_zan_off).getConstantState()
                        .equals(drawable.getConstantState())) {
                    zan(zanIv);
                } else {
                    cancelZan(zanIv);
                }
            }
        });
        replyIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                message.obj = iReplyEt;
                message.what = 1;
                mHandler.sendMessageDelayed(message, 200);
            }
        });
        replyMoreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NormalReplyListActivity.class);
                intent.putExtra("replyId", currentCourse.getUuid());
                intent.putExtra("type",NormalReplyListActivity.REPLY_TYPE_COURSE);
                startActivity(intent);
            }
        });

        sendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReply(replyLayout, iReplyEt);
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    EditText iReplyEtTemp = (EditText) msg.obj;
                    iReplyEtTemp.requestFocus();
                    iReplyEtTemp.setSelection(iReplyEtTemp.getText().length());
                    Utils.inputMethod(mContext, true, iReplyEtTemp);
                    break;
            }
        }
    };

    private void setCourse(View view, final List<Course> courses) {
        if (courses == null || courses.size() <= 0) {
            return;
        }

        initZanReplyViews(view);

        TextView mondayTv = (TextView) view.findViewById(R.id.course_monday);
        TextView tuesdayTv = (TextView) view.findViewById(R.id.course_tuesday);
        TextView wednesdayTv = (TextView) view.findViewById(R.id.course_wednesday);
        TextView thursdayTv = (TextView) view.findViewById(R.id.course_thursday);
        TextView fridayTv = (TextView) view.findViewById(R.id.course_friday);
        final TextView morningTv = (TextView) view.findViewById(R.id.course_morning);
        final TextView afternoonTv = (TextView) view.findViewById(R.id.course_afternoon);

        morningTv.setText(currentCourse.getMorning());
        afternoonTv.setText(currentCourse.getAfternoon());

        mondayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentCourse = courses.size() > 0 ? courses.get(0) : null;
                if (currentCourse == null) {
                    morningTv.setText("");
                    afternoonTv.setText("");
                } else {
                    morningTv.setText(currentCourse.getMorning());
                    afternoonTv.setText(currentCourse.getAfternoon());
                }
            }
        });


        tuesdayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentCourse = courses.size() > 1 ? courses.get(1) : null;
                if (currentCourse == null) {
                    morningTv.setText("");
                    afternoonTv.setText("");
                } else {
                    morningTv.setText(currentCourse.getMorning());
                    afternoonTv.setText(currentCourse.getAfternoon());
                }
            }
        });


        wednesdayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentCourse = courses.size() > 2 ? courses.get(2) : null;
                if (currentCourse == null) {
                    morningTv.setText("");
                    afternoonTv.setText("");
                } else {
                    morningTv.setText(currentCourse.getMorning());
                    afternoonTv.setText(currentCourse.getAfternoon());
                }
            }
        });


        thursdayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentCourse = courses.size() > 3 ? courses.get(3) : null;
                if (currentCourse == null) {
                    morningTv.setText("");
                    afternoonTv.setText("");
                } else {
                    morningTv.setText(currentCourse.getMorning());
                    afternoonTv.setText(currentCourse.getAfternoon());
                }
            }
        });


        fridayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentCourse = courses.size() > 4 ? courses.get(4) : null;
                if (currentCourse == null) {
                    morningTv.setText("");
                    afternoonTv.setText("");
                } else {
                    morningTv.setText(currentCourse.getMorning());
                    afternoonTv.setText(currentCourse.getAfternoon());
                }
            }
        });
    }

    private void initDate() {
        if (currentCourse == null) {
            return;
        }

        setCourse(rootView, courses);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && date != null) {
//            getCourseList();
        }
    }

    private void showProgressDialog() {
        if (mContext != null) {
            ((CourseListActivity) mContext).showProgressDialog();
        }
    }

    private void hideProgressDialog() {
        if (mContext != null) {
            ((CourseListActivity) mContext).hideProgressDialog();
        }
    }

    private void getCourseList() {
        showProgressDialog();
        String[] startEndDay = date.split("~");
        String uuid = "";
        if (CGApplication.getInstance().getLogin() != null && CGApplication.getInstance().getLogin().getList() != null
                && CGApplication.getInstance().getLogin().getList().size() > 0) {
            uuid = CGApplication.getInstance().getLogin().getList().get(0).getClassuuid();
        }
        UserRequest.getCourseList(mContext, startEndDay[0], startEndDay[1], uuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                hideProgressDialog();
                CourseList courseList = (CourseList) domain;
                if (courseList != null && courseList.getList() != null && courseList.getList().size() > 0) {
                    courses = courseList.getList();
                    currentCourse = courses != null && courses.size() > 0 ? courses.get(0) : null;
                } else {

                }

                initDate();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                hideProgressDialog();

            }
        });
    }

    private void zan(final ImageView zanIv) {
        isZanDoing = true;
        UserRequest.zan(mContext, currentCourse.getUuid(), NormalReplyListActivity.REPLY_TYPE_COURSE, new RequestResultI() {
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

    private void cancelZan(final ImageView zanIv) {
        isZanDoing = true;
        UserRequest.zanCancel(mContext, currentCourse.getUuid(), new RequestResultI() {
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

    private void sendReply(final LinearLayout replyLayout, final EditText iReplyEt) {
        UserRequest.reply(mContext, currentCourse.getUuid(), iReplyEt.getText().toString().trim(), "", 0, new RequestResultI() {
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
}
