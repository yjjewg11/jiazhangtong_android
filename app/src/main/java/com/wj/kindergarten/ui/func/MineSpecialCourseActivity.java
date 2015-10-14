package com.wj.kindergarten.ui.func;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.MineCourseStatusAdapter;

public class MineSpecialCourseActivity extends BaseActivity{
    private PullToRefreshListView mListView;
    private MineCourseStatusAdapter adapter;

    @Override
    protected void setContentLayout() {

        layoutId = R.layout.activity_mine_special_course;
    }

    @Override
    protected void setNeedLoading() {
//               isNeedLoading = true;
    }

    @Override
    protected void onCreate() {
               titleCenterTextView.setText("我的特长课程");
        initViews();

    }

    int pageNo = 1;
    private void initViews() {
        mListView = (PullToRefreshListView) findViewById(R.id.pulltorefresh_list);
        adapter = new MineCourseStatusAdapter(this);
        mListView.setAdapter(adapter);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNo++;

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MineSpecialCourseActivity.this,MineCourseDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void loadData() {

        //暂无法请求数据，没有接口

    }
}
