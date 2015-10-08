package com.wj.kindergarten.ui.func;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.wj.kindergarten.bean.MyTrainCoures;
import com.wj.kindergarten.bean.Reply;
import com.wj.kindergarten.bean.TrainChildInfo;
import com.wj.kindergarten.bean.TrainClass;
import com.wj.kindergarten.bean.TrainCourse;
import com.wj.kindergarten.bean.TrainCourseContent;
import com.wj.kindergarten.bean.ZanItem;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.emot.SendMessage;
import com.wj.kindergarten.ui.emot.ViewEmot2;
import com.wj.kindergarten.ui.func.adapter.CourseListAdapter;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
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

    public List<TrainCourse> trainCourseAll  = new ArrayList<>();

    private List<ZanItem> zanItemList = new ArrayList<>();

    private String nowReplyUUID = "";
    private int weekIndex = 0;
    private int netCount = 0;
    private ViewEmot2 myViewEmot2 = null;

    private List<TrainChildInfo> trainChildInfoList ;
    private List<TrainClass> lists;

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
        zanItemList.clear();
        trainCourseAll.clear();
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
                sendReply(nowReplyUUID, message,emot2);
            }
        });
        bottomLayou.addView(emot2);
        netCount = 0;

        if (CGApplication.getInstance().getChildInfoMap() != null
                && CGApplication.getInstance().getChildInfoMap().size() > 0) {
            netCount = CGApplication.getInstance().getChildInfoMap().size();
            getCourseOfChildren();
        }

        //根据班级的个数获取培训课程点赞列表
        if(CGApplication.getInstance().getLogin()!=null&&
                CGApplication.getInstance().getLogin().getJSESSIONID()!=null)
        getTrainingCourseOfChildren();

        return view;
    }



    //从培训机构获取的孩子的信息
    public List<TrainChildInfo> getTrainChildInfoList(){
        return trainChildInfoList;
    }

    //从培训机构获取的孩子的班级的信息
    public List<TrainClass> getTrainClass(){
        return lists;
    }

    //网络获取孩子培训课程信息。
    private void getTrainingCourseOfChildren() {
        //先实行简单的解析，后期优化  TODO
        MainActivity mainActivity  = MainActivity.instance;
        trainChildInfoList = mainActivity.getTrainChildInfoList().getList();
        lists = mainActivity.getTrainChildInfoList().getClass_list();
        for(int chang = 0;chang < lists.size();chang++){
            getc(lists.get(chang).getClass_uuid(),chang,lists.size());
        }
    }

    private void getc(String class_uuid, final int chang , final int size) {
        UserRequest.getTrainingCourseOfChildren(mContext, TimeUtil.getYMDTimeFromDate(new Date()),
                class_uuid, new RequestResultI() {
                    @Override
                    public void result(BaseModel domain) {
                        TrainCourse trainCourse = (TrainCourse) domain;
                        Log.i("TAG","打印TrainCourse对象"+trainCourse);
                            if(trainCourse!=null) {
                                MyTrainCoures myTrainCoures = trainCourse.getList();
                                if(myTrainCoures!=null&&myTrainCoures.getData()!=null) {
                                    courseListAdapter.addTrainCourseList(myTrainCoures.getData());
                                }
                            }
                    }

                    @Override
                    public void result(List<BaseModel> domains, int total) {
                    }

                    @Override
                    public void failure(String message) {

                    }
                }
        );
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
        bottomLayou.removeAllViews();
        bottomLayou.addView(emot2);
        nowReplyUUID = uuid;
        this.weekIndex = weekIndex;
        bottomLayou.setVisibility(View.VISIBLE);
        emot2.showSoftKeyboard();
        this.userCourse = userCourse;
    }

    private TrainCourseContent send_Train_Course_Content;
    public void showTrainReplyLayout(String uuid,TrainCourseContent trainCourseContent){
        bottomLayou.removeAllViews();
        if(myViewEmot2==null) {
            myViewEmot2 = new ViewEmot2(getActivity(), new SendMessage() {
                @Override
                public void send(String message) {
                    sendTrainReply(nowReplyUUID, message, myViewEmot2);
                }
            });
        }
        bottomLayou.addView(myViewEmot2);
        send_Train_Course_Content = trainCourseContent;
        nowReplyUUID = uuid;
        bottomLayou.setVisibility(View.VISIBLE);
        myViewEmot2.showSoftKeyboard();
    }

    //训练课程表内容回复
    private void sendTrainReply(String nowReplyUUID, final String replyContent, final ViewEmot2 myViewEmot2) {
        if (Utils.stringIsNull(replyContent)) {
            Utils.showToast(getActivity(), "请输入内容");
            return;
        }
        UserRequest.reply(mContext, nowReplyUUID, replyContent.trim(), "",
                NormalReplyListActivity.REPLY_TYPE_TRAIN_CLASS, new RequestResultI() {
                    @Override
                    public void result(BaseModel domain) {

                        try {
                            ((CourseListActivity) getActivity()).hideProgressDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        myViewEmot2.cleanEditText();
                        bottomLayou.setVisibility(View.GONE);
                        myViewEmot2.hideSoftKeyboard();
                        Reply reply = new Reply();
                        reply.setContent(replyContent.trim());
                        reply.setCreate_user(CGApplication.getInstance().getLogin().getUserinfo().getName());
                        addTrainReply(send_Train_Course_Content, reply);
                    }

                    @Override
                    public void result(List<BaseModel> domains, int total) {

                    }

                    @Override
                    public void failure(String message) {

                    }
                });


    }

    //添加训练课程内容并且更新
    private void addTrainReply(TrainCourseContent send_train_course_content, Reply reply) {
        if(send_train_course_content.getReplyPage().getData()!=null){
            send_train_course_content.getReplyPage().getData().add(0,reply);
            courseListAdapter.notifyDataSetChanged();
        }
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

    private void sendReply(String uuid, final String replyContent, final ViewEmot2 viewEmot2) {
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
                        viewEmot2.cleanEditText();
                        bottomLayou.setVisibility(View.GONE);
                        viewEmot2.hideSoftKeyboard();
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
