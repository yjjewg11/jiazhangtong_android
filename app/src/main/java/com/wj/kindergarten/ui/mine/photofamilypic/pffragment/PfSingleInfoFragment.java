package com.wj.kindergarten.ui.mine.photofamilypic.pffragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbum;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.PfDianZan;
import com.wj.kindergarten.bean.SingleNewInfo;
import com.wj.kindergarten.bean.SinlePfExtraInfo;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.emot.SendMessage;
import com.wj.kindergarten.ui.emot.ViewEmot2;
import com.wj.kindergarten.ui.func.adapter.PfPopPicAdapter;
import com.wj.kindergarten.ui.imagescan.AutoDownLoadListener;
import com.wj.kindergarten.ui.mine.photofamilypic.PfGalleryActivity;
import com.wj.kindergarten.ui.mine.photofamilypic.SinglePfEditActivity;
import com.wj.kindergarten.ui.more.ListenScrollView;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.ShareUtils;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;
import com.wj.kindergarten.utils.WindowUtils;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PfSingleInfoFragment extends Fragment {
    View fragmentView;
    private List<AllPfAlbumSunObject> list = new ArrayList<>();
    private ViewPager viewPager;
    private AllPfAlbumSunObject sunObject;
    private int position;
    private FinalDb family_uuid_object;

    private TextView[] textViews;
    private PagerAdapter pagerAdapter;
    private PfGalleryActivity activity;
    private SinlePfExtraInfo singleInfo;
    private ViewEmot2 emot2;
    private FrameLayout frame_bottom_tab;
    private LinearLayout bottom_assess;
    private View pf_more_view;
    private ImageView pf_delete;
    private ImageView pf_edit;
    private int[] location;
    private FinalDb db;

    public PfSingleInfoFragment(int position, List<AllPfAlbumSunObject> list) {
        this.position = position;
        this.list.clear();
        this.list.addAll(list);
    }

    private View view;
    private GridView pf_pop_gridView;
    private PfPopPicAdapter pfAdapter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };


    public void changeData(int position,List<AllPfAlbumSunObject> list){
        this.position = position;
        this.list.clear();
        this.list.addAll(list);
        pagerAdapter.notifyDataSetChanged();
    }

    public void updateSingleData(AllPfAlbumSunObject object){
        int position  =  list.indexOf(object);
        list.remove(position);
        list.add(position, object);
        pagerAdapter.notifyDataSetChanged();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(fragmentView != null) return fragmentView;
        activity = (PfGalleryActivity) getActivity();
        fragmentView = inflater.inflate(R.layout.pf_gallery_activity,null);
        initDB();
        initViews();
        initBottomBt();
        changeTitle();
        return fragmentView;
    }

    private void initDB() {
        db = FinalDb.create(getActivity(),GloablUtils.FAMILY_UUID_OBJECT);
    }

    private void initViews() {
        viewPager = (ViewPager) fragmentView.findViewById(R.id.pf_edit_viewPager);
        pagerAdapter = new PagerAdapter() {

            int mcount = 0;
            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                View view = View.inflate(getActivity(), R.layout.pf_gallery_fragment, null);
                final ListenScrollView pf_single_scroll = (ListenScrollView) view.findViewById(R.id.pf_single_scroll);
                final ImageView pf_gallery_image = (ImageView) view.findViewById(R.id.pf_gallery_image);
                final FrameLayout pf_gallery_fl = (FrameLayout) view.findViewById(R.id.pf_gallery_fl);
                final TextView pf_info_careme_time = (TextView) view.findViewById(R.id.pf_info_careme_time);
                final TextView pf_info_location = (TextView) view.findViewById(R.id.pf_info_location);
                final TextView pf_info_device = (TextView) view.findViewById(R.id.pf_info_device);
                final TextView pf_info_upload_people = (TextView) view.findViewById(R.id.pf_info_upload_people);
                AllPfAlbumSunObject object = null;

                pf_single_scroll.setOnScrollChanged(new ListenScrollView.OnScrollChanged() {
                    @Override
                    public void onScrollChanged(int l, int t, int oldl, int oldt) {
                        //发生滑动就隐藏输入法
                        bottomCancle();
                    }
                });
                if (list != null && list.size() > 0) object = list.get(position);
                if (object != null) {
                    String path = object.getPath();
                    if(!TextUtils.isEmpty(path)){
                        if(path.contains("@")){
                            path = path.substring(0,path.indexOf("@"));
                        }
                    }
                    ImageLoaderUtil.downLoadImageLoader(path, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            pf_gallery_image.setImageBitmap(loadedImage);
                            //将图片按比例放大显示
                            int height = (WindowUtils.dm.widthPixels / loadedImage.getWidth()) * loadedImage.getHeight();
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) pf_gallery_image.getLayoutParams();
                            params.height = height;
                            pf_gallery_image.setLayoutParams(params);
                            //判断图片大小是否要显示上面的信息
                            if (height > WindowUtils.dm.heightPixels / 3 * 2) {
                                pf_gallery_fl.setVisibility(View.GONE);
                                //TODO 显示上部分的标题
//                                setTitleRightImage(R.drawable.xinxi_photo, 0);
                            } else {
//                                setTitleRightImage(0, 0);
                                AllPfAlbumSunObject object = list.get(position);
                                if (object != null) {
                                    pf_info_careme_time.append("" + Utils.isNull(object.getCreate_time()));
                                    pf_info_location.append("" + Utils.isNull(object.getAddress()));
                                    pf_info_device.append("" + Utils.isNull(object.getCreate_useruuid()));
                                    pf_info_upload_people.append("" + Utils.isNull(object.getCreate_useruuid()));
                                }
                            }
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    });
                } else {
                }
                container.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return view;
            }

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public int getItemPosition(Object object) {
                if(mcount > 0){
                    mcount --;
                    return POSITION_NONE;
                }
                return super.getItemPosition(object);
            }

            @Override
            public void notifyDataSetChanged() {
                mcount = getCount();
                super.notifyDataSetChanged();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        };
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(position);
    }

    public void showRightInfo(){
        View view = View.inflate(getActivity(),R.layout.pf_info_four_message,null);
        TextView pf_info_careme_time = (TextView) view.findViewById(R.id.pf_info_careme_time);
        TextView pf_info_location = (TextView) view.findViewById(R.id.pf_info_location);
        TextView pf_info_device = (TextView) view.findViewById(R.id.pf_info_device);
        TextView pf_info_upload_people = (TextView) view.findViewById(R.id.pf_info_upload_people);
        AllPfAlbumSunObject object = list.get(viewPager.getCurrentItem());
        if(object != null){
            pf_info_careme_time.append("" + Utils.isNull(object.getCreate_time()));
            pf_info_location.append("" + Utils.isNull(object.getAddress()));
            pf_info_device.append("" + Utils.isNull(object.getCreate_useruuid()));
            pf_info_upload_people.append("" + Utils.isNull(object.getCreate_useruuid()));
        }
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Utils.setPopWindow(popupWindow);
        popupWindow.showAsDropDown(fragmentView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    private void queryItemNewInfo(String uuid) {
        UserRequest.getSinglePfInfo(getActivity(), uuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                SingleNewInfo singleNewInfo = (SingleNewInfo) domain;
                if (singleNewInfo != null) {
                    sunObject = singleNewInfo.getData();
                    sunObject.setStatus(0);
                    family_uuid_object.update(sunObject);
                    pagerAdapter.notifyDataSetChanged();
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

    private void initBottomBt() {
        bottom_assess = (LinearLayout) fragmentView.findViewById(R.id.pf_bottom_assess_linear);
        frame_bottom_tab = (FrameLayout) fragmentView.findViewById(R.id.bottom_tab);
        textViews = new TextView[]{
                (TextView) fragmentView.findViewById(R.id.pf_bottom_collect),
                (TextView) fragmentView.findViewById(R.id.pf_bottom_assess),
                (TextView) fragmentView.findViewById(R.id.pf_bottom_dianzan),
                (TextView) fragmentView.findViewById(R.id.pf_bottom_share),
                (TextView) fragmentView.findViewById(R.id.pf_bottom_more),
        };

        //初始化底部评论模块
        emot2 = new ViewEmot2(getActivity(), new SendMessage() {
            @Override
            public void send(String message) {
                sendReply(message);
            }
        });

        bottom_assess.addView(emot2);

        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sunObject == null) return;
                switch (v.getId()) {
                    case R.id.pf_bottom_collect:
                        String text = textViews[0].getText().toString();
                        if(text.equals("收藏")){
                            //进行收藏
                            store();
                        }else{
                            //取消收藏
                            cacleStore();
                        }
                        break;
                    case R.id.pf_bottom_assess:
                        beginAssess();
                        break;
                    case R.id.pf_bottom_share:
                        AllPfAlbumSunObject object = list.get(viewPager.getCurrentItem());
                        String note = object.getNote();
                        ShareUtils.showShareDialog(getActivity(), textViews[3], note, note, object.getPath(), object.getPath(), false);
                        break;
                    case R.id.pf_bottom_dianzan:
                        String dianzanText = textViews[2].getText().toString();
                        if(dianzanText.equals("点赞")){
                            dianzan();
                        }else{
                            cancleDianzan();
                        }
                        break;
                    case R.id.pf_bottom_more:
                        showMore();
                        break;
                }
            }
        };

        for (TextView textView : textViews) {
            textView.setOnClickListener(listener);
        }
    }

    private void showMore() {
        if(pf_more_view == null){
            pf_more_view = View.inflate(getActivity(),R.layout.pf_single_more,null);
            pf_delete = (ImageView) pf_more_view.findViewById(R.id.pf_single_delete);
            pf_edit = (ImageView) pf_more_view.findViewById(R.id.pf_single_edit);
        }

        final PopupWindow popupWindow = new PopupWindow(pf_more_view,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        Utils.setPopWindow(popupWindow, R.style.ShareAnimBPB);
        pf_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                deleteData();
            }
        });
        pf_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                activity.startEditActivity(list.get(viewPager.getCurrentItem()));
            }
        });
        int [] location = getLocation(textViews[4]);
        pf_more_view.measure(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        CGLog.v("打印测量的高度 　："+pf_more_view.getMeasuredHeight() +" 宽度 : "+pf_more_view.getMeasuredWidth());
        pf_more_view.getMeasuredHeight();
        popupWindow.showAtLocation(textViews[4], Gravity.NO_GRAVITY, location[0], location[1] - pf_more_view.getMeasuredHeight());
    }

    private void deleteData() {
        //从数据库，网络，轮播图中删除
        db.deleteById(AllPfAlbumSunObject.class, sunObject.getUuid());
        list.remove(viewPager.getCurrentItem());
        pagerAdapter.notifyDataSetChanged();
        UserRequest.deleteSinglePf(getActivity(), sunObject.getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                ToastUtils.showMessage("删除成功");
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    public void cancleDianzan(){
        activity.showDialog("取消点赞中...");
        UserRequest.commonCancleZan(getActivity(), sunObject.getUuid(), GloablUtils.MODE_OF_PF, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                activity.cancleDialog();
                Utils.cancleDizanStatus(getActivity(), textViews[2]);
                ToastUtils.showMessage("取消点赞成功");
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    private void dianzan() {
        activity.showDialog("点赞中...");
        UserRequest.commonZan(getActivity(), sunObject.getUuid(), GloablUtils.MODE_OF_PF, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                ToastUtils.showMessage("点赞成功");
                Utils.showDianzanStatus(getActivity(), textViews[2]);
                activity.cancleDialog();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    private void beginAssess() {
        bottomShow();
    }

    public void bottomShow(){
        emot2.showSoftKeyboard();
        bottom_assess.setVisibility(View.VISIBLE);
    }
    public void bottomCancle(){
        emot2.hideSoftKeyboard();
        bottom_assess.setVisibility(View.GONE);
    }

    private void sendReply(String message) {
        activity.showDialog("评论回复中...");
        Utils.commonSendReply(getActivity(), sunObject.getUuid(), sunObject.getCreate_useruuid(), "", message, GloablUtils.MODE_OF_PF, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                //TODO
                bottomCancle();
                activity.cancleDialog();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    private void cacleStore() {
        activity.showDialog("取消收藏中...");
        Utils.commonCancleStore(getActivity(), sunObject.getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                activity.cancleDialog();
                ToastUtils.showMessage("取消收藏成功");
                Utils.cancleStoreStatus(getActivity(), textViews[0]);
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    private void store() {
        activity.showDialog("收藏中...");
        Utils.commonStore(getActivity(), sunObject.getNote(), GloablUtils.MODE_OF_PF, sunObject.getUuid(), sunObject.getPath(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                activity.cancleDialog();
                ToastUtils.showMessage("收藏成功");
                Utils.showStoreStatus(getActivity(), textViews[0]);
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    private void changeTitle() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (list == null || list.size() == 0) return;
                pageNo = 1;
                maxTime = TimeUtil.getStringDate(new Date());
                sunObject = list.get(position);
                setTitleText(TimeUtil.getYMDTimeFromYMDHMS(sunObject.getCreate_time()));
                //做调试，屏蔽
                if (sunObject.getStatus() != 0) {
                    queryItemNewInfo(sunObject.getUuid());
                }
                queryExtraData(sunObject.getUuid());
                queSingleAssess();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    int pageNo = 1;
    String maxTime = TimeUtil.getStringDate(new Date());
    private void queSingleAssess() {
        UserRequest.getPfSingleAssess(getActivity(), pageNo, GloablUtils.MODE_OF_PF, sunObject.getUuid(), maxTime,
                new RequestResultI() {
                    @Override
                    public void result(BaseModel domain) {

                    }

                    @Override
                    public void result(List<BaseModel> domains, int total) {

                    }

                    @Override
                    public void failure(String message) {

                    }
                });
    }

    private void queryExtraData(String uuid) {
        UserRequest.getSinglePfExtraInfo(getActivity(), uuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                singleInfo = (SinlePfExtraInfo) domain;
                if (singleInfo != null) {
                    initCollect();
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

    private void initCollect() {
            //可以收藏
        if(singleInfo.isFavor()){
            Utils.cancleStoreStatus(getActivity(),textViews[0]);
        }else{
            Utils.showStoreStatus(getActivity(),textViews[0]);
        }


        PfDianZan dianZan =  singleInfo.getDianZan();
        if(dianZan != null){
            //可以点赞
            if(dianZan.getYidianzan() == 0){
                Utils.cancleDizanStatus(getActivity(),textViews[2]);
            }else{
                Utils.showDianzanStatus(getActivity(), textViews[2]);
            }
        }
    }

    private void setTitleText(String ymdTimeFromYMDHMS) {
        activity.changeTitle(ymdTimeFromYMDHMS);
    }

    public int[] getLocation(View view) {
        //获取更多按钮的位置
        int [] location = new int[2];
        view.getLocationInWindow(location);
        return location;
    }
}
