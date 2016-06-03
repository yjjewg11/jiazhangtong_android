package com.wj.kindergarten.ui.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
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
import com.wj.kindergarten.ui.other.FullGridView;
import com.wj.kindergarten.ui.other.RatingBarView;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2015/12/8.
 */
public class SpecialCourseFragment extends Fragment {
    /**
     * WebviewActivity
     *
     * @Description:xxx
     * @Author: pengqiang.zou
     * @CreateDate: 2015-08-17 20:38
     */
        private FullGridView courseGrid;
        private LinearLayout special_listView;

        private ArrayList<SpecialCourseType> sctlist = new ArrayList<>();
        private List<SpecialCourseInfoObject> allCourse = new ArrayList<>();
        private boolean isFirst;
        private SpecialCourseListAdapter listAdapter;
        private PullToRefreshScrollView scroll_view;
        private PopupWindow mPopupWindow;
        private TextView title_webview_normal_spinner;
        private TextView tv_center;
        private SpecialCourseGrid type_adapter;
        private HintInfoDialog dialog;
        private RelativeLayout tv_hot_course;
    private View view;

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
                            View view = View.inflate(getActivity(), R.layout.item_special_course_list_view,null);
                            ImageView imageView = (ImageView) view.findViewById(R.id.item_special_course_list_view_image_view);
                            RatingBarView ratingBar = (RatingBarView) view.findViewById(R.id.item_special_course_list_view__rating_bar);
                            TextView item_special_course_list_view_tv_adresss = (TextView) view.findViewById(R.id.item_special_course_list_view_tv_adresss);
                            TextView item_class_name = (TextView) view.findViewById(R.id.item_class_name);
                            TextView item_special_course_list_view_tv_distance = (TextView) view.findViewById(R.id.item_special_course_list_view_tv_distance);
                            TextView item_special_course_list_view_tv_edcucation = (TextView) view.findViewById(R.id.item_special_course_list_view_tv_edcucation);
                            TextView item_special_course_list_view_tv_study_people = (TextView) view.findViewById(R.id.item_special_course_list_view_tv_study_people);
                            ratingBar.setFloatStar(object.getCt_stars(), true);
                            ImageLoaderUtil.displayMyImage(object.getLogo(), imageView);

                            item_class_name.setText("" + object.getTitle());
                            item_special_course_list_view_tv_adresss.setText(""+object.getAddress());
                            //TODO
                            String text = "<font  color='#ff4966'>"+object.getCt_study_students()+"</font>"+"人已学";
                            item_special_course_list_view_tv_distance.setText(""+object.getDistance());
                            item_special_course_list_view_tv_edcucation.setText("" + object.getGroup_name());
                            item_special_course_list_view_tv_study_people.setText(Html.fromHtml(text));
                            view.setBackgroundResource(R.drawable.setting_item_click_selector);
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), SpecialCourseInfoActivity.class);
//                                intent.putExtra("position",position);
                                    intent.putExtra("object", object);
                                    intent.putExtra("uuid",object.getUuid());
                                    startActivity(intent);
                                }
                            });

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            special_listView.addView(view, params);

                            TextView tv_line = new TextView(getActivity());
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

    boolean isOnce;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((BaseActivity) getActivity()).clearCenterIcon();
        ((MainActivity) getActivity()).setTitleText("特长课程");
        if(view != null) return view;

        view = inflater.inflate(R.layout.activity_webview,null);
        if(!isOnce){
            isOnce = true;
            setViews();
        }

        return view;
    }

        protected void loadData() {
            //网络获取数据表格数据
            getClassSort();
            getHotCourse();
        }

        int pageNo = 1;
        int type = -1;
        private void getHotCourse() {
            //TODO 热门课程接口暂定
            UserRequest.getHotCourseInfo(getActivity(), "", pageNo, type, "", "", new RequestResultI() {
                @Override
                public void result(BaseModel domain) {
                    allCourse.clear();
                    SpecialCourseInfoList list = (SpecialCourseInfoList) domain;
                    if (list != null && list.getList() != null && list.getList().getData().size() > 0) {
                        allCourse.addAll(list.getList().getData());
                    } else {

                        ((MainActivity)getActivity()).commonClosePullToRefreshScrollView(scroll_view, pageNo);

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
            UserRequest.getSpecialCourseType(getActivity(), new RequestResultI() {
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

//                    ToastUtils.showMessage(message);/
                }
            });
        }
        boolean isBottom ;
        boolean isCanLoad = true;

        private void setViews() {
            dialog = new HintInfoDialog(getActivity());
            dialog.show();

            tv_hot_course = (RelativeLayout)view.findViewById(R.id.tv_hot_course);
            tv_hot_course.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SpecialCourseDetailActivity.class);
                    intent.putExtra("list", sctlist);
                    intent.putExtra("key", -1);
                    intent.putExtra("id", 0);
                    startActivity(intent);
                }
            });
//            title_webview_normal_back = (ImageView)view.findViewById(R.id.title_webview_normal_back);
//            title_webview_normal_back.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    getActivity().finish();
//                }
//            });
//            tv_center = (TextView)view.findViewById(R.id.title_webview_normal_text);
//            tv_center.setText("特长课程");
//            title_webview_normal_spinner = (TextView)view.findViewById(R.id.title_webview_normal_spinner);
////            cityChoose(title_webview_normal_spinner);
            special_listView = (LinearLayout)view.findViewById(R.id.listView_hot_course);
            special_listView.setBackgroundColor(Color.parseColor("#f6f6f6"));
            FrameLayout fl = (FrameLayout) view.findViewById(R.id.special_ads);
            Utils.ads(getActivity(), fl);
            scroll_view = (PullToRefreshScrollView)view.findViewById(R.id.scroll_view);
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
            courseGrid = (FullGridView) view.findViewById(R.id.special_course_grid_view);
            type_adapter = new SpecialCourseGrid(getActivity());
            courseGrid.setAdapter(type_adapter);
            courseGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //点击启动另一个页面
                    startActivity(position);
                }
            });

            loadData();

        }

        private void startActivity(int position) {
            Intent intent = new Intent(getActivity(), SpecialCourseDetailActivity.class);
            intent.putExtra("list", sctlist);
            intent.putExtra("key", sctlist.get(position).getDatakey());
            intent.putExtra("id", position+1);
            startActivity(intent);
        }
    }


