package com.wj.kindergarten.ui.mine.photofamilypic;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.QueryGroupCount;
import com.wj.kindergarten.ui.func.adapter.FusionAdapter;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.ui.main.PhotoFamilyFragment;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ThreadManager;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tangt on 2016/3/1.
 */
public class FusionListFragment extends Fragment{

    private final int QUERY_FINISHED = 3070;
    private final String QUERY_CLOUMN = "photo_time";
    private int firstItem;
    private PhotoFamilyFragment photoFamilyFragment;
    private View mainView;
    private PullToRefreshListView pullListView;
    private PfLoadDataProxy mPfLoadDataProxy;
    private List<QueryGroupCount> queryGroupCounts = new ArrayList<>();
    private HashMap<String,List<AllPfAlbumSunObject>> map = new HashMap<>();

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                    //查询指定时间前的数据
                    case PfLoadDataProxy.NORMAL_DATA:
                        List<QueryGroupCount> allList = (List<QueryGroupCount>) msg.obj;
                        if(allList != null && allList.size() > 0){
                            queryGroupCounts.clear();
                            queryGroupCounts.addAll(allList);
//                            fusionAdapter.setQueryGroupCounts(queryGroupCounts);
                            queryAllpic();
                        }
                        break;
                    //有maxTime后时间的刷新的数据
                    case PfLoadDataProxy.REFRESH_DATA:
                        showView();
                        break;
                case QUERY_FINISHED:
                    fusionAdapter.setQueryMap(map, queryGroupCounts);
                    break;
            }
        }
    };

    private void queryAllpic() {
        ThreadManager.instance.excuteRunnable(new Runnable() {
            @Override
            public void run() {
                for(int i = 0 ; i < queryGroupCounts.size() ; i++){
                    QueryGroupCount queryGroupCount = queryGroupCounts.get(i);
                    if(!map.containsKey(queryGroupCount.getDate())){
                        queryData(queryGroupCount);
                    }
                    if(i == queryGroupCounts.size() - 1){
                        queryData(queryGroupCount);
                    }
                }
                mHandler.sendEmptyMessage(QUERY_FINISHED);
            }
        });
    }

    private void queryData(QueryGroupCount queryGroupCount) {
        String sql = " strftime('%Y-%m-%d'," + QUERY_CLOUMN + ") ='" + queryGroupCount.getDate() +
                "' and family_uuid ='" + MainActivity.getFamily_uuid() + "'";
        List<AllPfAlbumSunObject> list =  db.findAllByWhere(AllPfAlbumSunObject.class, sql);
        map.put(queryGroupCount.getDate(),list);
    }

    private FusionAdapter fusionAdapter;

    private void showView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        TextView textView = new TextView(getActivity());
        textView.setText(""+"您有新数据了，点击更新!");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPfLoadDataProxy.loadData(MainActivity.getFamily_uuid(),1,false);
            }
        });
        builder.setView(textView);
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params =  window.getAttributes();
        params.x = 100;
        params.y = 150;
        params.gravity = Gravity.TOP;
        window.setAttributes(params);
        dialog.show();

    }


    private List<AllPfAlbumSunObject> findAllByDate(String date) {
        String family_uuid = MainActivity.getFamily_uuid();
        String sql = " strftime('%Y-%m-%d',"+QUERY_CLOUMN+") ='" + date + "' and family_uuid ='" + family_uuid + "'";
        return db.findAllByWhere(AllPfAlbumSunObject.class,sql);
    }

    private boolean noMoreData;
    private int pageNo = 1;
    private FinalDb db;

    public FusionListFragment(PhotoFamilyFragment photoFamilyFragment) {
        this.photoFamilyFragment = photoFamilyFragment;
    }

    public FusionListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initScrollListen();
        if(mainView != null) return mainView;
        initDb();
        mainView = View.inflate(getActivity(), R.layout.fusion_list_fragment,null);
        pullListView = (PullToRefreshListView) mainView.findViewById(R.id.pulltorefresh_list);
        pullListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        pullListView.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstItem = firstVisibleItem;
            }
        });
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPfLoadDataProxy.loadData(MainActivity.getFamily_uuid(), 1, true);
            }
        });
        fusionAdapter = new FusionAdapter(getActivity());
        pullListView.setAdapter(fusionAdapter);
        loadData();

        return mainView;
    }

    private void initDb() {
        db = FinalDb.create(getActivity(), GloablUtils.FAMILY_UUID_OBJECT,true);
    }


    public void loadData() {
        mPfLoadDataProxy = new PfLoadDataProxy(getActivity(), mHandler);
        mPfLoadDataProxy.setDataLoadFinish(new PfLoadDataProxy.DataLoadFinish() {
            @Override
            public void finish() {
                if (pullListView.isRefreshing()) {
                    pullListView.onRefreshComplete();
                }
            }

            @Override
            public void noMoreData() {
                pullListView.setMode(PullToRefreshBase.Mode.DISABLED);
                noMoreData = true;
            }

        });
        mPfLoadDataProxy.loadData(((MainActivity) getActivity()).getFamily_uuid(), pageNo, false);
    }



    private void initScrollListen() {
        photoFamilyFragment.setSubViewDecideScroll(new PfFragmentLinearLayout.DecideSubViewScroll() {

            @Override
            public void allowScroll() {
            }

            @Override
            public void stopScroll() {
            }

            @Override
            public boolean subViewLocationTop() {
                return firstItem == 0;
            }
        });
    }
}
