package com.wj.kindergarten.ui.main;

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
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.FoundHotSelectionFather;
import com.wj.kindergarten.bean.FoundHotSelectionSun;
import com.wj.kindergarten.bean.FoundTypeCount;
import com.wj.kindergarten.bean.FoundTypeCountSun;
import com.wj.kindergarten.bean.MainTopic;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.compounets.NestedGridView;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.func.ArticleListActivity;
import com.wj.kindergarten.ui.func.adapter.FoundGridAdapter;
import com.wj.kindergarten.ui.func.adapter.NstGridPicAdapter;
import com.wj.kindergarten.ui.mine.PrivilegeActiveActivity;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2015/12/8.
 */
public class FoundSunFragment extends Fragment {
    private View view;
    private GridView found_grid_view;
    private TextView tv_everyday_topic;
    private TextView tv_everyday_recommon;
    private LinearLayout contain_hot_list;
    private FoundGridAdapter adapter;
    private PullToRefreshScrollView scrollView;
    private static final int ADD_MORE_DATA = 1021;
    private static final int LOAD_DATA_FINISH = 1024;
    private ArrayList<FoundHotSelectionSun> list = new ArrayList<>();

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ADD_MORE_DATA:
                    //先添加10个假数据
                    for(int i = 0 ; i < list.size() ; i++){
                        View view = View.inflate(getActivity(),R.layout.found_hot_list_item,null);
                        RelativeLayout found_hot_selection_rl = (RelativeLayout) view.findViewById(R.id.found_hot_selection_rl);

                        final FoundHotSelectionSun selectionSun = list.get(i);

                        if(selectionSun != null){

                        view.setOnClickListener(new PublicCommonClick(){
                            @Override
                            public void openUrl() {
                                if(!TextUtils.isEmpty(selectionSun.getWebview_url())){
                                    FoundFragment.instance.showWeb().setWebUrl(selectionSun.getWebview_url());
                                }
                            }
                        });

                        TextView tv_found_hot_theme = (TextView) view.findViewById(R.id.tv_found_hot_theme);
                        tv_found_hot_theme.getPaint().setFakeBoldText(true);
                        TextView tv_found_hot_count = (TextView) view.findViewById(R.id.tv_found_hot_count);
                        TextView tv_found_content = (TextView) view.findViewById(R.id.tv_found_content);

                            tv_found_hot_theme.setText(Utils.isNull(selectionSun.getTitle()));
                            tv_found_hot_count.setText(""+selectionSun.getYes_count());
                            tv_found_content.setText(""+(TextUtils.isEmpty(Utils.isNull(selectionSun.getSummary())) == true ?
                                    "暂无内容!" :Utils.isNull(selectionSun.getSummary())));


                        NestedGridView item_found_hot_pic = (NestedGridView) view.findViewById(R.id.item_found_hot_pic);

                        if(selectionSun.getImgList() != null && selectionSun.getImgList().size() > 0){
                            NstGridPicAdapter nGAdapter = new NstGridPicAdapter(selectionSun.getImgList(), getActivity());
                            item_found_hot_pic.setAdapter(nGAdapter);
                            item_found_hot_pic.setVisibility(View.VISIBLE);
                        }else{

                            item_found_hot_pic.setVisibility(View.INVISIBLE);
                            LinearLayout.LayoutParams paramsGrid = (LinearLayout.LayoutParams)item_found_hot_pic.getLayoutParams();
                            paramsGrid.bottomMargin = 0;
                            item_found_hot_pic.setLayoutParams(paramsGrid);
                        }

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                               LinearLayout.LayoutParams.WRAP_CONTENT);
                        contain_hot_list.addView(view,params);

                        }


                    }
                    break;
                //加载数据完毕，关闭加载框
                case LOAD_DATA_FINISH:
                    if(((MainActivity)getActivity()).getDialog().isShowing()){
                        ((MainActivity)getActivity()).getDialog().cancel();
                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view != null) return view;

        view = inflater.inflate(R.layout.found_sun_fragment,null);

        scrollView = (PullToRefreshScrollView)view.findViewById(R.id.scroll_found_fragment);
        found_grid_view = (GridView)view.findViewById(R.id.found_grid_view);
        tv_everyday_recommon = (TextView)view.findViewById(R.id.tv_everyday_recommon);
        //加粗字体
        tv_everyday_recommon.getPaint().setFakeBoldText(true);

        tv_everyday_topic = (TextView)view.findViewById(R.id.tv_everyday_topic);
        final MainTopic t = CGSharedPreference.getMainTopic();
        tv_everyday_topic.setText("" + (TextUtils.isEmpty(t.getTitle()) == true ? "暂无推荐" : t.getTitle()));
        tv_everyday_topic.setOnClickListener(new PublicCommonClick() {
            @Override
            public void openUrl() {
                if(!TextUtils.isEmpty(t.getUrl())){
                    FoundFragment.instance.showWeb().setWebUrl(t.getUrl());
                    //发送点击次数
                    UserRequest.clickAndRefreshTopic(getActivity());
                }

            }
        });
        contain_hot_list = (LinearLayout)view.findViewById(R.id.contain_hot_list);
        adapter = new FoundGridAdapter(getActivity());
        found_grid_view.setAdapter(adapter);
        found_grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getActivity(), ArticleListActivity.class));
                        break;
                    case 1:
                        //切换话题WebviewFragment
                        if(!TextUtils.isEmpty(CGSharedPreference.getMainTopicUrl())){
                        FoundFragment.instance.showWeb().setWebUrl(CGSharedPreference.getMainTopicUrl());
                            return;
                        }
                        break;
                    case 2:
                        startActivity(new Intent(getActivity(), PrivilegeActiveActivity.class));
                        break;
                }
            }
        });
        scrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                //上拉加载数据
                pageNo++;
                getHotSeclection();
                scrollView.onRefreshComplete();
            }
        });
        loadData();

        return view;
    }

    private void loadData() {
        getCount();
        getHotSeclection();
    }

    int pageNo = 1;
    //获取热门精选列表
    private void getHotSeclection() {
         show();
         UserRequest.getFoundHotSeclection(getActivity(), pageNo, new RequestResultI() {
             @Override
             public void result(BaseModel domain) {
                 handler.sendEmptyMessage(LOAD_DATA_FINISH);
                 FoundHotSelectionFather selection = (FoundHotSelectionFather) domain;
                 if (selection != null && selection.getList() != null &&
                         selection.getList().getData() != null && selection.getList().getData().size() > 0) {
                     list.clear();
                     list.addAll(selection.getList().getData());
                     handler.sendEmptyMessage(ADD_MORE_DATA);
                 } else {
                     if (pageNo == 1) {
                         TextView textView = new TextView(getActivity());
                         textView.setText("当前热门暂无数据,看看别的吧！");
                         contain_hot_list.addView(textView);
                     } else {
                         ((MainActivity) getActivity()).commonClosePullToRefreshScrollView(scrollView, 2);
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

    private void show(){
        ((MainActivity)getActivity()).getDialog().show();
    }

    private void cancle(){
        if(((MainActivity)getActivity()).getDialog().isShowing()){
            ((MainActivity)getActivity()).getDialog().cancel();
        }
    }

    private void getCount() {
        show();
        UserRequest.getTypeCount(getActivity(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                //获取成功，更改数量
                FoundTypeCount foundTypeCount = (FoundTypeCount) domain;
                if (foundTypeCount != null) {
                    FoundTypeCountSun sun = foundTypeCount.getData();
                    adapter.getList().get(0).setCount(sun.getToday_goodArticle());
                    adapter.getList().get(1).setCount(sun.getToday_snsTopic());
                    adapter.getList().get(2).setCount(sun.getToday_pxbenefit());
                    adapter.notifyDataSetChanged();
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

    abstract class PublicCommonClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {

                openUrl();

        }

        public abstract void openUrl();
    }
}
