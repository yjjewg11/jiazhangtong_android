package com.wj.kindergarten.ui.mine.photofamilypic.pffragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbum;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.QueryGroupCount;
import com.wj.kindergarten.ui.func.adapter.BoutiquePopAdapter;
import com.wj.kindergarten.ui.mine.photofamilypic.PfGalleryActivity;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;

import java.util.ArrayList;
import java.util.List;


public class PfInfoAllPIcFragment extends Fragment {
    View view;
    private List<AllPfAlbumSunObject> objectList = new ArrayList<>();
    private List<QueryGroupCount> queryGroupCounts;
    private PullToRefreshListView changeDirListView;
    private BoutiquePopAdapter boutiquePopAdapter;
    private FinalDb db;
    private View popView;

    public PfInfoAllPIcFragment(List<QueryGroupCount> queryGroupCounts) {
        this.queryGroupCounts = queryGroupCounts;
    }

    @ViewInject(id = R.id.pf_info_all_gridView)
    private GridView pf_info_all_gridView;
    private GridAdapter adapter;
    @ViewInject(id = R.id.pf_info_choose_text, click = "onClick")
    private TextView pf_info_choose_text;

    public GridAdapter getAdapter() {
        return adapter;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pf_info_choose_text:
                showPop();
                break;
        }
    }

    private void showPop() {
        if (popView == null) {
            QueryGroupCount queryGroupCount = new QueryGroupCount();
            queryGroupCount.setDate("所有照片");
            queryGroupCounts.add(0, queryGroupCount);
            popView = View.inflate(getActivity(), R.layout.boutique_gallery_pop_layout, null);
            changeDirListView = (PullToRefreshListView) popView.findViewById(R.id.pulltorefresh_list);
            changeDirListView.setMode(PullToRefreshBase.Mode.DISABLED);
            boutiquePopAdapter = new BoutiquePopAdapter((PfGalleryActivity) getActivity(), queryGroupCounts);
            changeDirListView.setAdapter(boutiquePopAdapter);
        }
        final PopupWindow popupPic = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Utils.setPopWindow(popupPic);
        popView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupPic.dismiss();
            }
        });
        changeDirListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 1 == 0) {
                    objectList.addAll(db.findAll(AllPfAlbumSunObject.class));
                    adapter.notifyDataSetChanged();
                    return;
                }
                QueryGroupCount queryGroupCount = queryGroupCounts.get(position - 1);
                queryByDate(queryGroupCount.getDate());
                adapter.notifyDataSetChanged();
                popupPic.dismiss();
                pf_info_choose_text.setText(""+queryGroupCount.getDate());
            }
        });
        popupPic.showAtLocation(pf_info_choose_text, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((PfGalleryActivity) getActivity()).showActionbar();
        if (view != null) return view;
        view = inflater.inflate(R.layout.fragment_pf_info_all_pic, null);
        db = FinalDb.create(getActivity(), GloablUtils.FAMILY_UUID_OBJECT, true);
        FinalActivity.initInjectedView(this, view);
        pf_info_choose_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop();
            }
        });
        adapter = new GridAdapter();
        pf_info_all_gridView.setAdapter(adapter);
        pf_info_all_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PfGalleryActivity activity = (PfGalleryActivity) getActivity();
                activity.changeToSingleFragment(position, objectList);
            }
        });
        queryData();
        return view;
    }

    private void queryData() {
        List<AllPfAlbumSunObject> list = db.findAll(AllPfAlbumSunObject.class);
        this.objectList.addAll(list);
        adapter.setObjectList(objectList);
    }

    class GridAdapter extends BaseAdapter {

        private List<AllPfAlbumSunObject> adapterList = new ArrayList<>();

        public void setObjectList(List<AllPfAlbumSunObject> adapterList) {
            this.adapterList.clear();
            this.adapterList.addAll(adapterList);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return objectList.size();
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
            if (convertView == null) {
                holder = new Holder();
                convertView = View.inflate(getActivity(), R.layout.collect_image, null);
                holder.collect_item_image = (ImageView) convertView.findViewById(R.id.collect_item_image);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            AllPfAlbumSunObject object = adapterList.get(position);
            ImageLoaderUtil.displayMyImage(object.getPath(), holder.collect_item_image);
            return convertView;
        }

        class Holder {
            ImageView collect_item_image;
        }
    }

    public void queryByDate(String time) {
        String sql = "strftime('%Y-%m-%d',create_time) = '" + time + "';";
        List<AllPfAlbumSunObject> objectList = db.findAllByWhere(AllPfAlbumSunObject.class, sql);
        if (objectList != null && objectList.size() > 0) {
            this.objectList.clear();
            this.objectList.addAll(objectList);
        }
    }

    public boolean getVisible() {
        return allPicFragmentIsVisible;
    }

    boolean allPicFragmentIsVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        allPicFragmentIsVisible = isVisibleToUser;
        super.setUserVisibleHint(isVisibleToUser);
    }
}
