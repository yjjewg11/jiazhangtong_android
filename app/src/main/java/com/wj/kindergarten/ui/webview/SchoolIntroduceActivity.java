package com.wj.kindergarten.ui.webview;

import android.content.Intent;
import android.graphics.Color;

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
import com.wj.kindergarten.utils.HintInfoDialog;


import java.util.ArrayList;
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

    private TabLayout tab_layout;
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

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_school_introduce;
    }

    @Override
    protected void setNeedLoading() {

        dialog = new HintInfoDialog(this);
    }




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
        title_webview_normal_spinner.setText("成都市");
        title_webview_normal_spinner.setTextSize(14);

        tab_layout = (TabLayout)findViewById(R.id.common_tab_layout);
        tab_layout.addTab(tab_layout.newTab().setText("智能排序"));
        tab_layout.addTab(tab_layout.newTab().setText("评价最高"));
        tab_layout.addTab(tab_layout.newTab().setText("离我最近"));

        tab_layout.setOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                //判断容器内是否是listview
                if(pulllist_fl.findViewById(R.id.pulltorefresh_listview) == null){
                    pulllist_fl.removeAllViews();
                    if(listView.getParent() != null){
                        ((ViewGroup)(listView.getParent())).removeView(listView);
                    }
                    pulllist_fl.addView(listView);
                }
                listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                switch (tab.getPosition()) {
                    case 0:
                        sort = srots[0];
                        break;
                    case 1:
                        sort = srots[1];
                        //判断是否是第一次点击，如果是加载数据，不是则刷新集合.
                        if(!isFirstAssess){
                            isFirstAssess = true;

                            loadData(assessPage);
                        }else{
                            adapter.setList(assessList);
                        }

                        break;
                    case 2:
                        sort = srots[2];
                        //判断是否是第一次点击，如果是加载数据，不是则刷新集合.
                        if(!isFirstDistance){
                            isFirstDistance = true;

                            loadData(distancePage);
                        }else{
                            adapter.setList(distanceList);
                        }
                        break;
                }

            }

            @Override
            public void onTabUnselected(Tab tab) {

            }

            @Override
            public void onTabReselected(Tab tab) {

            }
        });
        listView = (PullToRefreshListView)findViewById(R.id.pulltorefresh_listview);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listView.setBackgroundColor(Color.parseColor("#f6f6f6"));

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉加载刷新

                switch (tab_layout.getSelectedTabPosition()){
                    case 0:
                        paiXuPage++;
                        loadData(paiXuPage);
                        break;
                    case 1:
                        assessPage++;
                        loadData(assessPage);
                        break;
                    case 2:
                        distancePage++;
                        loadData(distancePage);
                        break;
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
                intent.putExtra("object",(TrainSchoolInfo)adapter.getItem(position));
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