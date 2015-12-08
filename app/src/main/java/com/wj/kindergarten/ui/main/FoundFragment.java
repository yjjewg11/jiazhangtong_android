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
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.compounets.NestedGridView;
import com.wj.kindergarten.ui.func.ArticleListActivity;
import com.wj.kindergarten.ui.func.adapter.FoundGridAdapter;
import com.wj.kindergarten.ui.func.adapter.NstGridPicAdapter;
import com.wj.kindergarten.ui.mine.PrivilegeActiveActivity;

import java.util.ArrayList;

/**
 * Created by tangt on 2015/12/8.
 */
public class FoundFragment extends Fragment {
    private View view;
    private GridView found_grid_view;
    private TextView tv_everyday_topic;
    private TextView tv_everyday_recommon;
    private LinearLayout contain_hot_list;
    private FoundGridAdapter adapter;
    private PullToRefreshScrollView scrollView;
    private static final int ADD_MORE_DATA = 1021;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ADD_MORE_DATA:
                    //先添加10个假数据
                    for(int i = 0 ; i < 10 ; i++){
                        View view = View.inflate(getActivity(),R.layout.found_hot_list_item,null);
                        TextView tv_found_hot_theme = (TextView) view.findViewById(R.id.tv_found_hot_theme);
                        tv_found_hot_theme.getPaint().setFakeBoldText(true);
                        TextView tv_found_hot_count = (TextView) view.findViewById(R.id.tv_found_hot_count);
                        TextView tv_found_content = (TextView) view.findViewById(R.id.tv_found_content);
                        NestedGridView item_found_hot_pic = (NestedGridView) view.findViewById(R.id.item_found_hot_pic);

                        if(3 > 0){
                            NstGridPicAdapter nGAdapter = new NstGridPicAdapter(new ArrayList<String>(), getActivity());
                            item_found_hot_pic.setAdapter(nGAdapter);
                            item_found_hot_pic.setVisibility(View.VISIBLE);
                        }else{

                            item_found_hot_pic.setVisibility(View.GONE);
                        }
                       LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                               LinearLayout.LayoutParams.WRAP_CONTENT);
                        contain_hot_list.addView(view,params);


                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setTitleText("发现");
        if(view != null) return view;

        view = inflater.inflate(R.layout.found_fragment,null);
        scrollView = (PullToRefreshScrollView)view.findViewById(R.id.scroll_found_fragment);
        found_grid_view = (GridView)view.findViewById(R.id.found_grid_view);
        tv_everyday_recommon = (TextView)view.findViewById(R.id.tv_everyday_recommon);
        //加粗字体
        tv_everyday_recommon.getPaint().setFakeBoldText(true);

        tv_everyday_topic = (TextView)view.findViewById(R.id.tv_everyday_topic);
        contain_hot_list = (LinearLayout)view.findViewById(R.id.contain_hot_list);
        adapter = new FoundGridAdapter(getActivity());
        found_grid_view.setAdapter(adapter);
        found_grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        startActivity(new Intent(getActivity(), ArticleListActivity.class));
                        break;
                    case 1:
                        //点击话题隐藏当前界面

                        break;
                    case 2:
                        startActivity(new Intent(getActivity(),PrivilegeActiveActivity.class));
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
                scrollView.onRefreshComplete();

            }
        });
        handler.sendEmptyMessage(ADD_MORE_DATA);

        return view;
    }
}
