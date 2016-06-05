package com.wj.kindergarten.ui.func.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.DianZan;
import com.wj.kindergarten.bean.Food;
import com.wj.kindergarten.bean.Reply;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.emot.EmotUtil;
import com.wj.kindergarten.ui.func.FoodPageFragment;
import com.wj.kindergarten.ui.func.NormalReplyListActivity;
import com.wj.kindergarten.ui.imagescan.PhotoWallActivity;
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
public class FoodListAdapter extends BaseAdapter {
    private Context mContext;
    private FoodPageFragment foodPageFragment;
    private ImageView bigPic = null;
    private String date;
    private List<FoodPageFragment.UserFood> foods = new ArrayList<>();
    private PopupWindow mPopupWindow = null;
    private boolean zanLock = false;

    public FoodListAdapter(Context context, FoodPageFragment foodPageFragment, String date) {
        mContext = context;
        this.date = date;
        this.foodPageFragment = foodPageFragment;
    }

    public void setFoods(List<FoodPageFragment.UserFood> foods) {
        this.foods = foods;
        if (this.foods == null) {
            this.foods = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return foods.size();
    }

    @Override
    public Object getItem(int i) {
        return foods.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(mContext, R.layout.fragment_food, null);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    foodPageFragment.hideBottomLayout();
                }
            });

            viewHolder.head = (CircleImage) view.findViewById(R.id.food_head);
            viewHolder.dateTv = (TextView) view.findViewById(R.id.food_date);
            viewHolder.weekTv = (TextView) view.findViewById(R.id.food_week);
            viewHolder.breakLayout = (LinearLayout) view.findViewById(R.id.break_content);
            viewHolder.breakAddLayout = (LinearLayout) view.findViewById(R.id.break_add_content);
            viewHolder.lunchLayout = (LinearLayout) view.findViewById(R.id.lunch_content);
            viewHolder.lunchAddLayout = (LinearLayout) view.findViewById(R.id.lunch_add_content);
            viewHolder.dinerLayout = (LinearLayout) view.findViewById(R.id.diner_content);
            viewHolder.analysisTv = (TextView) view.findViewById(R.id.food_analysis);
            viewHolder.viewCountTv = (TextView) view.findViewById(R.id.notice_see);
            viewHolder.zanIv = (ImageView) view.findViewById(R.id.notice_zan);
            viewHolder.replyIv = (ImageView) view.findViewById(R.id.notice_reply);
            viewHolder.zanCountTv = (TextView) view.findViewById(R.id.notice_zan_count);
            viewHolder.iReplyEt = (TextView) view.findViewById(R.id.notice_reply_edit);
            viewHolder.replyMoreTv = (TextView) view.findViewById(R.id.notice_reply_more);
            viewHolder.replyLayout = (LinearLayout) view.findViewById(R.id.notice_reply_content);
            viewHolder.foodContent = (LinearLayout) view.findViewById(R.id.food_content);
            viewHolder.noFoodContent = (LinearLayout) view.findViewById(R.id.food_none);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final FoodPageFragment.UserFood userFood = foods.get(i);
        ImageLoaderUtil.displayImage(userFood.getGroup().getImg(), viewHolder.head);
        viewHolder.dateTv.setText(date /*+ " " + userFood.getGroup().getBrand_name()*/);
        viewHolder.weekTv.setText(TimeUtil.getWeekOfDay(date));

        final Food food = userFood.getFood();
        if (food == null) {
            viewHolder.foodContent.setVisibility(View.GONE);
            viewHolder.noFoodContent.setVisibility(View.VISIBLE);
        } else {
            viewHolder.foodContent.setVisibility(View.VISIBLE);
            viewHolder.noFoodContent.setVisibility(View.GONE);

            initFoodViews(food, viewHolder.breakLayout, viewHolder.breakAddLayout, viewHolder.lunchLayout,
                    viewHolder.lunchAddLayout, viewHolder.dinerLayout);

            if (food.getAnalysis() != null) {
                viewHolder.analysisTv.setText(food.getAnalysis());
            } else {
                viewHolder.analysisTv.setText("");
            }
            viewHolder.viewCountTv.setText("浏览" + food.getCount() + "次");
            if (food.getDianzan() != null) {
                viewHolder.replyIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Message message = new Message();
                        message.what = 0;
                        message.obj = userFood;
                        mHandler.sendMessageDelayed(message, 300);
                    }
                });
                viewHolder.iReplyEt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Message message = new Message();
                        message.what = 0;
                        message.obj = userFood;
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
                            setZan(food, viewHolder.zanIv);
                        } else {
                            cancelZan(food, viewHolder.zanIv);
                        }
                    }
                });

                viewHolder.replyMoreTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, NormalReplyListActivity.class);
                        intent.putExtra("replyId", food.getUuid());
                        intent.putExtra("type", NormalReplyListActivity.REPLY_TYPE_FOOD);
                        mContext.startActivity(intent);
                    }
                });

                DianZan dianZan = food.getDianzan();
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

            if (food.getReplyPage() != null) {
                if (food.getReplyPage() != null && food.getReplyPage().getData() != null) {
                    List<Reply> replies = food.getReplyPage().getData();
                    addReplyView(viewHolder.replyLayout, replies);

                    if (food.getReplyPage().getTotalCount() > food.getReplyPage().getPageSize()) {
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

        return view;
    }

    private void initFoodViews(Food food, LinearLayout breakLayout, LinearLayout breakAddLayout,
                               LinearLayout lunchLayout, LinearLayout lunchAddLayout, LinearLayout dinerLayout) {
        if (food != null) {
            if (food.getList_time_1() != null && food.getList_time_1().size() > 0) {
                addCookBook(breakLayout, food.getList_time_1());
            } else {
                breakLayout.removeAllViews();
            }

            if (food.getList_time_2() != null && food.getList_time_2().size() > 0) {
                addCookBook(breakAddLayout, food.getList_time_2());
            } else {
                breakAddLayout.removeAllViews();
            }

            if (food.getList_time_3() != null && food.getList_time_3().size() > 0) {
                addCookBook(lunchLayout, food.getList_time_3());
            } else {
                lunchLayout.removeAllViews();
            }

            if (food.getList_time_4() != null && food.getList_time_4().size() > 0) {
                addCookBook(lunchAddLayout, food.getList_time_4());
            } else {
                lunchAddLayout.removeAllViews();
            }

            if (food.getList_time_5() != null && food.getList_time_5().size() > 0) {
                addCookBook(dinerLayout, food.getList_time_5());
            } else {
                dinerLayout.removeAllViews();
            }
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    FoodPageFragment.UserFood userFood = (FoodPageFragment.UserFood) msg.obj;
                    if (userFood != null && userFood.getFood() != null && !Utils.stringIsNull(userFood.getFood().getUuid())) {
                        foodPageFragment.showReplyLayout(userFood.getFood().getUuid(), userFood);
                    }
                    break;
            }
        }
    };

    private void addCookBook(final LinearLayout viewGroup, final List<Food.CookBook> cookBooks) {
        viewGroup.removeAllViews();
        LinearLayout lineOne = null;
        LinearLayout lineTwo = null;
        if (cookBooks == null || cookBooks.size() <= 0) {
            return;
        }
        int padding = mContext.getResources().getDimensionPixelSize(R.dimen.normal_padding);
        for (int i = 0; i < cookBooks.size(); i++) {
            if ((cookBooks.size() - 1) / 3 == 0) {
                if (lineOne == null) {
                    lineOne = new LinearLayout(mContext);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(padding, 0, padding, 0);
                    lineOne.setLayoutParams(layoutParams);
                    lineOne.setOrientation(LinearLayout.HORIZONTAL);
                }
                final Food.CookBook cookBook = cookBooks.get(i);
                View childItemView = View.inflate(mContext, R.layout.item_food_cookbook, null);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (i % 3 == 1) {
                    layoutParams.setMargins(padding, 0, padding, 0);
                }
                childItemView.setLayoutParams(layoutParams);
                ImageView imgIv = (ImageView) childItemView.findViewById(R.id.cook_img);
                TextView nameTv = (TextView) childItemView.findViewById(R.id.cook_name);
                ImageLoaderUtil.displayImage(cookBook.getImg(), imgIv);
                nameTv.setText(cookBook.getName());
                final int position = i;
                childItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        showBigImage(viewGroup, cookBook.getImg());
                        jump2PhotoWall(position, cookBooks);
                    }
                });
                lineOne.addView(childItemView);
                continue;
            }
            if (cookBooks.size() / 3 == 1) {
                if (i < 3) {
                    if (lineOne == null) {
                        lineOne = new LinearLayout(mContext);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(padding, 0, padding, 0);
                        lineOne.setLayoutParams(layoutParams);
                        lineOne.setOrientation(LinearLayout.HORIZONTAL);
                    }
                    final Food.CookBook cookBook = cookBooks.get(i);
                    View childItemView = View.inflate(mContext, R.layout.item_food_cookbook, null);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    if (i % 3 == 1) {
                        layoutParams.setMargins(padding, 0, padding, 0);
                    }
                    childItemView.setLayoutParams(layoutParams);
                    ImageView imgIv = (ImageView) childItemView.findViewById(R.id.cook_img);
                    TextView nameTv = (TextView) childItemView.findViewById(R.id.cook_name);
                    ImageLoaderUtil.displayImage(cookBook.getImg(), imgIv);
                    nameTv.setText(cookBook.getName());
                    final int position = i;
                    childItemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            showBigImage(viewGroup, cookBook.getImg());
                            jump2PhotoWall(position, cookBooks);
                        }
                    });
                    lineOne.addView(childItemView);
                    continue;
                }

                if (lineTwo == null) {
                    lineTwo = new LinearLayout(mContext);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(padding, padding, padding, 0);
                    lineTwo.setLayoutParams(layoutParams);
                    lineTwo.setOrientation(LinearLayout.HORIZONTAL);
                }
                final Food.CookBook cookBook = cookBooks.get(i);
                View childItemView = View.inflate(mContext, R.layout.item_food_cookbook, null);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (i % 3 == 1) {
                    layoutParams.setMargins(padding, 0, padding, 0);
                }
                childItemView.setLayoutParams(layoutParams);
                ImageView imgIv = (ImageView) childItemView.findViewById(R.id.cook_img);
                TextView nameTv = (TextView) childItemView.findViewById(R.id.cook_name);
                ImageLoaderUtil.displayImage(cookBook.getImg(), imgIv);
                nameTv.setText(cookBook.getName());
                final int position = i;
                childItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        showBigImage(viewGroup, cookBook.getImg());
                        jump2PhotoWall(position, cookBooks);
                    }
                });
                lineTwo.addView(childItemView);
            }
        }

        viewGroup.setOrientation(LinearLayout.VERTICAL);
        if (lineOne != null) {
            viewGroup.addView(lineOne);
        }
        if (lineTwo != null) {
            viewGroup.addView(lineTwo);
        }
    }

    public void addReply(FoodPageFragment.UserFood userFood, Reply reply) {
        userFood.getFood().getReplyPage().getData().add(0, reply);
        userFood.getFood().getReplyPage().setTotalCount(userFood.getFood().getReplyPage().getTotalCount() + 1);
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

    private void setZan(final Food food, final ImageView imageView) {
        UserRequest.zan(mContext, food.getUuid(), NormalReplyListActivity.REPLY_TYPE_INTERACTION, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                imageView.setImageResource(R.drawable.interaction_zan_on);
                if (Utils.stringIsNull(food.getDianzan().getNames())) {
                    food.getDianzan().setNames(CGApplication.getInstance().getLogin().getUserinfo().getName());
                } else {
                    food.getDianzan().setNames(food.getDianzan().getNames()
                            + "," + CGApplication.getInstance().getLogin().getUserinfo().getName());
                }
                food.getDianzan().setCount(food.getDianzan().getCount() + 1);
                food.getDianzan().setCanDianzan(false);
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

    private void cancelZan(final Food food, final ImageView imageView) {
        UserRequest.zanCancel(mContext, food.getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                imageView.setImageResource(R.drawable.interaction_zan_off);
                if (food.getDianzan().getNames().contains("," +
                        CGApplication.getInstance().getLogin().getUserinfo().getName())) {
                    food.getDianzan().setNames(food.getDianzan().getNames().replace(","
                            + CGApplication.getInstance().getLogin().getUserinfo().getName(), ""));
                } else if (food.getDianzan().getNames().contains(
                        CGApplication.getInstance().getLogin().getUserinfo().getName() + ",")) {
                    food.getDianzan().setNames(food.getDianzan().getNames().replace(
                            CGApplication.getInstance().getLogin().getUserinfo().getName() + ",", ""));
                } else if (food.getDianzan().getNames().contains(CGApplication.getInstance().getLogin().getUserinfo().getName())) {
                    food.getDianzan().setNames(food.getDianzan().getNames()
                            .replace(CGApplication.getInstance().getLogin().getUserinfo().getName(), ""));
                }
                food.getDianzan().setCount(food.getDianzan().getCount() - 1);
                food.getDianzan().setCanDianzan(true);
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

    private void jump2PhotoWall(int position, List<Food.CookBook> cookBooks) {
        ArrayList<String> mUrlList = new ArrayList<>();
        for (Food.CookBook temp : cookBooks) {
            mUrlList.add(temp.getImg());
        }
        Intent intent = new Intent(mContext, PhotoWallActivity.class);
        intent.putExtra(PhotoWallActivity.KEY_POSITION, position);
        intent.putStringArrayListExtra(PhotoWallActivity.KEY_LIST, mUrlList);
        mContext.startActivity(intent);
    }

    private void showBigImage(View rootView, String url) {
        if (mPopupWindow != null) {
            if (bigPic != null && !Utils.stringIsNull(url)) {
                ImageLoaderUtil.displayImage(url, bigPic);
                mPopupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
            }
            return;
        }
        LinearLayout popupView = null;
        popupView = (LinearLayout) View.inflate(mContext, R.layout.layout_food_big_pic, null);
        mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        bigPic = (ImageView) popupView.findViewById(R.id.food_big_pic);
        ImageLoaderUtil.displayImage(url, bigPic);
        popupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        mPopupWindow.setAnimationStyle(R.style.PopWindowAnim);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);//outside hide
        mPopupWindow.getContentView().setFocusableInTouchMode(true);
        mPopupWindow.getContentView().setFocusable(true);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopupWindow.getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.update();
        mPopupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
    }

    class ViewHolder {
        CircleImage head;
        TextView dateTv;
        TextView weekTv;
        LinearLayout breakLayout;
        LinearLayout breakAddLayout;
        LinearLayout lunchLayout;
        LinearLayout lunchAddLayout;
        LinearLayout dinerLayout;
        TextView analysisTv;
        TextView viewCountTv;
        ImageView zanIv;
        ImageView replyIv;
        TextView zanCountTv;
        TextView iReplyEt;
        TextView replyMoreTv;
        LinearLayout replyLayout;
        LinearLayout foodContent;
        LinearLayout noFoodContent;
    }
}
