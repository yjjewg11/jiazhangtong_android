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
import com.wj.kindergarten.bean.DianZan;
import com.wj.kindergarten.bean.Food;
import com.wj.kindergarten.bean.FoodList;
import com.wj.kindergarten.bean.Reply;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.utils.URLImageParser;
import com.wj.kindergarten.utils.Utils;

import java.util.List;

/**
 * FoodFragment
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/16 13:45
 */
public class FoodFragment extends Fragment {
    private TextView analysisTv;
    private TextView viewCountTv;
    private ImageView zanIv;
    private ImageView replyIv;
    private TextView zanCountTv;
    private EditText iReplyEt;
    private TextView sendTv;
    private TextView replyMoreTv;
    private LinearLayout replyLayout;
    private LinearLayout replyRootView;


    private Context mContext;
    private String date;
    private Food food;
    private boolean isZanDoing = false;

    public static FoodFragment buildFoodFragment(String date) {
        FoodFragment foodFragment = new FoodFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        View view = View.inflate(mContext, R.layout.fragment_food, null);
        initViews(view);

        return view;
    }

    private void initViews(View view) {
        TextView dateTv = (TextView) view.findViewById(R.id.food_date);
        dateTv.setText(date);

        analysisTv = (TextView) view.findViewById(R.id.food_analysis);
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
        zanIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isZanDoing) {
                    return;
                }
                Drawable drawable = zanIv.getDrawable();
                if (getResources().getDrawable(R.drawable.interaction_zan_off).getConstantState()
                        .equals(drawable.getConstantState())) {
                    zan();
                } else {
                    cancelZan();
                }
            }
        });
        replyIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.sendEmptyMessageDelayed(1, 200);
            }
        });
        replyMoreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NormalReplyListActivity.class);
                intent.putExtra("replyId", food.getUuid());
                startActivity(intent);
            }
        });

        sendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReply();
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    iReplyEt.requestFocus();
                    iReplyEt.setSelection(iReplyEt.getText().length());
                    Utils.inputMethod(mContext, true, iReplyEt);
                    break;
            }
        }
    };

    private void initData() {
        if (food == null) {
            return;
        }
        replyRootView.setVisibility(View.VISIBLE);
        if (food.getAnalysis() != null) {
            analysisTv.setText(food.getAnalysis());
        }
        if (food.getDianzan() != null) {
            DianZan dianZan = food.getDianzan();
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

        if (food.getReplyPage() != null) {
            if (food.getReplyPage() != null && food.getReplyPage().getData() != null) {
                List<Reply> replies = food.getReplyPage().getData();
                addReplyView(replyLayout, replies);

                if (food.getReplyPage().getTotalCount() > food.getReplyPage().getPageSize()) {
                    replyMoreTv.setVisibility(View.VISIBLE);
                }
            }
        }
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
            getFoodList();
        }
    }

    private void showProgressDialog() {
        if (mContext != null) {
            ((FoodListActivity) mContext).showProgressDialog();
        }
    }

    private void hideProgressDialog() {
        if (mContext != null) {
            ((FoodListActivity) mContext).hideProgressDialog();
        }
    }

    private void getFoodList() {
        showProgressDialog();
        String uuid = "";
        if (CGApplication.getInstance().getLogin() != null && CGApplication.getInstance().getLogin().getList() != null
                && CGApplication.getInstance().getLogin().getList().size() > 0) {
            uuid = CGApplication.getInstance().getLogin().getList().get(0).getGroupuuid();
        }
        UserRequest.getFoodList(mContext, date, date, uuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                if (isDetached()) {
                    return;
                }
                hideProgressDialog();
                FoodList foodList = (FoodList) domain;
                if (foodList != null && foodList.getList() != null && foodList.getList().size() > 0) {
                    food = foodList.getList().get(0);
                } else {
                    //no data
                }

                initData();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (isDetached()) {
                    return;
                }
                hideProgressDialog();
            }
        });
    }

    private void zan() {
        isZanDoing = true;
        UserRequest.zan(mContext, food.getUuid(), "0", new RequestResultI() {
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

    private void cancelZan() {
        isZanDoing = true;
        UserRequest.zanCancel(mContext, food.getUuid(), new RequestResultI() {
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

    private void sendReply() {
        UserRequest.reply(mContext, food.getUuid(), iReplyEt.getText().toString().trim(), "", 0, new RequestResultI() {
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
