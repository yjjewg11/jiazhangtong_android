package com.wj.kindergarten.ui.webview;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.umeng.socialize.utils.Log;
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
import com.wj.kindergarten.ui.func.adapter.SpinnerAreaAdapter;
import com.wj.kindergarten.ui.map.ClickStartMap;
import com.wj.kindergarten.ui.map.MapTransportFactory;
import com.wj.kindergarten.ui.map.MapTransportObject;
import com.wj.kindergarten.ui.other.CustomScrollView;
import com.wj.kindergarten.ui.other.FullGridView;
import com.wj.kindergarten.ui.other.RatingBarView;
import com.wj.kindergarten.utils.Constant.MessageConstant;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.ShareUtils;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

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
    private FullGridView courseGrid;
    private LinearLayout special_listView;

    private ArrayList<SpecialCourseType> sctlist = new ArrayList<>();
    private List<SpecialCourseInfoObject> allCourse = new ArrayList<>();
    private boolean isFirst;
    private SpecialCourseListAdapter listAdapter;
    private PullToRefreshScrollView scroll_view;
    private PopupWindow mPopupWindow;
    private ImageView title_webview_normal_back;
    private TextView title_webview_normal_spinner;
    private TextView tv_center;
    private SpecialCourseGrid type_adapter;
    private HintInfoDialog dialog;
    private RelativeLayout tv_hot_course;
    private String [] click_events = new String[]{
            MessageConstant.ENGLISH,
            MessageConstant.PAINT,
            MessageConstant.MUSIC,
            MessageConstant.SPORTS,
            MessageConstant.LANGUAGE_ALL,
            MessageConstant.DANCE,
            MessageConstant.INTELLIGENCE,
    };

    public List<SpecialCourseType> getSctList(){
        return sctlist;
    }

    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //获取数据完毕，更新页面
                case 100 :
                    type_adapter.setList(sctlist);
                    break;
                case 200:
                    //添加热门课程数据
                    for(final SpecialCourseInfoObject object : allCourse){
                        View view = View.inflate(WebviewActivity.this,R.layout.item_special_course_list_view,null);
                        ImageView imageView = (ImageView) view.findViewById(R.id.item_special_course_list_view_image_view);
                        RatingBarView ratingBar = (RatingBarView) view.findViewById(R.id.item_special_course_list_view__rating_bar);
                        TextView item_special_course_list_view_tv_adresss = (TextView) view.findViewById(R.id.item_special_course_list_view_tv_adresss);
                        TextView item_class_name = (TextView) view.findViewById(R.id.item_class_name);
                        TextView item_special_course_list_view_tv_distance = (TextView) view.findViewById(R.id.item_special_course_list_view_tv_distance);
                        TextView item_special_course_list_view_tv_edcucation = (TextView) view.findViewById(R.id.item_special_course_list_view_tv_edcucation);
                        TextView item_special_course_list_view_tv_study_people = (TextView) view.findViewById(R.id.item_special_course_list_view_tv_study_people);
                        ratingBar.setFloatStar(object.getCt_stars(), true);
                        ImageLoaderUtil.displayMyImage(object.getLogo(), imageView);

                        item_special_course_list_view_tv_adresss.setOnClickListener(new ClickStartMap(WebviewActivity.this, MapTransportFactory.createMapTransport(object.getMap_point(), object.getLogo()
                                , object.getGroup_name(), object.getAddress())));
                        item_class_name.setText("" + object.getTitle());
                        item_special_course_list_view_tv_adresss.setText("" + object.getAddress());
                        //TODO
                        String text = "<font  color='#ff4966'>"+object.getCt_study_students()+"</font>"+"人已学";
                        item_special_course_list_view_tv_distance.setText("" + object.getDistance());
                        MapTransportObject mapTransportObject = new MapTransportObject(object.getMap_point());
                        item_special_course_list_view_tv_distance.setOnClickListener(new ClickStartMap(WebviewActivity.this,mapTransportObject));
                        item_special_course_list_view_tv_edcucation.setText("" + object.getGroup_name());
                        item_special_course_list_view_tv_study_people.setText(Html.fromHtml(text));
                        view.setBackgroundResource(R.drawable.setting_item_click_selector);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(WebviewActivity.this, SpecialCourseInfoActivity.class);
//                                intent.putExtra("position",position);
                                intent.putExtra("object", object);
                                intent.putExtra("uuid",object.getUuid());
                                startActivity(intent);
                            }
                        });

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        special_listView.addView(view, params);

                        TextView tv_line = new TextView(WebviewActivity.this);
                        tv_line.setBackgroundColor(Color.parseColor("#bbbbbb"));
                        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,1);
                        special_listView.addView(tv_line,llp);
                    }
                    if(dialog.isShowing()){dialog.cancel();}

//                    if(mPopupWindow!=null && mPopupWindow.isShowing()) mPopupWindow.dismiss();
                    break;
                case 10:
//                    pageNo++;
//                    getHotCourse();
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


    }

    @Override
    protected void onCreate() {
        setViews();
    }

    @Override
    protected void loadData() {
         //网络获取数据表格数据
        getClassSort();
        getHotCourse();

    }

    int pageNo = 1;
    int type = -1;
    private void getHotCourse() {
        //TODO 热门课程接口暂定
        UserRequest.getHotCourseInfo(this, "", pageNo, type, "", "", new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                allCourse.clear();
                SpecialCourseInfoList list = (SpecialCourseInfoList) domain;
                if (list != null && list.getList() != null && list.getList().getData().size() > 0) {
                    allCourse.addAll(list.getList().getData());
                } else {

                    commonClosePullToRefreshScrollView(scroll_view,pageNo);

                }
                if (scroll_view != null && scroll_view.isRefreshing()) {
                    scroll_view.onRefreshComplete();
                }
                mhandler.sendEmptyMessage(200);
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
    boolean isBottom ;
    boolean isCanLoad = true;

    private void setViews() {
        dialog = new HintInfoDialog(this);
        dialog.show();

        tv_hot_course = (RelativeLayout)findViewById(R.id.tv_hot_course);
        tv_hot_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WebviewActivity.this, SpecialCourseDetailActivity.class);
                intent.putExtra("list", sctlist);
                intent.putExtra("key", -1);
                intent.putExtra("id", 0);
                startActivity(intent);
            }
        });
        title_webview_normal_back = (ImageView)findViewById(R.id.title_webview_normal_back);
        title_webview_normal_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_center = (TextView)findViewById(R.id.title_webview_normal_text);
        tv_center.setText("特长课程");
        title_webview_normal_spinner = (TextView)findViewById(R.id.title_webview_normal_spinner);
        cityChoose(title_webview_normal_spinner);
        special_listView = (LinearLayout)findViewById(R.id.listView_hot_course);
        special_listView.setBackgroundColor(Color.parseColor("#f6f6f6"));
        FrameLayout fl = (FrameLayout) findViewById(R.id.special_ads);
        Utils.ads(this,fl);
        scroll_view = (PullToRefreshScrollView)findViewById(R.id.scroll_view);
        scroll_view.setMode(PullToRefreshBase.Mode.BOTH);
        scroll_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                scroll_view.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageNo++;
                getHotCourse();
            }
        });
        courseGrid = (FullGridView)findViewById(R.id.special_course_grid_view);
        type_adapter = new SpecialCourseGrid(this);
        courseGrid.setAdapter(type_adapter);
        courseGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击启动另一个页面
                Utils.registerUmengClickEvent(click_events[position]);
                startActivity(position);
            }
        });

        loadData();

    }

    private void startActivity(int position) {
        Intent intent = new Intent(WebviewActivity.this, SpecialCourseDetailActivity.class);
        intent.putExtra("list", sctlist);
        intent.putExtra("key", sctlist.get(position).getDatakey());
        intent.putExtra("id", position+1);
        startActivity(intent);
    }
}
