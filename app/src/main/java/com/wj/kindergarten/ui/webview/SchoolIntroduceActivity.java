package com.wj.kindergarten.ui.webview;

import android.content.Intent;
import android.graphics.Color;

import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.SchoolHtmlActivity;
import com.wj.kindergarten.ui.func.adapter.AboutSchoolAdatper;
import com.wj.kindergarten.utils.HintInfoDialog;


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

    private TextView title_webview_normal_text;
    private TextView title_webview_normal_spinner;
    private TextView normal_title_right_text;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_school_introduce;
    }

    @Override
    protected void setNeedLoading() {

        dialog = new HintInfoDialog(this);
//        isNeedLoading = true;
    }




    @Override
    protected void onCreate() {

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
            }
        });
        adapter = new AboutSchoolAdatper(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击跳转详情页面

                Intent intent = new Intent(SchoolIntroduceActivity.this, SchoolHtmlActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void loadData() {

    }
}