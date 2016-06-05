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
import com.wj.kindergarten.bean.Food;
import com.wj.kindergarten.bean.FoodList;
import com.wj.kindergarten.bean.Group;
import com.wj.kindergarten.bean.Reply;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.emot.SendMessage;
import com.wj.kindergarten.ui.emot.ViewEmot2;
import com.wj.kindergarten.ui.func.adapter.FoodListAdapter;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * FoodPageFragment
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/26 23:34
 */
public class FoodPageFragment extends Fragment {
    private ListView mListView;
    private TextView dateTv;
    private LinearLayout bottomLayou;
    private ViewEmot2 emot2 = null;

    private UserFood userFood = null;
    private Context mContext;
    private String date;
    private FoodListAdapter foodListAdapter;
    private List<UserFood> foods = new ArrayList<>();
    private String nowReplyUUID = "";
    private int netCount = 0;

    public static FoodPageFragment buildFoodPageFragment(String date) {
        FoodPageFragment foodFragment = new FoodPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("date", date);
        foodFragment.setArguments(bundle);
        return foodFragment;
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
        foods.clear();
        View view = View.inflate(getActivity(), R.layout.fragment_food_page, null);
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
        dateTv = (TextView) view.findViewById(R.id.course_date);
        dateTv.setVisibility(View.GONE);
        foodListAdapter = new FoodListAdapter(getActivity(), this, date);
        mListView.setAdapter(foodListAdapter);

        bottomLayou = (LinearLayout) view.findViewById(R.id.food_bottom);
        emot2 = new ViewEmot2(getActivity(), new SendMessage() {
            @Override
            public void send(String message) {
                sendReply(nowReplyUUID, message);
            }
        });
        bottomLayou.addView(emot2);
        netCount = 0;

        if (CGApplication.getInstance().getGroupMap() != null && CGApplication.getInstance().getGroupMap().size() > 0) {
            netCount = CGApplication.getInstance().getGroupMap().size();
            getFoodOfChildren();
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
                    foodListAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    public void showReplyLayout(String uuid, UserFood userFood) {
        nowReplyUUID = uuid;
        bottomLayou.setVisibility(View.VISIBLE);
        emot2.showSoftKeyboard();
        this.userFood = userFood;
    }

    public void hideBottomLayout() {
        emot2.hideFaceLayout();
        bottomLayou.setVisibility(View.GONE);
    }

    private void showProgressDialog() {
        try {
            ((FoodListActivity) mContext).setProgressDialogCancelable(false);
            ((FoodListActivity) mContext).showProgressDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideProgressDialog() {
        netCount--;
        try {
            if (netCount <= 0) {
                ((FoodListActivity) mContext).hideProgressDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getFoodOfChildren() {
        showProgressDialog();
        for (Map.Entry entry : CGApplication.getInstance().getGroupMap().entrySet()) {
            UserFood userFood = new UserFood();
            userFood.setGroup((Group) entry.getValue());
            foods.add(userFood);
            final String groupUUID = (String) entry.getKey();
            UserRequest.getFoodList(mContext, date, date, groupUUID, new RequestResultI() {
                @Override
                public void result(BaseModel domain) {
                    if (isDetached()) {
                        return;
                    }
                    hideProgressDialog();
                    FoodList foodList = (FoodList) domain;
                    if (foodList != null && foodList.getList() != null && foodList.getList().size() > 0) {
                        Food currentFood = foodList.getList().get(0);
                        for (int i = 0; i < foods.size(); i++) {
                            Group group = foods.get(i).getGroup();
                            if (currentFood.getGroupuuid().equals(group.getUuid())) {
                                foods.get(i).setFood(currentFood);
                                mHandler.sendEmptyMessage(0);
                                break;
                            }
                        }
                    } else {
                        //no data
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

        foodListAdapter.setFoods(foods);
    }

    private void sendReply(String uuid, final String replyContent) {
        if (Utils.stringIsNull(replyContent)) {
            Utils.showToast(getActivity(), "请输入内容");
            return;
        }
        ((FoodListActivity) getActivity()).showProgressDialog("发表回复中，请稍候...");
        UserRequest.reply(getActivity(), uuid, replyContent.trim(), "",
                NormalReplyListActivity.REPLY_TYPE_FOOD, new RequestResultI() {
                    @Override
                    public void result(BaseModel domain) {
                        try {
                            ((FoodListActivity) getActivity()).hideProgressDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        emot2.cleanEditText();
                        bottomLayou.setVisibility(View.GONE);
                        emot2.hideSoftKeyboard();

                        Reply reply = new Reply();
                        reply.setContent(replyContent.trim());
                        reply.setCreate_user(CGApplication.getInstance().getLogin().getUserinfo().getName());
                        foodListAdapter.addReply(userFood, reply);
                    }

                    @Override
                    public void result(List<BaseModel> domains, int total) {

                    }

                    @Override
                    public void failure(String message) {
                        try {
                            ((FoodListActivity) getActivity()).hideProgressDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Utils.showToast(CGApplication.getInstance(), message);
                    }
                });
    }

    public class UserFood {
        private Food food;
        private Group group;

        public Food getFood() {
            return food;
        }

        public void setFood(Food food) {
            this.food = food;
        }

        public Group getGroup() {
            return group;
        }

        public void setGroup(Group group) {
            this.group = group;
        }
    }
}
