package com.wj.kindergarten.ui.func;

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

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Interaction;
import com.wj.kindergarten.bean.InteractionList;
import com.wj.kindergarten.bean.Reply;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.emot.SendMessage;
import com.wj.kindergarten.ui.emot.ViewEmot2;
import com.wj.kindergarten.ui.func.adapter.CourseInteractionAdapter;
import com.wj.kindergarten.ui.func.adapter.InteractionAdapter;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * MainFragment
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/17 11:34
 */
public class CourseInteractionFragment extends Fragment {

    String newsuuid = "";
    private View rootView;
    private PullToRefreshListView mListView;
    private LinearLayout bottomLayou;
    private ViewEmot2 emot2 = null;
    private Interaction replyInteraction = null;

    private CourseInteractionAdapter courseInteractionAdapter;
    private List<Interaction> dataList = new ArrayList<>();
    private int nowPage = 1;
    private String nowReplyUUID = "";



    public void setNewsuuid(String newsuuid) {
        this.newsuuid = newsuuid;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_interaction, null, false);
            mListView = (PullToRefreshListView) rootView.findViewById(R.id.pulltorefresh_list);
            courseInteractionAdapter = new CourseInteractionAdapter(getActivity(), this, dataList);
            mListView.setDividerDrawable(getResources().getDrawable(R.color.line));
            mListView.setAdapter(courseInteractionAdapter);
            mListView.setMode(PullToRefreshBase.Mode.BOTH);
            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {
                    hideBottomLayout();
                }

                @Override
                public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                }
            });
            mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                    nowPage = 1;
                    getInteractionList(nowPage);
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    getInteractionList(nowPage + 1);
                }
            });

            bottomLayou = (LinearLayout) rootView.findViewById(R.id.interaciton_bottom);
            emot2 = new ViewEmot2(getActivity(), new SendMessage() {
                @Override
                public void send(String message) {
                    sendReply(nowReplyUUID, message);
                }
            });
            bottomLayou.addView(emot2);

            mHandler.sendEmptyMessageDelayed(0, 300);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (!mListView.isRefreshing()) {
                        mListView.setRefreshing();
                    }
                    break;
            }
        }
    };

    public void refreshList() {
        mHandler.sendEmptyMessageDelayed(0, 200);
    }

    public void showReplyLayout(String uuid, Interaction replyInteraction) {
        nowReplyUUID = uuid;
        bottomLayou.setVisibility(View.VISIBLE);
        emot2.showSoftKeyboard();
        this.replyInteraction = replyInteraction;
    }

    public void hideBottomLayout() {
        emot2.hideFaceLayout();
        bottomLayou.setVisibility(View.GONE);
    }

    private void getInteractionList(final int page) {
        UserRequest.getCourseInteractionList(getActivity(), newsuuid, page, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                if (mListView.isRefreshing()) {
                    mListView.onRefreshComplete();
                }
                InteractionList interactionList = (InteractionList) domain;
                if (interactionList != null && interactionList.getList() != null
                        && interactionList.getList().getData() != null && interactionList.getList().getData().size() > 0) {
                    if (page == 1) {
                        dataList.clear();
                    }
                    dataList.addAll(interactionList.getList().getData());
                    courseInteractionAdapter.notifyDataSetChanged();
                    nowPage = page;
                } else {
                    if (mListView.isRefreshing()) {
                        mListView.onRefreshComplete();
                    }
                    //没有互动，替换布局
                    if (page == 1){
                        removeView();
                    }else{
                        ToastUtils.showMessage("没有更多内容了!");
                    }

//                    Utils.showToast(CGApplication.getInstance(), "获取互动列表失败");
                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (mListView.isRefreshing()) {
                    mListView.onRefreshComplete();
                }
                Utils.showToast(CGApplication.getInstance(), message);
            }
        });
    }

    private void removeView() {
       ViewGroup viewGroup = (ViewGroup) rootView.getParent();
        viewGroup.removeView(rootView);
        View view = View.inflate(getActivity(),R.layout.nothing_view,null);
        viewGroup.addView(view);
    }

    private void sendReply(String uuid, final String replyContent) {
        if (Utils.stringIsNull(replyContent)) {
            Utils.showToast(getActivity(), "请输入内容");
            return;
        }
        hideBottomLayout();
        ((CourseInteractionListActivity) getActivity()).showProgressDialog("发表回复中，请稍候...");
        UserRequest.reply(getActivity(), uuid, replyContent.trim(), "",
                ((CourseInteractionListActivity) getActivity()).getType(), new RequestResultI() {
                    @Override
                    public void result(BaseModel domain) {
                        try {
                            ((CourseInteractionListActivity) getActivity()).hideProgressDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        emot2.cleanEditText();
                        bottomLayou.setVisibility(View.GONE);
                        emot2.hideSoftKeyboard();
                        Reply reply = new Reply();
                        reply.setContent(replyContent.trim());
                        reply.setCreate_user(CGApplication.getInstance().getLogin().getUserinfo().getName());
                        courseInteractionAdapter.addReply(replyInteraction, reply);
                    }

                    @Override
                    public void result(List<BaseModel> domains, int total) {

                    }

                    @Override
                    public void failure(String message) {
                        try {
                            ((InteractionListActivity) getActivity()).hideProgressDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Utils.showToast(CGApplication.getInstance(), message);
                    }
                });
    }
}
