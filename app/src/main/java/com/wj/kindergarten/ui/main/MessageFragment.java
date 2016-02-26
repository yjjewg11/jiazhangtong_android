package com.wj.kindergarten.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Msg;
import com.wj.kindergarten.bean.MsgDataModel;
import com.wj.kindergarten.bean.Teacher;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.addressbook.LeaderMessageActivty;
import com.wj.kindergarten.ui.addressbook.TeacherMessageActivty;
import com.wj.kindergarten.ui.func.ArticleActivity;
import com.wj.kindergarten.ui.func.CourseListActivity;
import com.wj.kindergarten.ui.func.FoodListActivity;
import com.wj.kindergarten.ui.func.InteractionListActivity;
import com.wj.kindergarten.ui.func.NormalReplyListActivity;
import com.wj.kindergarten.ui.func.NoticeActivity;
import com.wj.kindergarten.ui.func.SignListActivity;
import com.wj.kindergarten.ui.message.MessageAdapter;
import com.wj.kindergarten.ui.more.HtmlActivity;
import com.wj.kindergarten.ui.webview.SchoolIntroduceActivity;
import com.wj.kindergarten.ui.webview.WebviewActivity;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * MainFragment
 *
 * @Description: 消息
 * @Author: Wave
 * @CreateDate: 2015/7/17 11:34
 */
public class MessageFragment extends Fragment {
    private View rootView;
    private PullToRefreshListView mListView;

    private MessageAdapter adapter;
    private ArrayList<MsgDataModel> dataList = new ArrayList<MsgDataModel>();
    private int nowPage = 1;
    private RelativeLayout message_list_rl;
    private static final int SET_REFRESH = 10010;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((BaseActivity) getActivity()).clearCenterIcon();
        ((MainActivity) getActivity()).clearLeftIcon();
        ((MainActivity) getActivity()).clearRightIcon();
        ((BaseActivity) getActivity()).setTitleText("消息");

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_interaction, null);

            message_list_rl = (RelativeLayout)rootView.findViewById(R.id.message_list_rl);
            mListView = (PullToRefreshListView) rootView.findViewById(R.id.pulltorefresh_list_interation);
            mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
            mListView.setDividerPadding(0);
            mListView.setDividerDrawable(null);
            mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                    nowPage = 1;
                    queryMessage(nowPage);
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    queryMessage(nowPage++);
                }
            });

            adapter = new MessageAdapter(getActivity(), dataList);
            mListView.setDividerDrawable(getResources().getDrawable(R.color.line));
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MsgDataModel dataModel = dataList.get(position - 1);
                    if (null != dataModel) {
                        readMessage(dataModel);
                        if (dataModel.getType() == 0 || dataModel.getType() == 1) {
                            Intent intent = new Intent(getActivity(), NoticeActivity.class);
                            intent.putExtra("uuid", dataModel.getRel_uuid());
                            getActivity().startActivity(intent);
                        } else if (dataModel.getType() == 3) {
                            Intent intent = new Intent(getActivity(), ArticleActivity.class);
                            intent.putExtra("uuid", dataModel.getRel_uuid());
                            getActivity().startActivity(intent);
                        } else if (dataModel.getType() == 4) {
                            Intent intent = new Intent(getActivity(), SchoolIntroduceActivity.class);
                            intent.putExtra("type", 2);
                            intent.putExtra("uuid", dataModel.getRel_uuid());
                            getActivity().startActivity(intent);
                        } else if (dataModel.getType() == 5) {
                            Intent intent1 = new Intent(getActivity(), WebviewActivity.class);
                            intent1.putExtra("title", "特长课程");
                            intent1.putExtra("url", "http://jz.wenjienet.com/px-mobile/px/index.html");
                            getActivity().startActivity(intent1);
                        } else if (dataModel.getType() == 6) {
                            getActivity().startActivity(new Intent(getActivity(), FoodListActivity.class));
                        } else if (dataModel.getType() == 7) {
                            getActivity().startActivity(new Intent(getActivity(), CourseListActivity.class));
                        } else if (dataModel.getType() == 8) {
                            Intent intent = new Intent(getActivity(), SchoolIntroduceActivity.class);
                            intent.putExtra("type", 1);
                            intent.putExtra("uuid", dataModel.getRel_uuid());
                            getActivity().startActivity(intent);
                        } else if (dataModel.getType() == 11) {
                            Teacher teacher = new Teacher();
                            teacher.setTeacher_uuid(dataModel.getRel_uuid());
                            teacher.setIsFormMessage(true);
                            Intent intent = new Intent(getActivity(), TeacherMessageActivty.class);
                            intent.putExtra("teacher", teacher);
                            getActivity().startActivity(intent);
                        } else if (dataModel.getType() == 12) {
                            Teacher teacher = new Teacher();
                            teacher.setTeacher_uuid(dataModel.getRel_uuid());
                            teacher.setIsFormMessage(true);
                            Intent intent = new Intent(getActivity(), LeaderMessageActivty.class);
                            intent.putExtra("teacher", teacher);
                            getActivity().startActivity(intent);
                        } else if (dataModel.getType() == 10) {
                            CGLog.d("URL:" + dataModel.getUrl());
                                Intent intent = new Intent(getActivity(), HtmlActivity.class);
                                intent.putExtra("url",dataModel.getUrl());
                                startActivity(intent);
                        } else if (dataModel.getType() == 13) {
                            startActivity(new Intent(getActivity(), SignListActivity.class));
                        } else if (dataModel.getType() == 99) {
                            startActivity(new Intent(getActivity(), InteractionListActivity.class));
                        }
                    }
                }
            });
            mHandler.sendEmptyMessageDelayed(SET_REFRESH,0);
        }
        if(!CGSharedPreference.getMessageState()){
            mHandler.sendEmptyMessage(SET_REFRESH);
        }
        return rootView;
    }

    private void readMessage(final MsgDataModel dataModel) {
        if (dataModel.getIsread() == 0) {
            UserRequest.readMessage(getActivity(), dataModel.getUuid(), new RequestResultI() {
                @Override
                public void result(BaseModel domain) {
                    dataModel.setIsread(1);
                    adapter.notifyDataSetChanged();
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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SET_REFRESH:
                    queryMessage(1);
                    break;

            }
        }
    };

    public void loadMessage() {
//        nowPage = 1;
//        mHandler.sendEmptyMessageDelayed(0, 100);
//         queryMessage(nowPage);
    }

    private void queryMessage(final int page) {
        ((MainActivity)getActivity()).getDialog().show();

        UserRequest.queryMessage(getActivity(), page, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                if(((MainActivity)getActivity()).getDialog().isShowing()){
                ((MainActivity)getActivity()).getDialog().cancel();
                }
                if (mListView.isRefreshing()) {
                    mListView.onRefreshComplete();
                }
                Msg msg = (Msg) domain;
                if (msg != null && msg.getList() != null && msg.getList().getData()!= null &&
                        msg.getList().getData().size() > 0) {
                    if (page == 1) {
                        dataList.clear();
                    }
                    dataList.addAll(msg.getList().getData());
                    adapter.notifyDataSetChanged();
                    nowPage = page;
                } else {
                    if(page == 1){
                        ((BaseActivity)getActivity()).noView(message_list_rl);
                    }
                    Utils.showToast(CGApplication.getInstance(), "消息列表为空");
                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(CGApplication.getInstance(), message);
                }
                if (mListView.isRefreshing()) {
                    mListView.onRefreshComplete();
                }
            }
        });
    }
}
