package com.wj.kindergarten.ui.messagepag;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
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
import com.wj.kindergarten.ui.func.NoticeActivity;
import com.wj.kindergarten.ui.func.SignListActivity;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.ui.message.MessageAdapter;
import com.wj.kindergarten.ui.mine.photofamilypic.BoutiqueSingleInfoActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.TransportListener;
import com.wj.kindergarten.ui.mine.photofamilypic.dbupdate.DbUtils;
import com.wj.kindergarten.ui.more.HtmlActivity;
import com.wj.kindergarten.ui.webview.SchoolIntroduceActivity;
import com.wj.kindergarten.ui.webview.WebviewActivity;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.FinalUtil;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.List;


public class MessageSunFragment extends Fragment {
    private View rootView;
    private PullToRefreshListView mListView;

    private MessageAdapter adapter;
    private ArrayList<MsgDataModel> dataList = new ArrayList<MsgDataModel>();
    private int nowPage = 1;
    private RelativeLayout message_list_rl;
    private static final int SET_REFRESH = 10010;
    private FinalDb albumDb;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_interaction, null);
            albumDb = FinalUtil.getFamilyUuidObjectDb(getActivity());
            message_list_rl = (RelativeLayout) rootView.findViewById(R.id.message_list_rl);
            mListView = (PullToRefreshListView) rootView.findViewById(R.id.pulltorefresh_list_interation);
            mListView.setMode(PullToRefreshBase.Mode.BOTH);
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
                    queryMessage(++nowPage);
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
                        if (Utils.stringIsNull(dataModel.getRel_uuid())) return;
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
                            intent.putExtra("url", dataModel.getUrl());
                            startActivity(intent);
                        } else if (dataModel.getType() == 13) {
                            startActivity(new Intent(getActivity(), SignListActivity.class));
                        } else if (dataModel.getType() == 99) {
                            startActivity(new Intent(getActivity(), InteractionListActivity.class));
                        } else if (dataModel.getType() == 20) {
                            ((MainActivity) getActivity()).setCurrentTab(3);
                        } else if (dataModel.getType() == 21) {
                            //照片内容
                            List<AllPfAlbumSunObject> objectList = DbUtils.getAllPic(albumDb);
                            AllPfAlbumSunObject object = new AllPfAlbumSunObject();
                            object.setUuid(dataModel.getRel_uuid());
                            if (objectList != null && objectList.size() > 0) {
                                int positionList = objectList.indexOf(object);
                                if (positionList < 0) positionList = 0;
                                new TransportListener(getActivity(), positionList, objectList, null).onItemClick(parent, view, positionList, id);
                            }
                        } else if (dataModel.getType() == 22) {
                            //精品相册内容
                            String uuid = dataModel.getRel_uuid();
                            if (uuid != null && !TextUtils.isEmpty(uuid)) {
                                Intent intent = new Intent(getActivity(), BoutiqueSingleInfoActivity.class);
                                intent.putExtra("uuid", uuid);
                                MainActivity.instance.startActivityForResult(intent, GloablUtils.DELETE_BOUTIQUE_ALBUM_SUCCESSED);
                            }
                        }
                    }
                }
            });
            mHandler.sendEmptyMessageDelayed(SET_REFRESH, 0);
        }
        if (!CGSharedPreference.getMessageState()) {
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
        ((MainActivity) getActivity()).getDialog().show();

        UserRequest.queryMessage(getActivity(), page, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                if (((MainActivity) getActivity()).getDialog().isShowing()) {
                    ((MainActivity) getActivity()).getDialog().cancel();
                }
                if (mListView.isRefreshing()) {
                    mListView.onRefreshComplete();
                }
                Msg msg = (Msg) domain;
                if (msg != null && msg.getList() != null && msg.getList().getData() != null &&
                        msg.getList().getData().size() > 0) {
                    if (page == 1) {
                        dataList.clear();
                    }
                    dataList.addAll(msg.getList().getData());
                    adapter.notifyDataSetChanged();
                    nowPage = page;
                } else {
                    if (page == 1) {
//                        ((BaseActivity) getActivity()).noView(message_list_rl);
                    } else {
                        ToastUtils.showMessage("没有更多内容了!");
                        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
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
