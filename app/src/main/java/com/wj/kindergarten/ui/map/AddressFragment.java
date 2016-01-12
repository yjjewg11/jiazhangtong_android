package com.wj.kindergarten.ui.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.MapRouteData;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.Utils;

/**
 * Created by tangt on 2016/1/12.
 */
public class AddressFragment extends Fragment {
    View view;
    private TextView tv_by_way_where,map_tv_time,tv_map_distance;
    private MapRouteData routeData;
    private RelativeLayout map_route_container;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view != null) return view;
        view = inflater.inflate(R.layout.address_fragment,null);
        map_route_container = (RelativeLayout)view.findViewById(R.id.map_route_container);
        tv_by_way_where = (TextView)view.findViewById(R.id.tv_by_way_where);
        map_tv_time = (TextView)view.findViewById(R.id.map_tv_time);
        tv_map_distance = (TextView) view.findViewById(R.id.tv_map_distance);
        return view;
    }

    public void setMapRouteData(MapRouteData mapRouteData){
        this.routeData = mapRouteData;
        showView();
    }

    private void showView() {
        if(routeData == null){
            map_route_container.removeAllViews();
            ((BaseActivity)getActivity()).noView(map_route_container);
            return;
        }
        tv_by_way_where.setText(""+ Utils.isNull(routeData.getAddress()));
        tv_map_distance.setText(""+Utils.isNull(routeData.getDistance()));
        map_tv_time.setText(""+Utils.isNull(routeData.getTime()));
    }
}
