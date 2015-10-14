package com.wj.kindergarten.ui.webview;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.SpecialCourseInfoList;
import com.wj.kindergarten.bean.SpecialCourseInfoObject;
import com.wj.kindergarten.bean.SpecialCourseType;
import com.wj.kindergarten.bean.SpecialCourseTypeList;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.SpecialCourseDetailActivity;
import com.wj.kindergarten.ui.func.SpecialCourseInfoActivity;
import com.wj.kindergarten.ui.func.adapter.SpecialCourseGrid;
import com.wj.kindergarten.ui.func.adapter.SpecialCourseListAdapter;
import com.wj.kindergarten.utils.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * WebviewActivity
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-17 20:38
 */
public class WebviewActivity extends BaseActivity implements Serializable{

    private WebView webView = null;
    private String url = "";
    private LinearLayout layoutWebview = null;
    private RelativeLayout layoutLoad = null;
    private ImageView ivLoadFailure = null;
    private TextView tvLoadInfo = null;
    private ProgressBar progressBar = null;
    private GridView courseGrid;
    private PullToRefreshListView special_listView;

    private List<SpecialCourseType> sctlist = new ArrayList<>();
    private List<SpecialCourseInfoObject> allCourse = new ArrayList<>();
    private boolean isFirst;
    private SpecialCourseListAdapter listAdapter;

    public List<SpecialCourseType> getSctList(){
        return sctlist;
    }

    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //获取数据完毕，更新页面
                case 100 :
                    loadSuc();
                    break;
            }
        }
    };

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_webview;
    }

    @Override
    protected void setNeedLoading() {

        isNeedLoading  = true;
    }

    @Override
    protected void onCreate() {
        String title = getIntent().getStringExtra("title");
        setTitleText(title);
        setViews();

    }

    @Override
    protected void loadData() {
         //网络获取数据表格数据
        getClassSort();
        getHotCourse();

    }

    int pageNo = 1;
    int type = 0;
    private void getHotCourse() {
        UserRequest.getSpecialCourseInfoFormType(this,"",pageNo ,type, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                SpecialCourseInfoList list = (SpecialCourseInfoList) domain;
                //如果为课程分类，加入到course集合中，如果为学校分类，加入到school集合中，如果为全部课程加入到allcourse
                //暂定英语分类
                //如果课程获取类别不一样，则清空集合
                allCourse.clear();
                allCourse.addAll(list.getList().getData());
//

                if (!isFirst) {
                    loadSuc();
                } else {
                    if(special_listView.isRefreshing()) {
                        special_listView.onRefreshComplete();
                    }
                    listAdapter.setSepcialList(allCourse);
                }

                isFirst = true;
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });

    }


    private void getClassSort() {
        UserRequest.getSpecialCourseType(this, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                SpecialCourseTypeList sct = (SpecialCourseTypeList) domain;
                sctlist.addAll(sct.getList());
                mhandler.sendEmptyMessage(100);
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

                ToastUtils.showMessage(message);
            }
        });
    }



    private void setViews() {
        courseGrid = (GridView)findViewById(R.id.special_course_grid_view);
        courseGrid.setAdapter(new SpecialCourseGrid(this,sctlist));
        special_listView = (PullToRefreshListView)findViewById(R.id.pulltorefresh_list);
        listAdapter = new SpecialCourseListAdapter(this);
        special_listView.setAdapter(listAdapter);
        listAdapter.setSepcialList(allCourse);

        courseGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击启动另一个页面
                Intent intent = new Intent(WebviewActivity.this, SpecialCourseDetailActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        special_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WebviewActivity.this, SpecialCourseInfoActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
    }











}
