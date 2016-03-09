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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ant.liao.GifView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nineoldandroids.animation.ObjectAnimator;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.PfAlbumListSun;
import com.wj.kindergarten.bean.PfFamilyUuid;
import com.wj.kindergarten.bean.PopAttributes;
import com.wj.kindergarten.ui.func.adapter.BoutiqueChooseItemAdapter;
import com.wj.kindergarten.ui.func.adapter.FusionChooseItemAdapter;
import com.wj.kindergarten.ui.func.adapter.TopViewAdapter;
import com.wj.kindergarten.ui.imagescan.BitmapCallBack;
import com.wj.kindergarten.ui.mine.photofamilypic.BoutiqueGalleryActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.ConllectPicActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.FusionListFragment;
import com.wj.kindergarten.ui.mine.photofamilypic.PfEditInfoActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.PfFragmentLinearLayout;
import com.wj.kindergarten.ui.mine.photofamilypic.PfFusionFragment;
import com.wj.kindergarten.ui.mine.photofamilypic.PfUpGalleryActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.BoutiqueAlbumFragment;
import com.wj.kindergarten.ui.mine.photofamilypic.PointPositionListener;
import com.wj.kindergarten.ui.mine.photofamilypic.UpLoadActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.observer.ObserverFamilyUuid;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.Constant.BitmapUtil;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.PopWindowUtil;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;
import com.wj.kindergarten.utils.WindowUtils;

import net.tsz.afinal.FinalDb;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangt on 2016/1/14.
 */
public class PhotoFamilyFragment extends Fragment{
    public static final int ADD_PIC = 5001;
    private TabLayout tab_layout;
    //    private FragmentPagerAdapter pagerAdapter;
    private List<PfAlbumListSun> albumList;
    private View view;
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
    private RelativeLayout pf_pic_left_bt;
    private TextView pf_pic_center_tv;
    private RelativeLayout pf_pic_right_iv;
    private FinalDb db;
    private FusionListFragment pfFusionListFragment;
    private TextView pf_background_show_number;
    private List<PfAlbumListSun> pfAlbumListSunList = new ArrayList<>();
    private FinalDb dbFamilyAlbum;
    private PfAlbumListSun pfAlbumListSun;
    private String currentFamily_uuid;
    public static PhotoFamilyFragment instance;
    private View topVeiw;
    private PullToRefreshListView topViewPullList;
    private TopViewAdapter topViewAdapter;
    private ObserverFamilyUuid observer;
    private PopAttributes popRight;
    private PopAttributes popleft;
    private PopAttributes popTop;
    private int topPosition;
    private PfFusionFragment pfFusionFragment;

    public ObserverFamilyUuid getObserver() {
        return observer;
    }

    public String getCurrentFamily_uuid() {
        return currentFamily_uuid;
    }

    public void startGif(){
        if(gif == null)return;
        if(gif.getVisibility() == View.INVISIBLE){
            gif.setVisibility(View.VISIBLE);
        }
    }
    public void stopGif(){
        if(gif != null){
            gif.setVisibility(View.INVISIBLE);
        }
    }

    private int getFlTopMargin(){
        LinearLayout.LayoutParams params  = (LinearLayout.LayoutParams) back_pf_scroll_fl.getLayoutParams();
        return params.topMargin;
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };

    public void setSubViewDecideScroll(PfFragmentLinearLayout.DecideSubViewScroll decideScroll){
        pf_back_ll.setDecideSubViewScroll(decideScroll);
    }

    public void setLocationChanged(LocationChanged locationChanged) {
        this.locationChanged = locationChanged;
    }

    public boolean isFlIsLocationTop() {
        return flIsLocationTop;
    }

    private boolean isOne;
    private float moveY;
    private ArrayList<AllPfAlbumSunObject> list = new ArrayList<>();
    public boolean canScroll = true;
    private ObjectAnimator animotor;

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }


    //标签 ： 时光轴，精品相册
    private String[] fragment_tags = new String[]{"fusionList", "boutique_album","fusion"};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initHead();
        if (view != null){
            return view;
        }
        view = inflater.inflate(R.layout.photo_family_pic, null);
        instance = this;
        initDb();
        initFamilyAlbumData();
        initBar();
        initFamilyAlbum();
        initObserver();
        initHeadView();
        initViews(view);
        initHeadViewData();
        initFragment();
        initClickListener();
        initTabLayout();

        return view;
    }

    private void initObserver() {
        observer = new ObserverFamilyUuid();
    }

    private void initFamilyAlbum() {
        if(pfAlbumListSunList.size() != 0){
            pfAlbumListSun = pfAlbumListSunList.get(0);
            currentFamily_uuid = pfAlbumListSun.getUuid();
            if(!TextUtils.isEmpty(pfAlbumListSun.getTitle())){
                pf_pic_center_tv.setText(""+Utils.isNull(pfAlbumListSun.getTitle()));
            }else {
                pf_pic_center_tv.setText("家庭相册-1");
            }

        }
    }

    private void initDb() {
        db = FinalDb.create(getActivity(), GloablUtils.FAMILY_UUID_OBJECT);
        dbFamilyAlbum = FinalDb.create(getActivity(),GloablUtils.FAMILY_UUID);
    }

    private void initFamilyAlbumData() {
        if(MainActivity.instance.getAlbumList() != null && MainActivity.instance.getAlbumList().size() > 0){
            pfAlbumListSunList.addAll(MainActivity.instance.getAlbumList());
        }else {
            List<PfFamilyUuid> pfFamilyUuids =  dbFamilyAlbum.findAll(PfFamilyUuid.class);
            if(pfFamilyUuids == null || pfFamilyUuids.size() == 0) return;
            int size = pfFamilyUuids.size();
            for(int k = 0 ; k < size ; k ++){
                PfAlbumListSun sun = new PfAlbumListSun();
                sun.setUuid(pfFamilyUuids.get(k).getFamily_uuid());
                pfAlbumListSunList.add(sun);
            }
        }
    }


    private void initBar() {
        pf_pic_left_bt = (RelativeLayout) view.findViewById(R.id.pf_pic_left_bt);
        pf_pic_center_tv = (TextView) view.findViewById(R.id.pf_pic_center_tv);
        pf_pic_right_iv = (RelativeLayout) view.findViewById(R.id.pf_pic_right_iv);

        pf_pic_left_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLeft();
            }
        });
        pf_pic_center_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  changePFAlbum();
            }
        });
        pf_pic_right_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRightListener();
            }
        });
    }

    private void changePFAlbum() {

        if(popTop == null){
            popTop = new PopAttributes();
            popTop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popTop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            popTop.setGrarity(Gravity.NO_GRAVITY);
            popTop.setLeftOffset(0);
        }
        final TopViewAdapter topAdapter = new TopViewAdapter(getActivity(), pfAlbumListSunList,topPosition);
        PopWindowUtil.showPoPWindow(getActivity(), pf_pic_center_tv, topAdapter,
                popTop, new PopWindowUtil.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {
                        PfAlbumListSun sun = (PfAlbumListSun) topAdapter.getItem(position - 1);
                        currentFamily_uuid = sun.getUuid();
                        observer.setFamily_uuid(currentFamily_uuid);
                        topPosition = position - 1;
                        PfAlbumListSun albumListSun = (PfAlbumListSun) topAdapter.getItem(topPosition);
                        String title = Utils.isNull(albumListSun.getTitle());
                        if(TextUtils.isEmpty(title)){
                            title = "家庭相册--"+topPosition;
                        }
                        pf_pic_center_tv.setText(""+title);
                    }
                });
    }

    public void initHeadBack(){
        initHeadViewData();
    }

    private void initHeadViewData() {
        if(pfAlbumListSun == null) return;
        if(TextUtils.isEmpty(pfAlbumListSun.getTitle())) return;
        int count = Integer.valueOf(pfAlbumListSun.getPhoto_count());
        String n = null;
        if(count > 1000){
            n =  pfAlbumListSun.getPhoto_count().substring(0,1);
            n = n+".k";
        }else{
            n = pfAlbumListSun.getPhoto_count();
        }
        pf_background_show_number.setText(""+n);
        if(!TextUtils.isEmpty(pfAlbumListSun.getHerald())){
            ImageLoaderUtil.displayImage(pfAlbumListSun.getHerald(),pf_backGround_image);
            BitmapCallBack.loadBitmap(pfAlbumListSun.getHerald(), new BitmapCallBack.GetBitmapCallback() {
            @Override
            public void callback(Bitmap bitmap) {
                BitmapDrawable drawable =  BitmapUtil.blur(getResources(), -1, bitmap);
                pf_backGround_rl.setBackground(drawable);
            }
        });
        }
        String text = Utils.isNull(pfAlbumListSun.getTitle());
        if(TextUtils.isEmpty(text)){
            text = "问界互动家园";
        }else{
        }
        pf_backGround_family_name.setText("" + text);

    }

    private void initHeadView() {
        gif = (GifView) view.findViewById(R.id.pf_family_gif);
        gif.setVisibility(View.INVISIBLE);
        gif.setGifImage(R.drawable.update_jtxc_red);
        pf_backGround_rl = (RelativeLayout) view.findViewById(R.id.pf_backGround_rl);
        pf_backGround_image = (ImageView) view.findViewById(R.id.pf_backGround_image);
        pf_backGround_family_name = (TextView) view.findViewById(R.id.pf_backGround_family_name);
        pf_backGround_count = (TextView) view.findViewById(R.id.pf_backGround_count);
        pf_backGround_count_right = (TextView) view.findViewById(R.id.pf_backGround_count_right);
        pf_backGround_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                  Intent intent = new Intent(getActivity(), PfAlbumListActivity.class);
//                startActivity(intent);
            }
        });
    }

    private void initFragment() {
        pfFusionListFragment = new FusionListFragment(this,currentFamily_uuid);
        getFragmentManager().beginTransaction().replace(R.id.pf_change_content_fl, pfFusionListFragment, fragment_tags[0]).commit();
    }

    private void initHead() {
        ((MainActivity)getActivity()).hideActionbar();
    }

    public void updateBoutiqueFragmentData(String uuid){
        if(boutique_album_framgent != null){
            boutique_album_framgent.updateData(uuid);
        }
    }

    private void clickLeft() {
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
                if (pfAlbumListSun == null) {
                    ToastUtils.showMessage("暂无相册信息!");
                    return;
                }
                Intent intent = new Intent(getActivity(), PfEditInfoActivity.class);
                intent.putExtra("uuid", pfAlbumListSun.getUuid());
                MainActivity.instance.startActivityForResult(intent, GloablUtils.UPDATE_SUCCESSED_REFRESH);
                popupWindow.dismiss();
            }
        });

        Utils.setPopWindow(popupWindow);
        popupWindow.showAsDropDown(pf_pic_left_bt);
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
        pf_back_ll.setContentFl(back_pf_scroll_fl);
        pf_background_show_number = (TextView) v.findViewById(R.id.pf_background_show_number);
    }



    String[] titles = new String[]{"时光轴", "精品相册"};

    int boutiquePosition = 1;
    int fusionPosition = 0;
    private void initTabLayout() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tab_layout.getLayoutParams();
        params.height = (int)(Float.valueOf(45)*Float.valueOf(WindowUtils.getDesnity()));
        CGLog.v("打印密度 : " + WindowUtils.getDesnity());
        CGLog.v("打印tab高度 : " + params.height);
        tab_layout.setLayoutParams(params);
        tab_layout.addTab(tab_layout.newTab().setCustomView(R.layout.photo_tab_fusion));
        tab_layout.addTab(tab_layout.newTab().setCustomView(R.layout.photo_tab_boutique_album));
        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        FusionListFragment fusionListFragment = (FusionListFragment) getFragmentManager().findFragmentByTag(fragment_tags[0]);
                        if (fusionListFragment != null) {
                            getFragmentManager().beginTransaction().replace(R.id.pf_change_content_fl, fusionListFragment, fragment_tags[0]).commit();
                        } else {
                            getFragmentManager().beginTransaction().replace(R.id.pf_change_content_fl, pfFusionListFragment, fragment_tags[0]).commit();
                        }
                        break;
                    case 1:
                        //精品相册
                        if (getFragmentManager().findFragmentByTag(fragment_tags[1]) == null) {
                            if(boutique_album_framgent == null)boutique_album_framgent = new BoutiqueAlbumFragment(PhotoFamilyFragment.this);
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
                switch (tab.getPosition()){
                    case 0 :
                        if(popleft == null){
                            popleft = new PopAttributes();
                            popleft.setWidth(WindowUtils.dm.widthPixels / 2);
                            popleft.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                            popleft.setGrarity(Gravity.NO_GRAVITY);
                            popleft.setLeftOffset(0);
                        }
                        PopWindowUtil.showPoPWindow(getActivity(), tab_layout, new FusionChooseItemAdapter(getActivity(), fusionPosition),
                                popleft, new PopWindowUtil.OnItemClickListener() {
                                    @Override
                                    public void onItemClickListener(int position) {
                                        if(fusionPosition == position - 1) return;
                                        fusionPosition = position - 1;
                                        changFusionFragment();
                                    }
                                });
                        break;
                    case 1 :
                        if(popRight == null){
                            popRight = new PopAttributes();
                            popRight.setWidth(WindowUtils.dm.widthPixels / 2);
                            popRight.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                            popRight.setGrarity(Gravity.NO_GRAVITY);
                            popRight.setLeftOffset(WindowUtils.dm.widthPixels/2);
                        }
                        PopWindowUtil.showPoPWindow(getActivity(), tab_layout, new BoutiqueChooseItemAdapter(getActivity(), boutiquePosition),
                                popRight, new PopWindowUtil.OnItemClickListener() {
                                    @Override
                                    public void onItemClickListener(int position) {
                                        if(boutiquePosition == position - 1) return;
                                        boutiquePosition = position - 1;
                                        boutique_album_framgent.loadDataAccordingType(boutiquePosition);
                                    }
                                });
                        break;
                }
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

    private void changFusionFragment() {
        if(fusionPosition == 0){
            FusionListFragment fusionListFragment = (FusionListFragment) getFragmentManager().findFragmentByTag(fragment_tags[0]);
            if (fusionListFragment != null) {
                getFragmentManager().beginTransaction().replace(R.id.pf_change_content_fl, fusionListFragment, fragment_tags[0]).commit();
            } else {
                getFragmentManager().beginTransaction().replace(R.id.pf_change_content_fl, pfFusionListFragment, fragment_tags[0]).commit();
            }
        }else if(fusionPosition == 1){
            PfFusionFragment fusionFragment = (PfFusionFragment) getFragmentManager().findFragmentByTag(fragment_tags[2]);
            if (fusionFragment != null) {
                getFragmentManager().beginTransaction().replace(R.id.pf_change_content_fl, fusionFragment, fragment_tags[2]).commit();
            } else {
                pfFusionFragment = new PfFusionFragment(this,currentFamily_uuid);
                getFragmentManager().beginTransaction().replace(R.id.pf_change_content_fl, pfFusionFragment, fragment_tags[2]).commit();
            }

        }
    }


    public void refreshFusionData(){
        pfFusionListFragment.refreshData();
    }

    public interface LocationChanged{
        void onTop();
        void onBottom();
    }

}
