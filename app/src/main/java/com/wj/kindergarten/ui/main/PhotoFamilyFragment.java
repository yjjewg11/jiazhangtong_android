package com.wj.kindergarten.ui.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ant.liao.GifView;
import com.nineoldandroids.animation.ObjectAnimator;
import com.umeng.socialize.utils.BitmapUtils;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.PfAlbumListSun;
import com.wj.kindergarten.ui.func.EditPfActivity;
import com.wj.kindergarten.ui.imagescan.BitmapCallBack;
import com.wj.kindergarten.ui.mine.photofamilypic.BoutiqueGalleryActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.ConllectPicActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.PfAlbumListActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.PfEditInfoActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.PfFragmentLinearLayout;
import com.wj.kindergarten.ui.mine.photofamilypic.PfFusionFragment;
import com.wj.kindergarten.ui.mine.photofamilypic.PfGalleryActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.PfUpGalleryActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.BoutiqueAlbumFragment;
import com.wj.kindergarten.ui.mine.photofamilypic.UpLoadActivity;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.Constant.BitmapUtil;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/1/14.
 */
public class PhotoFamilyFragment extends Fragment {
    public static final int ADD_PIC = 5001;
    private TabLayout tab_layout;
    //    private FragmentPagerAdapter pagerAdapter;
    private List<PfAlbumListSun> albumList;
    private View view;
    private PfFusionFragment pfFusionFragment;
    private BoutiqueAlbumFragment boutique_album_framgent;
    private FrameLayout back_pf_scroll_fl;
    private PfFragmentLinearLayout pf_back_ll;
    boolean flIsLocationTop ;
    private LocationChanged locationChanged;
    private RelativeLayout pf_backGround_rl;
    private ImageView pf_backGround_image;
    private TextView pf_backGround_family_name;
    private TextView pf_backGround_count;
    private TextView pf_backGround_count_right;
    private GifView gif;

    private int getFlTopMargin(){
        LinearLayout.LayoutParams params  = (LinearLayout.LayoutParams) back_pf_scroll_fl.getLayoutParams();
        return params.topMargin;
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };

    public void setLocationChanged(LocationChanged locationChanged) {
        this.locationChanged = locationChanged;
    }

    public boolean isFlIsLocationTop() {
        return flIsLocationTop;
    }

    private boolean isOne;
    private float moveY;
    private ArrayList<String> list = new ArrayList<>();
    private String[] images = new String[]{
            "http://img03.sogoucdn.com/app/a/100520024/83ef625cdb1ea0a339645e6a1ade033c",
            "http://img02.sogoucdn.com/app/a/100520024/ad55a6132984150bf7b6df71fab9d16b",
            "http://img02.sogoucdn.com/app/a/100520024/f4ade868d7abc6769cae5ee9d70bf75c",
            "http://img01.sogoucdn.com/app/a/100520024/f12f63ca6757d36a6044c317876dd00c",
            "http://img02.sogoucdn.com/app/a/100520024/7c7942e15af5220fd165e571aa0fca33",
            "http://img01.sogoucdn.com/app/a/100520024/bb33c849ec21ea3a98b3598d56efb8c4",
            "http://img03.sogoucdn.com/app/a/100520024/d409d7b4fb46c19da38cd398acea013b",
    };
    public boolean canScroll = true;
    private ObjectAnimator animotor;

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    {
        for (String path : images) {
            list.add(path);
        }
    }

    //标签 ： 时光轴，精品相册
    private String[] fragment_tags = new String[]{"fusion", "boutique_album"};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initHead();
        ((MainActivity)getActivity()).hideActionbar();
        if (view != null) return view;
        view = inflater.inflate(R.layout.photo_family_pic, null);
        initHeadView();
        initHeadViewData();
        initViews(view);
        initFragment();
        initClickListener();
        initTabLayout();

        return view;
    }

    private void initHeadViewData() {
        PfAlbumListSun sun = null;
        if(MainActivity.instance.getAlbumList() != null && MainActivity.instance.getAlbumList().size() > 0){
            sun = MainActivity.instance.getAlbumList().get(0);
            if(sun == null) return;
        }
        if(!TextUtils.isEmpty(sun.getHerald())){
            ImageLoaderUtil.displayImage(sun.getHerald(),pf_backGround_image);
        BitmapCallBack.loadBitmap(sun.getHerald(), new BitmapCallBack.GetBitmapCallback() {
            @Override
            public void callback(Bitmap bitmap) {
                BitmapDrawable drawable =  BitmapUtil.blur(getResources(), -1, bitmap);
                pf_backGround_rl.setBackground(drawable);
            }
        });
        }
        String text = Utils.isNull(sun.getTitle());
        if(TextUtils.isEmpty(text)){
            text = "问界互动家园";
        }else{
        }
        pf_backGround_family_name.setText("" +text);

    }

    private void initHeadView() {
        gif = (GifView) view.findViewById(R.id.pf_family_gif);
        gif.setGifImage(R.drawable.gifdownload);
        pf_backGround_rl = (RelativeLayout) view.findViewById(R.id.pf_backGround_rl);
        pf_backGround_image = (ImageView) view.findViewById(R.id.pf_backGround_image);
        pf_backGround_family_name = (TextView) view.findViewById(R.id.pf_backGround_family_name);
        pf_backGround_count = (TextView) view.findViewById(R.id.pf_backGround_count);
        pf_backGround_count_right = (TextView) view.findViewById(R.id.pf_backGround_count_right);
        pf_backGround_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Intent intent = new Intent(getActivity(), PfAlbumListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initFragment() {
        pfFusionFragment = new PfFusionFragment(this);
        pf_back_ll.setOnInterceptTouchEvent(new MyTouch());
        getFragmentManager().beginTransaction().replace(R.id.pf_change_content_fl, pfFusionFragment, fragment_tags[0]).commit();
    }

    private void initHead() {
        ((MainActivity) getActivity()).clearCenterIcon();
        ((MainActivity) getActivity()).setTitleText("家庭相册");
        ((MainActivity) getActivity()).showLeftButton(R.drawable.pf_lefthanbao);
        ((MainActivity) getActivity()).setTitleRightImage(R.drawable.pf_first_new_album, 0);
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

        final View rightView = View.inflate(getActivity(), R.layout.pop_pf_choose_pic, null);
        final PopupWindow rightpopupWindow = new PopupWindow(rightView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TextView put_in_pic = (TextView) rightView.findViewById(R.id.put_in_pic);
        TextView pop_of_choose_new_pic = (TextView) rightView.findViewById(R.id.pop_of_choose_new_pic);
        pop_of_choose_new_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BoutiqueGalleryActivity.class);
//                Bundle bundle = new Bundle();
//                intent.putExtra("bundle",bundle);
                startActivity(intent);
                rightpopupWindow.dismiss();
            }
        });
        put_in_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PfUpGalleryActivity.class);
                intent.putExtra("type", ADD_PIC);
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
        rightpopupWindow.showAsDropDown(rightView);
    }

    private void initClickListener() {

    }

    float rawY;

    private void initViews(View v) {
        tab_layout = (TabLayout) v.findViewById(R.id.common_tab_layout);
//        viewPager = (ViewPager) v.findViewById(R.id.common_viewPager);
        back_pf_scroll_fl = (FrameLayout) v.findViewById(R.id.back_pf_scroll_fl);
        pf_back_ll = (PfFragmentLinearLayout) v.findViewById(R.id.pf_back_ll);

    }


    private void bottomRequest() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) back_pf_scroll_fl.getLayoutParams();
        int margin = (int) (params.topMargin + moveY);

        if (margin <= -Math.abs(back_pf_scroll_fl.getHeight())) {
            margin = -back_pf_scroll_fl.getHeight();
        }else{

        }
        if (margin > 0) {
            margin = 0;
        }

        CGLog.v("打印列表是否在顶部 ; "+pfFusionFragment.scrollIsTop());
           if(flIsLocationTop && !pfFusionFragment.scrollIsTop()) return;

            params.topMargin = margin;
            back_pf_scroll_fl.setLayoutParams(params);
            back_pf_scroll_fl.requestLayout();

    }

    String[] titles = new String[]{"时光轴", "精品相册"};

    private void initTabLayout() {

        tab_layout.addTab(tab_layout.newTab().setText("时光轴"));
        tab_layout.addTab(tab_layout.newTab().setText("精品相册"));
        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        //时光轴
                        PfFusionFragment mfFusionFragment = (PfFusionFragment) getFragmentManager().findFragmentByTag(fragment_tags[0]);
                        if (mfFusionFragment != null) {
                            getFragmentManager().beginTransaction().replace(R.id.pf_change_content_fl, mfFusionFragment, fragment_tags[0]).commit();
                        } else {
                            getFragmentManager().beginTransaction().replace(R.id.pf_change_content_fl, pfFusionFragment, fragment_tags[0]).commit();
                        }
                        break;
                    case 1:
                        //精辟相册
                        if (getFragmentManager().findFragmentByTag(fragment_tags[1]) == null) {
                            boutique_album_framgent = new BoutiqueAlbumFragment();
                        } else {
                            boutique_album_framgent = (BoutiqueAlbumFragment) getFragmentManager().findFragmentByTag(fragment_tags[1]);
                        }

                        getFragmentManager().beginTransaction().replace(R.id.pf_change_content_fl, boutique_album_framgent, fragment_tags[1]).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        pagerAdapter = new FragmentPagerAdapter(getFragmentManager()) {
//            @Override
//            public int getCount() {
//                return 2;
//            }
//
//            @Override
//            public Fragment getItem(int position) {
//
//                switch (position) {
//                    case 0:
//                        if (pfFusionFragment == null) {
//                            pfFusionFragment = new PfFusionFragment();
//                        }
//                        pf_back_ll.setOnInterceptTouchEvent(new MyTouch());
//                        return pfFusionFragment;
//                    case 1:
//                        return new TestFragment();
//                    case 2:
//                        return new TestFragment();
//                }
//
//
//                return null;
//            }
//
//            @Override
//            public CharSequence getPageTitle(int position) {
//                return titles[position];
//            }
//        };

        //取消viewPager，改用动态添加fragment，原因为上下滑动和左右滑动容易冲突。

//        viewPager.setAdapter(pagerAdapter);
//        tab_layout.setupWithViewPager(viewPager);

    }

    boolean isFirst ;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(!isFirst){
            int height = back_pf_scroll_fl.getHeight();
            isFirst = true;
            animotor = ObjectAnimator.ofInt(new Wrapper(back_pf_scroll_fl),"topMargin",-height);
            animotor.setDuration(300);
            animotor.setInterpolator(new DecelerateInterpolator());
        }

    }
    public void flMoveTop(){
        int height = back_pf_scroll_fl.getHeight();
        ObjectAnimator animator = ObjectAnimator.ofInt(new Wrapper(back_pf_scroll_fl),"topMargin",-height);
        animator.setDuration(300).setInterpolator(new DecelerateInterpolator());
        animator.start();
    }
    public void flMoveBottom(){
        ObjectAnimator animator = ObjectAnimator.ofInt(new Wrapper(back_pf_scroll_fl),"topMargin",0);
        animator.setDuration(300).setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    class MyTouch implements PfFragmentLinearLayout.OnInterceptTouchEvent {


        private float startY;

        @Override
        public boolean onInterceptTouch(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startY = event.getRawY();
                    rawY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    CGLog.v("打印scrollview滑动的距离　：　"+pfFusionFragment.getBanScrollView().getRefreshableView().getScrollY());
                    moveY = event.getRawY() - rawY;
                    CGLog.v("打印移动值 ： "+(int)moveY);
                    bottomRequest();
                    if(getFlTopMargin() == 0 && event.getRawY() - startY > 50){
                        flMoveBottom();
                        flIsLocationTop = false;
                        locationChanged.onBottom();
                    }
                    //当不在顶部的时候，scrollview停止滑动
                    if(getFlTopMargin() == - back_pf_scroll_fl.getHeight()){
                        locationChanged.onTop();
                    }else{
                        locationChanged.onBottom();
                    }
                    rawY = event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    //下移
                    CGLog.v("打印差值 ： "+((int)event.getRawY() - rawY));


                    //上移
                    if(event.getRawY() - startY < -30){
                        flMoveTop();
                        flIsLocationTop = true;

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                locationChanged.onTop();
                            }
                        },300);

                    }
                    break;
            }
            return false;
        }
    }

    class Wrapper {
        FrameLayout frameLayout;
        private int topMargin;

        public Wrapper(FrameLayout frameLayout) {
            this.frameLayout = frameLayout;
        }

        public int getTopMargin() {
            return ((LinearLayout.LayoutParams)(frameLayout.getLayoutParams())).topMargin;
        }

        public void setTopMargin(int topMargin) {
            ((LinearLayout.LayoutParams)(frameLayout.getLayoutParams())).topMargin = topMargin;
            frameLayout.requestLayout();
        }
    }

    public interface LocationChanged{
        void onTop();
        void onBottom();
    }

}
