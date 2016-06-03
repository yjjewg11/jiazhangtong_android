package com.wj.kindergarten.ui.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.MapRouteData;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/1/12.
 */
public class AddressFragment extends Fragment {
    View view;
    private TextView tv_by_way_where,map_tv_time,tv_map_distance;
    private RelativeLayout map_route_container;
//    private List<WalkingRouteLine> routeList = new ArrayList<>();
    private PullToRefreshListView pullList;
    private MapRouteAdapter routeAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view != null) return view;
        view = inflater.inflate(R.layout.address_fragment,null);
        pullList = (PullToRefreshListView)view.findViewById(R.id.pulltorefresh_list);
        pullList.setMode(PullToRefreshBase.Mode.DISABLED);
        routeAdapter = new MapRouteAdapter();
        pullList.setAdapter(routeAdapter);
        pullList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtils.showMessage("点击查看详细地图!");
            }
        });
        return view;
    }

//    public void setRouteList(List<WalkingRouteLine> routeList){
//        this.routeList = routeList;
//        showView();
//    }

//    private void showView() {
//        if(routeList.size() == 0){
//            map_route_container.removeAllViews();
//            ((BaseActivity)getActivity()).noView(map_route_container);
//        }else{
//            routeAdapter.notifyDataSetChanged();
//        }
//    }

    class MapRouteAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
//            return routeList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null){
                viewHolder = new ViewHolder();
                convertView = View.inflate(getActivity(),R.layout.map_route_item,null);
                viewHolder.map_tv_time = (TextView)convertView.findViewById(R.id.map_tv_time);
                viewHolder.tv_by_way_where = (TextView)convertView.findViewById(R.id.tv_by_way_where);
                viewHolder.tv_map_distance = (TextView)convertView.findViewById(R.id.tv_map_distance);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
//            WalkingRouteLine plan = routeList.get(position);
//            if(plan != null){
//                viewHolder.map_tv_time.setText(""+plan.getDuration());
//                viewHolder.tv_by_way_where.setText(""+plan.getTitle());
//                viewHolder.tv_map_distance.setText(""+plan.getDistance());
//            }
            return convertView;
        }
    }

    class ViewHolder{
        TextView tv_by_way_where,tv_map_distance,map_tv_time;
    }
}
