package com.wj.kindergarten.ui.mine.photofamilypic;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;
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
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.QueryGroupCount;
import com.wj.kindergarten.ui.func.adapter.FusionAdapter;
import com.wj.kindergarten.ui.func.adapter.FusionListOwnGridAdapter;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.ui.main.PhotoFamilyFragment;
import com.wj.kindergarten.ui.mine.photofamilypic.observer.Watcher;
import com.wj.kindergarten.ui.more.PfRefreshLinearLayout;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ThreadManager;
import com.wj.kindergarten.utils.TimeUtil;

import net.tsz.afinal.FinalDb;

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
    private final String QUERY_CLOUMN = "photo_time";
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
                        List<QueryGroupCount> allList = (List<QueryGroupCount>) msg.obj;
                        if(allList != null && allList.size() > 0){
                            queryGroupCounts.clear();
                            queryGroupCounts.addAll(allList);
                            queryAllpic();
                            queryAlldata();
                        }
                        break;
                    //有maxTime后时间的刷新的数据
                    case PfLoadDataProxy.REFRESH_DATA:
                        showView();
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
        List<AllPfAlbumSunObject> list = db.findAllByWhere(AllPfAlbumSunObject.class, sql);
        if(list != null && list.size() > 0){
            Collections.sort(list, new Comparator<AllPfAlbumSunObject>() {
                @Override
                public int compare(AllPfAlbumSunObject o, AllPfAlbumSunObject t) {
                    long t1 = (TimeUtil.getYMDHMSTime(o.getPhoto_time()));
                    long t2 = TimeUtil.getYMDHMSTime(t.getPhoto_time());
                    return (int) (t2 - t1);
                }
            });
            allObjects.clear();
            allObjects.addAll(list);
            ownGridAdapter.setQueryList(queryGroupCounts,allObjects);
        }
    }

    private StickyGridHeadersGridView fusion_list_fragment_stick_grid;
    private FusionListOwnGridAdapter ownGridAdapter;

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
        List<AllPfAlbumSunObject> list =  db.findAllByWhere(AllPfAlbumSunObject.class, sql);
        map.put(queryGroupCount.getDate(),list);
        mapSize.put(queryGroupCount.getDate(),list.size());
    }

    private FusionAdapter fusionAdapter;

    private void showView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        TextView textView = new TextView(getActivity());
        textView.setText(""+"您有新数据了，点击更新!");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPfLoadDataProxy.loadData(family_uuid,1,false);
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
        String sql = " strftime('%Y-%m-%d',"+QUERY_CLOUMN+") ='" + date + "' and family_uuid ='" + family_uuid + "'";
        return db.findAllByWhere(AllPfAlbumSunObject.class,sql);
    }

    private boolean noMoreData;
    private int pageNo = 1;
    private FinalDb db;

    public FusionListFragment(PhotoFamilyFragment photoFamilyFragment,String family_uuid) {
        this.photoFamilyFragment = photoFamilyFragment;
        this.family_uuid = family_uuid;
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
        photoFamilyFragment.getObserver().registerObserver(this);
        fusion_list_fresh_linear = (PfRefreshLinearLayout)mainView.findViewById(R.id.fusion_list_fresh_linear);
        fusion_list_fresh_linear.setPullScroll(new PfRefreshLinearLayout.PullScroll() {
            @Override
            public boolean judgeScrollBotom() {
                CGLog.v("判断是否滑动到底部了?    " + (firstItem + visibleItem == totalItem));
                return firstItem + visibleItem == totalItem;
            }
        });
        fusion_list_fragment_stick_grid = (StickyGridHeadersGridView) mainView.findViewById(R.id.fusion_list_fragment_stick_grid);
        fusion_list_fresh_linear.setStickyGridHeadersGridView(fusion_list_fragment_stick_grid);
        fusion_list_fresh_linear.setOnRefreshListener(new PfRefreshLinearLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPfLoadDataProxy.loadData(family_uuid,1,true);
            }
        });
        fusion_list_fragment_stick_grid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstItem = firstVisibleItem;
                visibleItem = visibleItemCount;
                totalItem = totalItemCount;
                CGLog.v("firstVisibleItem : " + firstVisibleItem + "   visibleItemCount : " + visibleItemCount +
                        "    totalItemCount:" + totalItemCount);
            }
        });
        ownGridAdapter = new FusionListOwnGridAdapter(getActivity());
        fusion_list_fragment_stick_grid.setAdapter(ownGridAdapter);
        fusionAdapter = new FusionAdapter(getActivity());
        mPfLoadDataProxy = new PfLoadDataProxy(getActivity(), mHandler);
        mPfLoadDataProxy.setDataLoadFinish(new PfLoadDataProxy.DataLoadFinish() {
            @Override
            public void finish() {
                fusion_list_fresh_linear.onRefreshComplete();
            }

            @Override
            public void noMoreData() {
                fusion_list_fresh_linear.setMode(PfRefreshLinearLayout.Mode.DISALBED);
                noMoreData = true;
            }

        });
        loadData();

        return mainView;
    }

    private void initDb() {
        db = FinalDb.create(getActivity(), GloablUtils.FAMILY_UUID_OBJECT,true);
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

    private void refreshData() {
        loadData();
    }
}
