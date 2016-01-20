package com.wj.kindergarten.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.PfAlbumList;
import com.wj.kindergarten.bean.PfAlbumListSun;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.ConllectPicActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.PfEditInfoActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.PfFragmentLinearLayout;
import com.wj.kindergarten.ui.mine.photofamilypic.PfFusionFragment;
import com.wj.kindergarten.ui.mine.photofamilypic.PfUpGalleryActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.UpLoadActivity;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.Utils;
import com.wj.kindergarten.utils.WindowUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tangt on 2016/1/14.
 */
public class PhotoFamilyFragment extends Fragment {
    private TabLayout tab_layout;
    private ViewPager viewPager;
    private FragmentPagerAdapter pagerAdapter;
    private List<PfAlbumListSun> albumList;
    private View view;
    private PfFusionFragment pfFusionFragment;
    private FrameLayout back_pf_scroll_fl;
    private PfFragmentLinearLayout pf_back_ll;
    public static String family_uuid = "";
    boolean flIsLocationTop;
    private boolean isOne;
    private float moveY;
    private ArrayList<String> list = new ArrayList<>();
    private String [] images = new String []{
            "http://img03.sogoucdn.com/app/a/100520024/83ef625cdb1ea0a339645e6a1ade033c",
            "http://img02.sogoucdn.com/app/a/100520024/ad55a6132984150bf7b6df71fab9d16b",
            "http://img02.sogoucdn.com/app/a/100520024/f4ade868d7abc6769cae5ee9d70bf75c",
            "http://img01.sogoucdn.com/app/a/100520024/f12f63ca6757d36a6044c317876dd00c",
            "http://img02.sogoucdn.com/app/a/100520024/7c7942e15af5220fd165e571aa0fca33",
            "http://img01.sogoucdn.com/app/a/100520024/bb33c849ec21ea3a98b3598d56efb8c4",
            "http://img03.sogoucdn.com/app/a/100520024/d409d7b4fb46c19da38cd398acea013b",
    };

    {
        for(String path : images){
            list.add(path);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initHead();
        if (view != null) return view;
        view = inflater.inflate(R.layout.photo_family_pic, null);
        initViews(view);
        initClickListener();
        initTabLayout();
        loadPfData();
        return view;
    }

    private void initHead() {
        ((MainActivity) getActivity()).clearCenterIcon();
        ((MainActivity) getActivity()).setTitleText("家庭相册");
        ((MainActivity) getActivity()).showLeftButton(R.drawable.hanbao_left);
        ((MainActivity) getActivity()).setTitleRightImage(R.drawable.new_album_carema, 0);
        ((MainActivity) getActivity()).titleLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出菜单
                View viewleft = View.inflate(getActivity(), R.layout.pf_left_choose, null);
                final PopupWindow popupWindow = new PopupWindow(viewleft, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView tv_collect = (TextView) viewleft.findViewById(R.id.tv_head_collect);
                TextView tv_up_list = (TextView) viewleft.findViewById(R.id.tv_up_list);
                TextView tv_album_info = (TextView) viewleft.findViewById(R.id.tv_album_info);
                tv_collect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ConllectPicActivity.class);
                        //TODO 放入收藏的照片集合
                        intent.putExtra("collect_list", list);
                        startActivity(intent);
                        popupWindow.dismiss();
                    }
                });

                tv_up_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), UpLoadActivity.class);
                        startActivity(intent);
                        popupWindow.dismiss();
                    }
                });

                tv_album_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击启动相册详细页面
                        Intent intent = new Intent(getActivity(), PfEditInfoActivity.class);
                        startActivity(intent);
                        popupWindow.dismiss();
                    }
                });

                Utils.setPopWindow(popupWindow);
                popupWindow.showAsDropDown(((MainActivity) getActivity()).titleLeftButton);
            }
        });
    }

    public void addRightListener() {

        final View rightView = View.inflate(getActivity(),R.layout.pop_pf_choose_pic,null);
        final PopupWindow rightpopupWindow = new PopupWindow(rightView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        TextView put_in_pic = (TextView) rightView.findViewById(R.id.put_in_pic);
        put_in_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PfUpGalleryActivity.class);
                startActivity(intent);
                rightpopupWindow.dismiss();
            }
        });

        Utils.setPopWindow(rightpopupWindow);
        rightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightpopupWindow.dismiss();
            }
        });
        rightpopupWindow.showAsDropDown(view);
    }

    //获取家庭相册集
    private void loadPfData() {
        UserRequest.getPfAlbumList(getActivity(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                PfAlbumList pfAlbumList = (PfAlbumList) domain;
                if (pfAlbumList != null && pfAlbumList.getList() != null && pfAlbumList.getList().size() > 0) {
                    albumList = pfAlbumList.getList();
                    family_uuid =  pfAlbumList.getList().get(0).getUuid();
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

    private void initClickListener() {

    }

    float rawY;

    private void initViews(View v) {
        tab_layout = (TabLayout) v.findViewById(R.id.common_tab_layout);
        viewPager = (ViewPager) v.findViewById(R.id.common_viewPager);
        back_pf_scroll_fl = (FrameLayout) v.findViewById(R.id.back_pf_scroll_fl);
        pf_back_ll = (PfFragmentLinearLayout) v.findViewById(R.id.pf_back_ll);

    }

    private void bottomRequest() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) back_pf_scroll_fl.getLayoutParams();
        int margin = (int) (params.topMargin + moveY);

        if (margin < -Math.abs(back_pf_scroll_fl.getHeight())) {
            margin = -back_pf_scroll_fl.getHeight();
        }
        if(margin > 0){
            margin = 0;
        }
        params.topMargin = margin;
        back_pf_scroll_fl.setLayoutParams(params);
        back_pf_scroll_fl.requestLayout();
    }

    String[] titles = new String[]{"时光轴", "精品相册"};

    private void initTabLayout() {


        pagerAdapter = new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public Fragment getItem(int position) {

                switch (position) {
                    case 0:
                        if (pfFusionFragment == null) {
                            pfFusionFragment = new PfFusionFragment();
                        }
                        pf_back_ll.setOnInterceptTouchEvent(new MyTouch());
                        return pfFusionFragment;
                    case 1:
                        return new TestFragment();
                    case 2:
                        return new TestFragment();
                }


                return null;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        };

        viewPager.setAdapter(pagerAdapter);
        tab_layout.setupWithViewPager(viewPager);

    }
    class MyTouch implements PfFragmentLinearLayout.OnInterceptTouchEvent {


        @Override
        public boolean onInterceptTouch(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    rawY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    moveY = event.getRawY() - rawY;
                    bottomRequest();
                    rawY = event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return false;
        }
    }

}
