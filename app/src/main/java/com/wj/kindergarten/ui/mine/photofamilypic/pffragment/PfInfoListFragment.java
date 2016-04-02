package com.wj.kindergarten.ui.mine.photofamilypic.pffragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.QueryGroupCount;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import net.tsz.afinal.FinalDb;

import java.util.List;

public class PfInfoListFragment extends Fragment {
    View view;
    private GridView pf_info_gridView;
    private GridAdapter adapter;
    private FinalDb db;
    private List<QueryGroupCount> groupCountList ;

    public PfInfoListFragment(List<QueryGroupCount> groupCountList) {
        this.groupCountList = groupCountList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view != null) return view;
        db = FinalDb.create(getActivity(), GloablUtils.FAMILY_UUID_OBJECT);
        view = inflater.inflate(R.layout.fragment_pf_info_list,null);
        pf_info_gridView = (GridView) view.findViewById(R.id.pf_info_gridView);
        adapter = new GridAdapter(groupCountList);
        pf_info_gridView.setAdapter(adapter);
        return view;
    }

    class GridAdapter extends BaseAdapter{
        private List<QueryGroupCount> queryGroupCountList ;

        public GridAdapter(List<QueryGroupCount> queryGroupCountList) {
            this.queryGroupCountList = queryGroupCountList;
        }

        @Override
        public int getCount() {
            return queryGroupCountList.size();
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
            Holder holder;
            if(convertView == null){
                holder = new Holder();
                convertView = View.inflate(getActivity(), R.layout.pf_info_item,null);
                holder.pf_info_image = (ImageView) convertView.findViewById(R.id.pf_info_image);
                holder.pf_info_text = (TextView) convertView.findViewById(R.id.pf_info_text);
                convertView.setTag(holder);
            }else{
                holder = (Holder) convertView.getTag();
            }
            QueryGroupCount queryGroupCount = queryGroupCountList.get(position);
            if(queryGroupCount != null){
                String sql = "strftime('%Y-%m-%d',create_time) = '"+queryGroupCount.getDate()+"' limit 1";
                List<AllPfAlbumSunObject> list = db.findAllByWhere(AllPfAlbumSunObject.class, sql);
                String path = list.get(0).getPath();
                ImageLoaderUtil.displayMyImage(path,holder.pf_info_image);
                holder.pf_info_text.setText(""+queryGroupCount.getCount());
            }
            return convertView;
        }

        class Holder{
            TextView pf_info_text;
            ImageView pf_info_image;
        }
    }

}
