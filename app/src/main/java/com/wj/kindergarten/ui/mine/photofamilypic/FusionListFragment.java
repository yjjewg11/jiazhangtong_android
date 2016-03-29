package com.wj.kindergarten.ui.mine.photofamilypic;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.QueryGroupCount;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.adapter.FusionAdapter;
import com.wj.kindergarten.ui.func.adapter.FusionListOwnGridAdapter;
import com.wj.kindergarten.ui.main.PhotoFamilyFragment;
import com.wj.kindergarten.ui.mine.photofamilypic.observer.Watcher;
import com.wj.kindergarten.ui.more.PfRefreshLinearLayout;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.FinalUtil;
import com.wj.kindergarten.utils.ThreadManager;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.ToastUtils;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tangt on 2016/3/1.
 */
public class FusionListFragment extends Fragment implements Watcher{

    private final int QUERY_FINISHED = 3070;
    private final String QUERY_CLOUMN = "create_time";
    private int firstItem;
    private PhotoFamilyFragment photoFamilyFragment;
    private View mainView;
//    private PullToRefreshListView pullListView;
    private PfLoadDataProxy mPfLoadDataProxy;
    private List<AllPfAlbumSunObject> allObjects = new ArrayList<>();
    private List<QueryGroupCount> queryGroupCounts = new ArrayList<>();
    private HashMap<String,List<AllPfAlbumSunObject>> map = new HashMap<>();
    private HashMap<String,Integer> mapSize = new HashMap();
    private String family_uuid;

    public void setFamily_uuid(String family_uuid) {
        this.family_uuid = family_uuid;
    }


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                    //查询指定时间前的数据
                    case PfLoadDataProxy.NORMAL_DATA:
                        //判断是否是没有数据，新请求的数据，是的话，去掉无内容提醒
                        if(fusion_list_root.getChildCount() > 0 &&
                                fusion_list_root.getChildAt(0) != fusion_list_fresh_linear){
                            fusion_list_root.removeAllViews();
                            fusion_list_root.addView(fusion_list_fresh_linear);
                        }
                        List<QueryGroupCount> allList = (List<QueryGroupCount>) msg.obj;
                        if(allList != null){
                            queryGroupCounts.clear();
                            queryGroupCounts.addAll(allList);
                            queryAllpic();
                            queryAlldata();
                        }
                        break;
                    //有maxTime后时间的刷新的数据
                    case PfLoadDataProxy.REFRESH_DATA:
//                        showView();
                        mPfLoadDataProxy.loadData(family_uuid,1,false);
                        break;
                case QUERY_FINISHED:
//                    fusionAdapter.setQueryMap(map, queryGroupCounts);
                    ownGridAdapter.setqueryCount(mapSize);
                    break;
            }
        }
    };
    private PfRefreshLinearLayout fusion_list_fresh_linear;
    private int visibleItem;
    private int totalItem;

    private void queryAlldata() {
        String sql = "family_uuid = '"+family_uuid+"';";
        List<AllPfAlbumSunObject> list = dbObj.findAllByWhere(AllPfAlbumSunObject.class, sql);
        if(list != null ){
            sortList(list);
            allObjects.clear();
            allObjects.addAll(list);
            ownGridAdapter.setQueryList(queryGroupCounts,allObjects);
        }
        if(list != null && list.size() == 0){
            ((BaseActivity)getActivity()).noView(fusion_list_root);
        }
    }

    private void sortList(List<AllPfAlbumSunObject> list) {
        Collections.sort(list, new Comparator<AllPfAlbumSunObject>() {
            @Override
            public int compare(AllPfAlbumSunObject o, AllPfAlbumSunObject t) {
                int cha = 0;
                long t1 = TimeUtil.getYMDHMSTime(o.getCreate_time());
                long t2 = TimeUtil.getYMDHMSTime(t.getCreate_time());
                if (t2 - t1 > 0) {
                    cha = 1;
                } else if(t2 - t1 < 0){
                    cha = -1;
                }

                return cha;
            }
        });
    }

    @ViewInject(id = R.id.fusion_list_fragment_stick_grid)
    private StickyGridHeadersGridView fusion_list_fragment_stick_grid;
    private FusionListOwnGridAdapter ownGridAdapter;
    @ViewInject(id = R.id.fusion_list_root)
    FrameLayout fusion_list_root;

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
                "' and family_uuid ='" +family_uuid + "'";
        List<AllPfAlbumSunObject> list =  dbObj.findAllByWhere(AllPfAlbumSunObject.class, sql);
        map.put(queryGroupCount.getDate(),list);
        mapSize.put(queryGroupCount.getDate(),list.size());
    }

    private FusionAdapter fusionAdapter;


    private List<AllPfAlbumSunObject> findAllByDate(String date) {
        String sql = " strftime('%Y-%m-%d',"+QUERY_CLOUMN+") ='" + date + "' and family_uuid ='" + family_uuid + "'";
        return dbObj.findAllByWhere(AllPfAlbumSunObject.class,sql);
    }

    private boolean noMoreData;
    private int pageNo = 1;
    private FinalDb dbObj;

    public FusionListFragment(PhotoFamilyFragment photoFamilyFragment,String family_uuid) {
        this.photoFamilyFragment = photoFamilyFragment;
        this.family_uuid = family_uuid;
    }

    public FusionListFragment() {
    }

    //防止多次下拉造成的重复加载
    boolean isCouldLoad = true;
    public int scrollStateS;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initScrollListen();
        if(mainView != null) return mainView;
        initDb();
        mainView = inflater.inflate( R.layout.fusion_list_fragment,container,false);
        FinalActivity.initInjectedView(this, mainView);
        photoFamilyFragment.getObserver().registerObserver(this);
        fusion_list_fragment_stick_grid.setOnScrollListener(new AbsListView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                scrollStateS = scrollState;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstItem = firstVisibleItem;
                visibleItem = visibleItemCount;
                totalItem = totalItemCount;
                //滑到底部自动加载,并且能够加载更多，而且处于滚动状态
                if(firstVisibleItem + visibleItemCount == totalItemCount && isCouldLoad
                        && scrollStateS != 0){
                    isCouldLoad = false;
                    fusion_list_fresh_linear.fromEndBeginRefresh();
                }
            }
        });
        photoFamilyFragment.setPullUpView(fusion_list_fragment_stick_grid);
        ownGridAdapter = new FusionListOwnGridAdapter(getActivity());
        fusion_list_fragment_stick_grid.setAdapter(ownGridAdapter);
        fusion_list_fragment_stick_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String time = TimeUtil.getYMDTimeFromYMDHMS(allObjects.get(position).getPhoto_time());
//                String sql = "family_uuid = '"+family_uuid+"' and strftime('%Y-%m-%d',photo_time) = '"+time+"'";
//                String sql = " strftime('%Y-%m-%d',create_time) DESC limit 50;";
//                //按倒序排列
//                List<AllPfAlbumSunObject> shortList = dbObj.findAll(AllPfAlbumSunObject.class,sql);
////                sortList(shortList);
//                position = shortList.indexOf(ownGridAdapter.getItem(position));
                //传递查询效率太低，改为传递过去进行查询
//                new TransportListener(getActivity(), position, shortList, null).onItemClick(parent, view, position, id);
                new TransportListener(getActivity()).onAllItem((AllPfAlbumSunObject) ownGridAdapter.getItem(position));


            }
        });
        fusion_list_fresh_linear = (PfRefreshLinearLayout)mainView.findViewById(R.id.fusion_list_fresh_linear);
        fusion_list_fresh_linear.setPullScroll(new PfRefreshLinearLayout.PullScrollBoth() {
            @Override
            public boolean judgeScrollTop() {
                CGLog.v("打印firstItem : " + (firstItem == 0) + " weizhi : " + firstItem);
                return firstItem == 0;
            }

            @Override
            public boolean judgeScrollBotom() {
                return firstItem + visibleItem == totalItem;
            }
        });

        fusion_list_fresh_linear.setMode(PfRefreshLinearLayout.Mode.PULL_FROM_UP);
        fusion_list_fresh_linear.setOnRefreshListener(new PfRefreshLinearLayout.OnRefreshListener() {

            @Override
            public void pullFromEndRefresh() {
                loadBottomData();
            }

            @Override
            public void pullFromTopRefresh() {
                mPfLoadDataProxy.queryIncrementNewData(family_uuid, new PfLoadDataProxy.DataLoadFinish() {
                    @Override
                    public void finish() {
                        fusion_list_fresh_linear.onRefreshComplete();
                    }

                    @Override
                    public void noMoreData() {
                        ToastUtils.showMessage("暂无更新数据!");
                    }

                    @Override
                    public void loadFailed() {
                        if(fusion_list_fresh_linear.getRefreshing())
                            fusion_list_fresh_linear.onRefreshComplete();
                    }
                });
            }
        });
        mPfLoadDataProxy = new PfLoadDataProxy(getActivity(), mHandler);
        mPfLoadDataProxy.setQUERY_CLOUMN(QUERY_CLOUMN);
        mPfLoadDataProxy.setDataLoadFinish(new PfLoadDataProxy.DataLoadFinishFirst() {
            @Override
            public void noMoreDataFirst() {
                ((BaseActivity)getActivity()).noView(fusion_list_root);
            }

            @Override
            public void finish() {
                isCouldLoad = true;
                fusion_list_fresh_linear.onRefreshComplete();
            }

            @Override
            public void noMoreData() {
                    fusion_list_fresh_linear.setMode(PfRefreshLinearLayout.Mode.PULL_FROM_UP);
                    isCouldLoad = false;
            }

            @Override
            public void loadFailed() {
                if(fusion_list_fresh_linear.getRefreshing())
                fusion_list_fresh_linear.onRefreshComplete();
            }

        });
        loadData();

        return mainView;
    }

    public void loadBottomData(){
        mPfLoadDataProxy.loadData(family_uuid, 1, true);
    }

    private void initDb() {
        dbObj = FinalUtil.getFamilyUuidObjectDb(getActivity());
    }

    public void loadData() {
        mPfLoadDataProxy.loadData(family_uuid, pageNo, false);
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

    @Override
    public void refreshUUid(String family_uuid) {
        if(!TextUtils.isEmpty(family_uuid)){
            this.family_uuid = family_uuid;
            refreshData();
        }
    }

    public void refreshData() {
        isCouldLoad = true;
        loadData();
    }

}
