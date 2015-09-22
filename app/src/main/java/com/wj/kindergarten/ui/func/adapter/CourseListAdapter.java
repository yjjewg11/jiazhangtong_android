package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

import com.umeng.socialize.utils.Log;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Course;
import com.wj.kindergarten.bean.DianZan;
import com.wj.kindergarten.bean.Reply;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.emot.EmotUtil;
import com.wj.kindergarten.ui.func.CourseListFragment;
import com.wj.kindergarten.ui.func.NormalReplyListActivity;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * FoodListAdapter
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/27 1:23
 */
public class CourseListAdapter extends BaseAdapter {
    private Context mContext;
    private CourseListFragment courseListFragment;
    private List<CourseListFragment.UserCourse> courseList = new ArrayList<>();
    private boolean zanLock = false;
    //    private Map<Integer, Integer> perChildWeek = new HashMap<>();
    private int weekIndex = 0;


    public CourseListAdapter(Context context, CourseListFragment courseListFragment, String date) {
        mContext = context;
        this.courseListFragment = courseListFragment;

        Log.i("TAG","打印时间"+date);
        int tempWeek = TimeUtil.getWeekOfDayNum(TimeUtil.getNowDate());
        if (tempWeek > 5) {
            weekIndex = 0;
        } else {
            weekIndex = tempWeek - 1;
        }
    }

    public void setCourseList(List<CourseListFragment.UserCourse> foods) {
        this.courseList = foods;
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
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final CourseListFragment.UserCourse userCourse = courseList.get(i);
        ImageLoaderUtil.displayImage(userCourse.getChildInfo().getHeadimg(), viewHolder.head);

        final List<Course> courseTL = userCourse.getCourses();
        final Course course;

//        if (perChildWeek.containsKey(i)) {
//            weekIndex = perChildWeek.get(i);
        if (courseTL != null) {
            course = getCourseByWeek(weekIndex, courseTL);
        } else {
            course = null;
        }
//        } else {
//            weekIndex = 0;
//            if (courseTL != null) {
//                course = getCourseByWeek(weekIndex, courseTL);
//            } else {
//                course = null;
//            }
//        }

        if (course == null) {
            viewHolder.courseContent.setVisibility(View.GONE);
            viewHolder.noCourseContent.setVisibility(View.VISIBLE);
            viewHolder.morningTv.setText("");
            viewHolder.afternoonTv.setText("");
        } else {
            viewHolder.courseContent.setVisibility(View.VISIBLE);
            viewHolder.noCourseContent.setVisibility(View.GONE);

            viewHolder.morningTv.setText(course.getMorning());
            viewHolder.afternoonTv.setText(course.getAfternoon());

            viewHolder.viewCountTv.setText("浏览" + course.getCount() + "次");
            if (course.getDianzan() != null) {
                viewHolder.replyIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Message message = new Message();
                        message.what = 0;
                        message.obj = userCourse;
                        message.arg1 = weekIndex;
                        mHandler.sendMessageDelayed(message, 300);
                    }
                });
                viewHolder.iReplyEt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Message message = new Message();
                        message.what = 0;
                        message.arg1 = weekIndex;
                        message.obj = userCourse;
                        mHandler.sendMessageDelayed(message, 300);
                    }
                });
                viewHolder.zanIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (zanLock) {
                            return;
                        }
                        zanLock = true;
                        Drawable drawable = viewHolder.zanIv.getDrawable();
                        if (mContext.getResources().getDrawable(R.drawable.interaction_zan_off).getConstantState()
                                .equals(drawable.getConstantState())) {
                            setZan(course, viewHolder.zanIv);
                        } else {
                            cancelZan(course, viewHolder.zanIv);
                        }
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

                DianZan dianZan = course.getDianzan();
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

        viewHolder.mondayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                perChildWeek.put(i, 0);
                weekIndex = 0;
                notifyDataSetChanged();
            }
        });

        viewHolder.tuesdayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                perChildWeek.put(i, 1);
                weekIndex = 1;
                notifyDataSetChanged();
            }
        });

        viewHolder.wednesdayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                perChildWeek.put(i, 2);
                weekIndex = 2;
                notifyDataSetChanged();
            }
        });

        viewHolder.thursdayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                perChildWeek.put(i, 3);
                weekIndex = 3;
                notifyDataSetChanged();
            }
        });

        viewHolder.fridayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                perChildWeek.put(i, 4);
                weekIndex = 4;
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
    }
}
