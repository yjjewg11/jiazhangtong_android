package com.wj.kindergarten.ui.webview;

import android.content.Intent;
import android.graphics.Color;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.TrainSchoolInfo;
import com.wj.kindergarten.bean.TrainSchoolInfoListFather;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.SchoolHtmlActivity;
import com.wj.kindergarten.ui.func.adapter.AboutSchoolAdatper;
import com.wj.kindergarten.ui.func.adapter.OwnAdapter;
import com.wj.kindergarten.utils.HintInfoDialog;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.support.design.widget.TabLayout.*;

/**
 * SchoolIntroduceActivity
 *
 * @Description:xxx
 * @Author: tang
 * @CreateDate: 2015-08-17 21:58
 */
public class SchoolIntroduceActivity extends BaseActivity {

    private PullToRefreshListView listView;
    private AboutSchoolAdatper adapter;
    private HintInfoDialog dialog;
    private LinearLayout normal_title_left_layout;
    public static final int SUCCESS_GET_DATA = 100;

    private TextView title_webview_normal_text;
    private TextView title_webview_normal_spinner;
    private TextView normal_title_right_text;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESS_GET_DATA:
                    break;
            }
        }
    };
    private boolean isFirst;
    private boolean isFirstAssess;
    private boolean isFirstDistance;
    private FrameLayout pulllist_fl;
    private RadioButton child_go_choose_sort;
    private ListView listView_choose;
    private OwnAdapter ownAdapter;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_school_introduce;
    }

    @Override
    protected void setNeedLoading() {

        dialog = new HintInfoDialog(this);
    }




    private List<String> listString = Arrays.asList(new String[]{"智能排序", "评价最高", "离我最近"});
    @Override
    protected void onCreate() {

        pulllist_fl = (FrameLayout)findViewById(R.id.pulllist_fl);
        normal_title_left_layout = (LinearLayout)findViewById(R.id.normal_title_left_layout);
        normal_title_left_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_webview_normal_text = (TextView)findViewById(R.id.title_webview_normal_text);
        title_webview_normal_text.setText("招生中心");
        title_webview_normal_spinner = (TextView)findViewById(R.id.title_webview_normal_spinner);
        title_webview_normal_spinner.setVisibility(View.GONE);
        child_go_choose_sort = (RadioButton)findViewById(R.id.child_go_choose_sort);
        child_go_choose_sort.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(getApplicationContext(), R.layout.window_list, null);

                listView_choose = (ListView) view.findViewById(R.id.window_lsit);
                ownAdapter = new OwnAdapter(SchoolIntroduceActivity.this);
                ownAdapter.setList(listString);
                listView_choose.setAdapter(ownAdapter);
                final PopupWindow popupWindowss = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                popupWindowss.setAnimationStyle(R.style.ShareAnimBase);
                popupWindowss.setFocusable(true);
                popupWindowss.setTouchable(true);
                popupWindowss.setOutsideTouchable(true);
                popupWindowss.getContentView().setFocusableInTouchMode(true);
                popupWindowss.getContentView().setFocusable(true);
                popupWindowss.setBackgroundDrawable(new BitmapDrawable());
                popupWindowss.update();
                popupWindowss.showAsDropDown(v, 0, 0);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        popupWindowss.dismiss();
                    }
                });

                listView_choose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        popupWindowss.dismiss();
                        if (pulllist_fl.findViewById(R.id.pulltorefresh_listview) == null) {
                            pulllist_fl.removeAllViews();
                            if (listView.getParent() != null) {
                                ((ViewGroup) (listView.getParent())).removeView(listView);
                            }
                            pulllist_fl.addView(listView);
                        }
                        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

                        sort = srots[position];
                        child_go_choose_sort.setText(listString.get(position));
                        switch (position) {
                            case 0:

                                break;
                            case 1:
                                //判断是否是第一次点击，如果是加载数据，不是则刷新集合.
                                if (!isFirstAssess) {
                                    isFirstAssess = true;

                                    loadData(assessPage);
                                } else {
                                    adapter.setList(assessList);
                                }

                                break;
                            case 2:
                                //判断是否是第一次点击，如果是加载数据，不是则刷新集合.
                                if (!isFirstDistance) {
                                    isFirstDistance = true;
                                    loadData(distancePage);
                                } else {
                                    adapter.setList(distanceList);
                                }
                                break;
                        }
                    }
                });
            }
        });


        listView = (PullToRefreshListView)findViewById(R.id.pulltorefresh_listview);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listView.setBackgroundColor(Color.parseColor("#ffffff"));

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉加载刷新

                String text = child_go_choose_sort.getText().toString();

                if(text.equals(listString.get(0))){
                    paiXuPage++;
                    loadData(paiXuPage);
                }else if(text.equals(listString.get(1))){
                    assessPage++;
                    loadData(assessPage);
                }else if(text.equals(listString.get(2))){
                    distancePage++;
                    loadData(distancePage);
                }

            }
        });
        adapter = new AboutSchoolAdatper(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击跳转详情页面

                Intent intent = new Intent(SchoolIntroduceActivity.this, SchoolHtmlActivity.class);
                //点击带入对象，因为那边要显示信息
                intent.putExtra("uuid",((TrainSchoolInfo)adapter.getItem(position-1)).getUuid());
                startActivity(intent);
            }
        });
        loadData(paiXuPage);
    }

    private String [] srots = new String[]{
            //智能，评价最高，距离最近
            "intelligent","appraise","distance"
    };
    String sort = srots[0];
    private List<TrainSchoolInfo> paiXuList = new ArrayList<>();
    private List<TrainSchoolInfo> assessList = new ArrayList<>();
    private List<TrainSchoolInfo> distanceList = new ArrayList<>();
    int paiXuPage = 1;
    int assessPage = 1;
    int distancePage = 1;

    protected void loadData(final int page) {


        dialog.show();
        //查询列表数据
        UserRequest.getSchoolAbout(this, "", page, sort, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                if(dialog.isShowing()){
                    dialog.cancel();
                }
                if(listView.isRefreshing()){
                    listView.onRefreshComplete();
                }
                TrainSchoolInfoListFather tsif = (TrainSchoolInfoListFather) domain;
                if (tsif != null && tsif.getList() != null && tsif.getList().getData() != null &&
                        tsif.getList().getData().size() > 0) {

                    if (sort.equals(srots[0])) {
                        paiXuList.addAll(tsif.getList().getData());
                        adapter.setList(paiXuList);
                    } else if (sort.equals(srots[1])) {
                        assessList.addAll(tsif.getList().getData());
                        adapter.setList(assessList);
                    } else if (sort.equals(srots[2])) {
                        distanceList.addAll(tsif.getList().getData());
                        adapter.setList(distanceList);
                    }

                } else {
                    if(page == 1){
//                        noView(pulllist_fl);
                    }else{
                        commonClosePullToRefreshListGridView(listView);
                    }
                }
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