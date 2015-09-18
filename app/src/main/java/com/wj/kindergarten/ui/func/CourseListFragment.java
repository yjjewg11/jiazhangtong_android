package com.wj.kindergarten.ui.func;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.ChildInfo;
import com.wj.kindergarten.bean.Course;
import com.wj.kindergarten.bean.CourseList;
import com.wj.kindergarten.bean.Reply;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.emot.SendMessage;
import com.wj.kindergarten.ui.emot.ViewEmot2;
import com.wj.kindergarten.ui.func.adapter.CourseListAdapter;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CourseListFragment
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/20 10:31
 */
public class CourseListFragment extends Fragment {
    private TextView dateTv;
    private ListView mListView;
    private LinearLayout bottomLayou;
    private ViewEmot2 emot2 = null;

    private UserCourse userCourse = null;
    private Context mContext;
    private String date;
    private CourseListAdapter courseListAdapter;
    private List<UserCourse> courseAll = new ArrayList<>();
    private String nowReplyUUID = "";
    private int weekIndex = 0;
    private int netCount = 0;

    public static CourseListFragment buildCourseFragment(String date) {
        CourseListFragment courseListFragment = new CourseListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("date", date);
        courseListFragment.setArguments(bundle);
        return courseListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date = getArguments().getString("date");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        courseAll.clear();
        View view = View.inflate(getActivity(), R.layout.fragment_food_page, null);
        dateTv = (TextView) view.findViewById(R.id.course_date);
        dateTv.setText(date);
        mListView = (ListView) view.findViewById(R.id.normal_list);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                hideBottomLayout();
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
        courseListAdapter = new CourseListAdapter(getActivity(), this, date);
        mListView.setAdapter(courseListAdapter);

        bottomLayou = (LinearLayout) view.findViewById(R.id.food_bottom);
        emot2 = new ViewEmot2(getActivity(), new SendMessage() {
            @Override
            public void send(String message) {
                sendReply(nowReplyUUID, message);
            }
        });
        bottomLayou.addView(emot2);
        netCount = 0;

        if (CGApplication.getInstance().getChildInfoMap() != null
                && CGApplication.getInstance().getChildInfoMap().size() > 0) {
            netCount = CGApplication.getInstance().getChildInfoMap().size();
            getCourseOfChildren();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser && mContext != null) {
//            foods.clear();
//            if (CGApplication.getInstance().getGroupMap() != null && CGApplication.getInstance().getGroupMap().size() > 0) {
//                netCount = CGApplication.getInstance().getGroupMap().size();
//                getFoodOfChildren();
//            }
//        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    courseListAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    public void showReplyLayout(String uuid, int weekIndex, UserCourse userCourse) {
        nowReplyUUID = uuid;
        this.weekIndex = weekIndex;
        bottomLayou.setVisibility(View.VISIBLE);
        emot2.showSoftKeyboard();
        this.userCourse = userCourse;
    }

    public void hideBottomLayout() {
        emot2.hideFaceLayout();
        bottomLayou.setVisibility(View.GONE);
    }

    private void showProgressDialog() {
        try {
            ((CourseListActivity) mContext).setProgressDialogCancelable(false);
            ((CourseListActivity) mContext).showProgressDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideProgressDialog() {
        netCount--;
        try {
            if (netCount <= 0) {
                ((CourseListActivity) mContext).hideProgressDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCourseOfChildren() {
        showProgressDialog();
        for (Map.Entry entry : CGApplication.getInstance().getChildInfoMap().entrySet()) {
            UserCourse userCourse = new UserCourse();
            userCourse.setChildInfo((ChildInfo) entry.getValue());
            courseAll.add(userCourse);
            final String classUUID = (String) entry.getKey();
            String[] startEndDay = date.split("~");

            UserRequest.getCourseList(mContext, startEndDay[0], startEndDay[1], classUUID, new RequestResultI() {
                @Override
                public void result(BaseModel domain) {
                    if (isDetached()) {
                        return;
                    }
                    hideProgressDialog();
                    CourseList courseList = (CourseList) domain;
                    if (courseList != null && courseList.getList() != null && courseList.getList().size() > 0) {
                        List<Course> currentCourses = courseList.getList();
                        for (int i = 0; i < courseAll.size(); i++) {
                            ChildInfo childInfo = courseAll.get(i).getChildInfo();
                            if (currentCourses.get(0).getClassuuid().equals(childInfo.getClassuuid())) {
                                courseAll.get(i).setCourses(currentCourses);
                                mHandler.sendEmptyMessage(0);
                                break;
                            }
                        }
                    } else {

                    }
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

        courseListAdapter.setCourseList(courseAll);
    }

    private void sendReply(String uuid, final String replyContent) {
        if (Utils.stringIsNull(replyContent)) {
            Utils.showToast(getActivity(), "请输入内容");
            return;
        }
        ((CourseListActivity) getActivity()).showProgressDialog("发表回复中，请稍候...");
        UserRequest.reply(getActivity(), uuid, replyContent.trim(), "",
                NormalReplyListActivity.REPLY_TYPE_FOOD, new RequestResultI() {
                    @Override
                    public void result(BaseModel domain) {
                        try {
                            ((CourseListActivity) getActivity()).hideProgressDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        emot2.cleanEditText();
                        bottomLayou.setVisibility(View.GONE);
                        emot2.hideSoftKeyboard();

                        Reply reply = new Reply();
                        reply.setContent(replyContent.trim());
                        reply.setCreate_user(CGApplication.getInstance().getLogin().getUserinfo().getName());
                        courseListAdapter.addReply(userCourse, weekIndex, reply);
                    }

                    @Override
                    public void result(List<BaseModel> domains, int total) {

                    }

                    @Override
                    public void failure(String message) {
                        try {
                            ((CourseListActivity) getActivity()).hideProgressDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Utils.showToast(CGApplication.getInstance(), message);
                    }
                });
    }

    public class UserCourse {
        private List<Course> courses;
        private ChildInfo childInfo;

        public List<Course> getCourses() {
            return courses;
        }

        public void setCourses(List<Course> courses) {
            this.courses = courses;
        }

        public ChildInfo getChildInfo() {
            return childInfo;
        }

        public void setChildInfo(ChildInfo childInfo) {
            this.childInfo = childInfo;
        }
    }
}
