package com.wj.kindergarten.ui.func.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Course;
import com.wj.kindergarten.bean.DianZan;

import com.wj.kindergarten.bean.MyTrainCoures;
import com.wj.kindergarten.bean.Reply;
import com.wj.kindergarten.bean.TrainCourse;

import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.emot.EmotUtil;
import com.wj.kindergarten.ui.func.CourseListFragment;
import com.wj.kindergarten.ui.func.MineCourseDetailActivity;
import com.wj.kindergarten.ui.func.NormalReplyListActivity;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * FoodListAdapter
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/27 1:23
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CourseListAdapter extends BaseAdapter {
    private Context mContext;
    private CourseListFragment courseListFragment;
    private List<Object> courseList = new ArrayList<>();
    //承载培训课程内容的集合


    private List<TrainCourse> trainCourseList  ;

    private boolean zanLock = false;
    //    private Map<Integer, Integer> perChildWeek = new HashMap<>();
    private int weekIndex = 0;
    private int trainWeekIndex = 0;


    public void addTrainCourseList(List<MyTrainCoures> list){
        for(MyTrainCoures myTrainCoures : list){
            courseList.add(myTrainCoures);

        }
        notifyDataSetChanged();
    }


    public CourseListAdapter(Context context, CourseListFragment courseListFragment, String date) {
        mContext = context;
        this.courseListFragment = courseListFragment;
        int tempWeek = TimeUtil.getWeekOfDayNum(TimeUtil.getNowDate());
        if (tempWeek == 0 || tempWeek == 6) {
            weekIndex = 0;
        } else {
            weekIndex = tempWeek - 1;
        }
    }


//    public void setTrainCourseList(List<TrainCourse> trainCourseList){
//        this.trainCourseList = trainCourseList;
//        notifyDataSetChanged();
//    }

    public void setCourseList(List<CourseListFragment.UserCourse> foods) {
//        this.courseList = foods;
        courseList.addAll(foods);
        if (this.courseList == null) {
            this.courseList = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return courseList.size();
    }

    @Override
    public Object getItem(int i) {
        return courseList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private Long moneyDayDateMullions = new Date().getTime();;

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(mContext, R.layout.item_course_list, null);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    courseListFragment.hideBottomLayout();
                }
            });
            viewHolder.head = (CircleImage) view.findViewById(R.id.course_head);
            viewHolder.mondayTv = (RelativeLayout) view.findViewById(R.id.course_monday);
            viewHolder.tuesdayTv = (RelativeLayout) view.findViewById(R.id.course_tuesday);
            viewHolder.wednesdayTv = (RelativeLayout) view.findViewById(R.id.course_wednesday);
            viewHolder.thursdayTv = (RelativeLayout) view.findViewById(R.id.course_thursday);
            viewHolder.fridayTv = (RelativeLayout) view.findViewById(R.id.course_friday);
            viewHolder.mondayIv = (ImageView) view.findViewById(R.id.course_week_bg1);
            viewHolder.tuesdayIv = (ImageView) view.findViewById(R.id.course_week_bg2);
            viewHolder.wednesdayIv = (ImageView) view.findViewById(R.id.course_week_bg3);
            viewHolder.thursdayIv = (ImageView) view.findViewById(R.id.course_week_bg4);
            viewHolder.fridayIv = (ImageView) view.findViewById(R.id.course_week_bg5);
            viewHolder.morningTv = (TextView) view.findViewById(R.id.course_morning);
            viewHolder.afternoonTv = (TextView) view.findViewById(R.id.course_afternoon);
            viewHolder.viewCountTv = (TextView) view.findViewById(R.id.notice_see);
            viewHolder.zanIv = (ImageView) view.findViewById(R.id.notice_zan);
            viewHolder.replyIv = (ImageView) view.findViewById(R.id.notice_reply);
            viewHolder.zanCountTv = (TextView) view.findViewById(R.id.notice_zan_count);
            viewHolder.iReplyEt = (TextView) view.findViewById(R.id.notice_reply_edit);
            viewHolder.replyMoreTv = (TextView) view.findViewById(R.id.notice_reply_more);
            viewHolder.replyLayout = (LinearLayout) view.findViewById(R.id.notice_reply_content);
            viewHolder.courseContent = (FrameLayout) view.findViewById(R.id.course_reply);
            viewHolder.noCourseContent = (LinearLayout) view.findViewById(R.id.course_none);

            viewHolder.train_fl = (FrameLayout)view.findViewById(R.id.train_fl);
            viewHolder.course_ll = (LinearLayout)view.findViewById(R.id.course_ll);

            //培训课程的内容显示
            viewHolder.tv_below_school_class_name = (TextView)view.findViewById(R.id.tv_below_school_class_name);
            viewHolder.tv_edcucation_center = (TextView)view.findViewById(R.id.tv_edcutation_center);
            viewHolder.iv_training_head = (CircleImage)view.findViewById(R.id.iv_training_head);

            viewHolder.tv_yy_mm_dd_time = (TextView)view.findViewById(R.id.tv_yy_mm_dd_time);
            viewHolder.tv_everyday_oneweek = (TextView)view.findViewById(R.id.tv_everyday_oneweek);
//            viewHolder.tv_training_dianZancount = (TextView)view.findViewById(R.id.train_notice_zan_count);
            viewHolder.tv_training_classname_content = (TextView)view.findViewById(R.id.tv_training_classname_content);
            viewHolder.tv_training_classtime = (TextView)view.findViewById(R.id.tv_training_classtime);
            viewHolder.tv_trainning_adress = (TextView)view.findViewById(R.id.tv_trainning_adress);
            viewHolder.tv_trainning_preparething = (TextView)view.findViewById(R.id.tv_trainning_preparething);
            viewHolder.iv_train_today_tixing = (ImageView)view.findViewById(R.id.iv_train_today_tixing);
            viewHolder.tv_more_course = (TextView)view.findViewById(R.id.tv_more_course);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        CourseListFragment.UserCourse userCourse = null;
        MyTrainCoures myTrainCoures = null;

        //如果是课程表
        if(courseList.get(i) instanceof CourseListFragment.UserCourse){
             userCourse = (CourseListFragment.UserCourse) courseList.get(i);
             ImageLoaderUtil.displayImage(userCourse.getChildInfo().getHeadimg(), viewHolder.head);
             viewHolder.train_fl.setVisibility(View.GONE);
             viewHolder.course_ll.setVisibility(View.VISIBLE);

        }else if (courseList.get(i) instanceof MyTrainCoures){
            myTrainCoures = (MyTrainCoures)courseList.get(i);

             viewHolder.train_fl.setVisibility(View.VISIBLE);
             viewHolder.course_ll.setVisibility(View.GONE);
        }




        final List<Course> courseTL = (userCourse==null ? null : userCourse.getCourses());
        final Course course;


        if (courseTL != null) {
            course = getCourseByWeek(weekIndex, courseTL);
        } else {
            course = null;
        }

        if (course == null) {
            viewHolder.courseContent.setVisibility(View.GONE);
            viewHolder.noCourseContent.setVisibility(View.VISIBLE);
            viewHolder.morningTv.setText("");
            viewHolder.afternoonTv.setText("");
        } else {
            viewHolder.noCourseContent.setVisibility(View.GONE);
            viewHolder.courseContent.setVisibility(View.VISIBLE);

            viewHolder.morningTv.setText(course.getMorning());
            viewHolder.afternoonTv.setText(course.getAfternoon());
            viewHolder.viewCountTv.setText("浏览" + course.getCount() + "次");
            if (course.getDianzan() != null) {
                final CourseListFragment.UserCourse finalUserCourse = userCourse;
                viewHolder.replyIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Message message = new Message();
                        message.what = 0;
                        message.obj = finalUserCourse;
                        message.arg1 = weekIndex;
                        mHandler.sendMessageDelayed(message, 300);
                    }
                });
                final CourseListFragment.UserCourse finalUserCourse1 = userCourse;
                viewHolder.iReplyEt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Message message = new Message();
                        message.what = 0;
                        message.arg1 = weekIndex;
                        message.obj = finalUserCourse1;
                        mHandler.sendMessageDelayed(message, 300);
                    }
                });
                viewHolder.replyMoreTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, NormalReplyListActivity.class);
                        intent.putExtra("replyId", course.getUuid());
                        intent.putExtra("type", NormalReplyListActivity.REPLY_TYPE_FOOD);
                        mContext.startActivity(intent);
                    }
                });

                final DianZan dianZan = course.getDianzan();
                viewHolder.zanIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        if (zanLock) {
//                            return;
//                        }
                        zanLock = true;
//                        Drawable drawable = viewHolder.zanIv.getDrawable();
                        if (dianZan.isCanDianzan()) {
                            setZan(course, viewHolder.zanIv);
                        } else {
                            cancelZan(course, viewHolder.zanIv);
                        }
                    }
                });

                setCommonDianZan(viewHolder, dianZan);
            } else {
                viewHolder.zanIv.setImageResource(R.drawable.interaction_zan_off);
                viewHolder.zanCountTv.setText("0人觉得很赞");
            }

            if (course.getReplyPage() != null) {
                if (course.getReplyPage() != null && course.getReplyPage().getData() != null) {
                    List<Reply> replies = course.getReplyPage().getData();
                    addReplyView(viewHolder.replyLayout, replies);

                    if (course.getReplyPage().getTotalCount() > course.getReplyPage().getPageSize()) {
                        viewHolder.replyMoreTv.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.replyMoreTv.setVisibility(View.GONE);
                    }
                }
            } else {
                viewHolder.replyLayout.removeAllViews();
                viewHolder.replyMoreTv.setVisibility(View.GONE);
            }

        }

        if(myTrainCoures!=null) {
            final MyTrainCoures finalMyTrainCoures1 = myTrainCoures;
            viewHolder.tv_more_course.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击跳转我的课程详情页面
                    Intent intent = new Intent(mContext,MineCourseDetailActivity.class);
                    intent.putExtra("courseuuid", finalMyTrainCoures1.getClassuuid());
                    intent.putExtra(GloablUtils.FROM_COURSE_TO_MINE_COURSE, finalMyTrainCoures1);
                    mContext.startActivity(intent);
                }
            });
            String de = TimeUtil.getDateToString(moneyDayDateMullions);
            String getTimeFromHttp = TimeUtil.getYMDTimeFromYMDHMS(myTrainCoures.getPlandate());
            viewHolder.tv_yy_mm_dd_time.setText(""+getTimeFromHttp);
            viewHolder.tv_everyday_oneweek.setText("" +TimeUtil.getDay(getTimeFromHttp));
            ImageLoaderUtil.displayMyImage(myTrainCoures.getStudent_headimg(),viewHolder.iv_training_head);

            //判断课程是否是今日的
            viewHolder.courseContent.setVisibility(View.VISIBLE);
            if (de.equals(getTimeFromHttp)){

                viewHolder.iv_train_today_tixing.setVisibility(View.VISIBLE);
            }else {
                viewHolder.iv_train_today_tixing.setVisibility(View.INVISIBLE);
            }

            //显示数据

            // TODO 教育中心数据
            viewHolder.tv_below_school_class_name.setText("" + (myTrainCoures.getGroup_name()));
            viewHolder.tv_training_classname_content.setText("" + (myTrainCoures.getName() == null ? "" : myTrainCoures.getName()));
            viewHolder.tv_training_classtime.setText("" + (myTrainCoures.getPlandate()== null  ? "" : myTrainCoures.getPlandate()));
            viewHolder.tv_trainning_adress.setText("" + (myTrainCoures.getAddress()== null  ? "" : myTrainCoures.getAddress()));
            viewHolder.tv_trainning_preparething.setText("" + (myTrainCoures.getReadyfor()== null ? "" : myTrainCoures.getReadyfor()));

            final DianZan trainDianZan = myTrainCoures.getDianZan();
            final MyTrainCoures finalMyTrainCoures = myTrainCoures;
            if(trainDianZan != null){
                setCommonDianZan(viewHolder,trainDianZan);


                viewHolder.zanIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (trainDianZan.isCanDianzan()) {
                            setTrainZan(finalMyTrainCoures, viewHolder.zanIv);
                        } else {
                            cancelTrainZan(finalMyTrainCoures, viewHolder.zanIv);
                        }
                    }
                });
            }



            if (myTrainCoures.getReplyPage() != null && myTrainCoures.getReplyPage().getData() != null) {
                List<Reply> replies = myTrainCoures.getReplyPage().getData();
                addReplyView(viewHolder.replyLayout, replies);
            }
            //点击点赞之后根据该变切换状态

            //点赞旁的回复
            viewHolder.replyIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Message message = new Message();
                    message.what = 10;
                    message.obj = finalMyTrainCoures;

                    message.arg1 = weekIndex;
                    mHandler.sendMessageDelayed(message, 300);

                }
            });

            //点击输入内容
            viewHolder.iReplyEt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Message message = new Message();
                    message.what = 10;
                    message.obj = finalMyTrainCoures;

                    message.arg1 = weekIndex;
                    mHandler.sendMessageDelayed(message, 300);
                }
            });


        }else{
            //如果没有数据
            //隐藏回复 TODO

            viewHolder.iv_train_today_tixing.setVisibility(View.INVISIBLE);
            viewHolder.tv_training_classname_content.setText("");
            viewHolder.tv_training_classtime.setText("" );
            viewHolder.tv_trainning_adress.setText("" );
            viewHolder.tv_trainning_preparething.setText("");

        }

        viewHolder.mondayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                perChildWeek.put(i, 0);
                weekIndex = 0;
                trainWeekIndex = 0;
                notifyDataSetChanged();
            }
        });

        viewHolder.tuesdayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                perChildWeek.put(i, 1);
                weekIndex = 1;
                trainWeekIndex = 1;
                notifyDataSetChanged();
            }
        });

        viewHolder.wednesdayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                perChildWeek.put(i, 2);
                weekIndex = 2;
                trainWeekIndex = 2;
                notifyDataSetChanged();
            }
        });

        viewHolder.thursdayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                perChildWeek.put(i, 3);
                weekIndex = 3;
                trainWeekIndex = 3;
                notifyDataSetChanged();
            }
        });

        viewHolder.fridayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                perChildWeek.put(i, 4);
                weekIndex = 4;
                trainWeekIndex = 4;
                notifyDataSetChanged();
            }
        });
        setWeekBg(weekIndex, 0, viewHolder.mondayIv);
        setWeekBg(weekIndex, 1, viewHolder.tuesdayIv);
        setWeekBg(weekIndex, 2, viewHolder.wednesdayIv);
        setWeekBg(weekIndex, 3, viewHolder.thursdayIv);
        setWeekBg(weekIndex, 4, viewHolder.fridayIv);
        return view;
    }

    //公共点赞部分

    private void setCommonDianZan(ViewHolder viewHolder, DianZan dianZan) {
        if (dianZan.isCanDianzan()) {
            viewHolder.zanIv.setImageResource(R.drawable.interaction_zan_off);
        } else {
            viewHolder.zanIv.setImageResource(R.drawable.interaction_zan_on);
        }
        if (dianZan != null && dianZan.getCount() > 0) {
            String temp = "<font  color='#ff4966'>" + dianZan.getNames() + "</font>" + "等"
                    + dianZan.getCount() + "人觉得很赞";
            viewHolder.zanCountTv.setText(Html.fromHtml(temp));
        } else {
            viewHolder.zanCountTv.setText("0人觉得很赞");
        }
    }


    private boolean trainZanLock = false;

    private Course getCourseByWeek(int week, List<Course> coursesT) {
        if (coursesT != null) {
            for (Course course : coursesT) {
                if (course != null) {
                    String dateT = course.getPlandate();
                    if (!Utils.stringIsNull(dateT)) {
                        int weekCourse = TimeUtil.getWeekOfDayNum(dateT);
                        if (week == weekCourse - 1) {
                            return course;
                        }
                    }
                }
            }
        }
        return null;
    }



    private void setWeekBg(int position, int week, ImageView wt) {
        if (position == week) {
            wt.setImageDrawable(mContext.getResources().getDrawable(R.drawable.course_week_bg));
        } else {
            wt.setImageDrawable(null);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    CourseListFragment.UserCourse userCourse = (CourseListFragment.UserCourse) msg.obj;
                    int weekIndex = msg.arg1;
                    if (userCourse != null && getCourseByWeek(weekIndex, userCourse.getCourses()) != null
                            && !Utils.stringIsNull(getCourseByWeek(weekIndex, userCourse.getCourses()).getUuid())) {
                        courseListFragment.showReplyLayout(getCourseByWeek(weekIndex, userCourse.getCourses()).getUuid(), weekIndex, userCourse);
                    }
                    break;

                case 10:
                    MyTrainCoures m = (MyTrainCoures) msg.obj;
                    courseListFragment.showTrainReplyLayout(m.getUuid(),m);

                    break;
            }
        }
    };

    public void addReply(CourseListFragment.UserCourse userCourse, int weekIndex, Reply reply) {
        for (int i = 0; i < userCourse.getCourses().size(); i++) {
            Course courseT = userCourse.getCourses().get(i);
            int tempWeek = TimeUtil.getWeekOfDayNum(courseT.getPlandate()) - 1;
            if (weekIndex == tempWeek) {
                courseT.getReplyPage().getData().add(0, reply);
                courseT.getReplyPage().setTotalCount(courseT.getReplyPage().getTotalCount() + 1);
            }
        }
        notifyDataSetChanged();
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
                    TextView contentTv = (TextView) view.findViewById(R.id.item_reply_text_content);
                    contentTv.setText(EmotUtil.getEmotionContent(mContext, reply.getContent()));
                    replyContent.addView(view);
                } else {
                    break;
                }
            }
        }
    }

    //取消训练点赞
    public void cancelTrainZan(final MyTrainCoures mTc,final ImageView imageView){
        UserRequest.zanCancel(mContext, mTc.getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                imageView.setImageResource(R.drawable.interaction_zan_off);
                if (mTc.getDianZan().getNames().contains("," +
                        CGApplication.getInstance().getLogin().getUserinfo().getName())) {
                    mTc.getDianZan().setNames(mTc.getDianZan().getNames().replace(","
                            + CGApplication.getInstance().getLogin().getUserinfo().getName(), ""));
                } else if (mTc.getDianZan().getNames().contains(
                        CGApplication.getInstance().getLogin().getUserinfo().getName() + ",")) {
                    mTc.getDianZan().setNames(mTc.getDianZan().getNames().replace(
                            CGApplication.getInstance().getLogin().getUserinfo().getName() + ",", ""));
                } else if (mTc.getDianZan().getNames().contains(CGApplication.getInstance().getLogin().getUserinfo().getName())) {
                    mTc.getDianZan().setNames(mTc.getDianZan().getNames()
                            .replace(CGApplication.getInstance().getLogin().getUserinfo().getName(), ""));
                }
                mTc.getDianZan().setCount(mTc.getDianZan().getCount() - 1);
                mTc.getDianZan().setCanDianzan(true);
                notifyDataSetChanged();
//                zanLock = false;
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
    //训练点赞
    public void setTrainZan(final MyTrainCoures myTrain,final ImageView imageView){

        UserRequest.zan(mContext, myTrain.getUuid(), NormalReplyListActivity.REPLY_TYPE_TRAIN_CLASS, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                imageView.setImageResource(R.drawable.interaction_zan_on);
                if (Utils.stringIsNull(myTrain.getDianZan().getNames())) {
                    myTrain.getDianZan().setNames(CGApplication.getInstance().getLogin().getUserinfo().getName());
                } else {
                    myTrain.getDianZan().setNames(myTrain.getDianZan().getNames()
                            + "," + CGApplication.getInstance().getLogin().getUserinfo().getName());
                }
                myTrain.getDianZan().setCount(myTrain.getDianZan().getCount() + 1);
                myTrain.getDianZan().setCanDianzan(false);
                notifyDataSetChanged();
//                zanLock = false;
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


    private void setZan(final Course course, final ImageView imageView) {
        UserRequest.zan(mContext, course.getUuid(), NormalReplyListActivity.REPLY_TYPE_INTERACTION, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                imageView.setImageResource(R.drawable.interaction_zan_on);
                if (Utils.stringIsNull(course.getDianzan().getNames())) {
                    course.getDianzan().setNames(CGApplication.getInstance().getLogin().getUserinfo().getName());
                } else {
                    course.getDianzan().setNames(course.getDianzan().getNames()
                            + "," + CGApplication.getInstance().getLogin().getUserinfo().getName());
                }
                course.getDianzan().setCount(course.getDianzan().getCount() + 1);
                course.getDianzan().setCanDianzan(false);
                notifyDataSetChanged();
                zanLock = false;
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



    private void cancelZan(final Course course, final ImageView imageView) {
        UserRequest.zanCancel(mContext, course.getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                imageView.setImageResource(R.drawable.interaction_zan_off);
                if (course.getDianzan().getNames().contains("," +
                        CGApplication.getInstance().getLogin().getUserinfo().getName())) {
                    course.getDianzan().setNames(course.getDianzan().getNames().replace(","
                            + CGApplication.getInstance().getLogin().getUserinfo().getName(), ""));
                } else if (course.getDianzan().getNames().contains(
                        CGApplication.getInstance().getLogin().getUserinfo().getName() + ",")) {
                    course.getDianzan().setNames(course.getDianzan().getNames().replace(
                            CGApplication.getInstance().getLogin().getUserinfo().getName() + ",", ""));
                } else if (course.getDianzan().getNames().contains(CGApplication.getInstance().getLogin().getUserinfo().getName())) {
                    course.getDianzan().setNames(course.getDianzan().getNames()
                            .replace(CGApplication.getInstance().getLogin().getUserinfo().getName(), ""));
                }
                course.getDianzan().setCount(course.getDianzan().getCount() - 1);
                course.getDianzan().setCanDianzan(true);
                notifyDataSetChanged();
                zanLock = false;
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

    class ViewHolder {
        CircleImage head;
        RelativeLayout mondayTv;
        RelativeLayout tuesdayTv;
        RelativeLayout wednesdayTv;
        RelativeLayout thursdayTv;
        RelativeLayout fridayTv;
        TextView morningTv;
        TextView afternoonTv;
        TextView viewCountTv;
        ImageView mondayIv;
        ImageView tuesdayIv;
        ImageView wednesdayIv;
        ImageView thursdayIv;
        ImageView fridayIv;
        ImageView zanIv;
        ImageView replyIv;
        TextView zanCountTv;
        TextView iReplyEt;
        TextView replyMoreTv;
        LinearLayout replyLayout;
        FrameLayout courseContent;
        LinearLayout noCourseContent;

        FrameLayout train_fl;
        LinearLayout course_ll;

        TextView tv_edcucation_center,tv_yy_mm_dd_time,
                tv_everyday_oneweek,tv_training_dianZancount,
                tv_training_classname_content,tv_training_classtime,
                tv_trainning_adress,tv_trainning_preparething,
                tv_trainning_notice_see,tv_training_zan_reply_edit,tv_below_school_class_name,
                tv_more_course;

        ImageView iv_trainning_notice_zan,iv_trainning_notice_reply,
        iv_train_today_class,iv_train_today_tixing;

        LinearLayout ll_trainning_notice_reply_content;
        CircleImage iv_training_head;

    }

    public boolean noNextPage(){
        if(trainWeekIndex==0){
            Utils.showToast(mContext,"已经是第一天了！");
            return false;
        }
        if(trainWeekIndex==6){
            Utils.showToast(mContext,"已经是最后一天了！");
            return false;
        }

        return true;


    }

    //此接口用于监听前一天和后一天按钮有无数据时的点击状态的切换
    interface DoEveryThing{
       void myDo();
    }
}
