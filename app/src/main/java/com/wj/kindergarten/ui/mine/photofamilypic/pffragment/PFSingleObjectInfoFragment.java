package com.wj.kindergarten.ui.mine.photofamilypic.pffragment;

import android.app.MediaRouteButton;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.PfDianZan;
import com.wj.kindergarten.bean.PfSingleAssess;
import com.wj.kindergarten.bean.PfSingleAssessObject;
import com.wj.kindergarten.bean.Reply;
import com.wj.kindergarten.bean.SingleNewInfo;
import com.wj.kindergarten.bean.SinlePfExtraInfo;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.emot.EmotUtil;
import com.wj.kindergarten.ui.emot.SendMessage;
import com.wj.kindergarten.ui.emot.ViewEmot2;
import com.wj.kindergarten.ui.func.adapter.PfCommonAssessAdapter;
import com.wj.kindergarten.ui.func.adapter.PfInfoFragmentAdapter;
import com.wj.kindergarten.ui.mine.photofamilypic.PfGalleryActivity;
import com.wj.kindergarten.ui.more.ListenScrollView;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.HintInfoDialog;
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

/**
 * Created by tangt on 2016/2/23.
 */
public class PFSingleObjectInfoFragment extends Fragment {
    private final int UPDATE_ASSESS_COUNT = 301;
    private final int POP_DELAY_300 = 300;
    private AllPfAlbumSunObject sunObject;
    private ListenScrollView pf_single_scroll;
    private View innerView;
    private String maxTime;
    int pageNo = 1;
    private FinalDb family_uuid_object;
    private SinlePfExtraInfo singleInfo;
    private ImageView pf_gallery_image;
    private LinearLayout top_left;
    private TextView title_webview_normal_text;
    private TextView title_webview_normal_spinner;
    private ImageView normal_title_right_icon;
    private LinearLayout bottom_assess;
    private FrameLayout frame_bottom_tab;
    private TextView[] textViews;
    private ViewEmot2 emot2;
    private HintInfoDialog dialog;
    private View pf_more_view;
    private PfSingleInfoFragment pfSingleInfoFragment;
    private ImageView pf_delete;
    private ImageView pf_edit;
    private PfInfoFragmentAdapter pfInfoFragmentAdapter;
    private TextView pf_gallery_fragment_extra_info_description;
    private TextView pf_gallery_fragment_extra_info_time;
    private TextView pf_gallery_fragment_extra_info_human;
    private TextView pf_gallery_fragment_extra_info_address;
    private View assessView;
    private TextView pf_common_show_assess_title;
    private PullToRefreshListView assessListView;
    private PopupWindow popAssessWindow;
    private PfCommonAssessAdapter pfCommonAssessAdapter;
    private List<PfSingleAssessObject> assessObjectList = new ArrayList<>();
    private LinearLayout pf_pic_bottom_viewGroup;
    private TextView pf_pic_bottom_assess_count;
    private Handler mHanlder = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case POP_DELAY_300:
                    emot2.showSoftKeyboard();
                    bottom_assess.setVisibility(View.VISIBLE);
                    pf_pic_bottom_viewGroup.setVisibility(View.GONE);
                    break;
                case UPDATE_ASSESS_COUNT:
                    int count = singleInfo.getReply_count();
                    if (count == 0) {
                        pf_pic_bottom_assess_count.setVisibility(View.GONE);
                    } else {
                        pf_pic_bottom_assess_count.setVisibility(View.VISIBLE);
                    }
                    pf_pic_bottom_assess_count.setText("" + count);
                    break;
            }
        }
    };

    public PFSingleObjectInfoFragment(PfInfoFragmentAdapter pfInfoFragmentAdapter,PfSingleInfoFragment pfSingleInfoFragment) {
        this.pfSingleInfoFragment = pfSingleInfoFragment;
        this.pfInfoFragmentAdapter = pfInfoFragmentAdapter;
    }

    public PFSingleObjectInfoFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //在每次重用view之后都会更新数据

        if(innerView != null){
            initData();
            showPic();
            return innerView;
        }
        dialog = new HintInfoDialog(getActivity(),"加载数据中...请稍后");
        family_uuid_object = FinalDb.create(getActivity(), GloablUtils.FAMILY_UUID_OBJECT);
        innerView = View.inflate(getActivity(), R.layout.pf_gallery_fragment, null);
        initExtraView();
        pf_pic_bottom_assess_count = (TextView) innerView.findViewById(R.id.pf_pic_bottom_assess_count);
        pf_pic_bottom_viewGroup = (LinearLayout) innerView.findViewById(R.id.pf_pic_bottom_viewGroup);
        pf_single_scroll = (ListenScrollView) innerView.findViewById(R.id.pf_single_scroll);
        pf_gallery_image = (ImageView) innerView.findViewById(R.id.pf_gallery_image);
        pf_single_scroll.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        maxTime = TimeUtil.getStringDate(new Date());
        pf_single_scroll.setOnScrollChanged(new ListenScrollView.OnScrollChanged() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                //隐藏输入法
                bottomCancle();
            }
        });
        pf_gallery_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = new ArrayList();
                list.add(sunObject.getPath());
                Utils.carouselPic(getActivity(), 0, (ArrayList<String>) list);
            }
        });
        pf_single_scroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageNo++;
                queSingleAssess();
            }
        });
        initData();
        showPic();
        chcekUpadate();
        queryExtraData(sunObject.getUuid());
        queSingleAssess();
        initBottomBt();
        initTopView();

        return innerView;
    }

    private void initExtraView() {
        pf_gallery_fragment_extra_info_description = (TextView) innerView.findViewById(R.id.pf_gallery_fragment_extra_info_description);
        pf_gallery_fragment_extra_info_time = (TextView) innerView.findViewById(R.id.pf_gallery_fragment_extra_info_time);
        pf_gallery_fragment_extra_info_address = (TextView) innerView.findViewById(R.id.pf_gallery_fragment_extra_info_address);
        pf_gallery_fragment_extra_info_human = (TextView) innerView.findViewById(R.id.pf_gallery_fragment_extra_info_human);
    }

    private void initData() {
        if(pfInfoFragmentAdapter != null){
            sunObject = pfInfoFragmentAdapter.getCurrentObject();
            updatePfInfo();
        }
    }

    private void initTopView() {
        top_left = (LinearLayout) innerView.findViewById(R.id.normal_title_left_layout);
        title_webview_normal_text = (TextView) innerView.findViewById(R.id.title_webview_normal_text);
        title_webview_normal_spinner = (TextView) innerView.findViewById(R.id.title_webview_normal_spinner);
        title_webview_normal_spinner.setText("");
        title_webview_normal_spinner.setCompoundDrawables(null, null, null, null);
        normal_title_right_icon = (ImageView) innerView.findViewById(R.id.normal_title_right_icon);
        top_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
//        normal_title_right_icon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showRightInfo();
//            }
//        });
        Drawable drawable = getResources().getDrawable(R.drawable.pf_card_view);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getIntrinsicHeight());
//        title_webview_normal_text.setCompoundDrawables(null, null, drawable, null);
        title_webview_normal_text.setCompoundDrawablePadding(4);
        title_webview_normal_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PfGalleryActivity)getActivity()).checkAlbumListFragment();
            }
        });
        title_webview_normal_text.setText(""+TimeUtil.getYMDTimeFromYMDHMS(Utils.isNull(sunObject.getCreate_time())));
    }

    private void chcekUpadate() {
        if (sunObject.getStatus() != 0) {
            queryItemNewInfo(sunObject.getUuid());
        }else{
            updatePfInfo();
        }
    }

    private void updatePfInfo() {
        pf_gallery_fragment_extra_info_description.setText(""+Utils.isNull(sunObject.getNote()));
        pf_gallery_fragment_extra_info_time.setText("拍摄时间: "+Utils.isNull(sunObject.getPhoto_time()));
        pf_gallery_fragment_extra_info_address.setText("拍摄地点: "+Utils.isNull(sunObject.getAddress()));
        pf_gallery_fragment_extra_info_human.setText("上传人: "+Utils.isNull(sunObject.getCreate_user()));
    }

    private void showAssessList() {
        if (assessView == null) {
            assessView = View.inflate(getActivity(), R.layout.pf_common_show_assess_layout, null);
            pf_common_show_assess_title = (TextView) assessView.findViewById(R.id.pf_common_show_assess_title);
            assessListView = (PullToRefreshListView) assessView.findViewById(R.id.pulltorefresh_list);
            assessListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
            assessListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    pageNo++;
                    queSingleAssess();
                }
            });
            pf_common_show_assess_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popAssessWindow.dismiss();
                }
            });
            pfCommonAssessAdapter = new PfCommonAssessAdapter(getActivity());
            pfCommonAssessAdapter.setBottomListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomShow();
                }
            });
            assessListView.setAdapter(pfCommonAssessAdapter);
            //初始化底部评论模块
            emot2 = new ViewEmot2(getActivity(), new SendMessage() {
                @Override
                public void send(String message) {
                    sendReply(message);
                }
            });

            bottom_assess.addView(emot2);
        }
        pfCommonAssessAdapter.setObjectList(assessObjectList);

        //指定显示高度
        int height = WindowUtils.dm.heightPixels / 5 * 3;
        CGLog.v("打印高度 : " + height);
        popAssessWindow = new PopupWindow(assessView, ViewGroup.LayoutParams.MATCH_PARENT, height);
        Utils.setPopWindow(popAssessWindow);
        popAssessWindow.showAsDropDown(textViews[1], Gravity.BOTTOM, 0, 0);
    }

    private void showPic() {
        if (sunObject != null) {
            String path = sunObject.getPath();
            if (!TextUtils.isEmpty(path)) {
                if (path.contains("@")) {
                    path = path.substring(0, path.indexOf("@"));
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
                    //将图片按比例放大显示
                    int height = (WindowUtils.dm.widthPixels / loadedImage.getWidth()) * loadedImage.getHeight();
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) pf_gallery_image.getLayoutParams();
                    params.height = height;
                    pf_gallery_image.setLayoutParams(params);
                    pf_gallery_image.setImageBitmap(loadedImage);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        }
    }

    private void queSingleAssess() {
        UserRequest.getPfSingleAssess(getActivity(), pageNo, GloablUtils.MODE_OF_PF, sunObject.getUuid(), maxTime,
                new RequestResultI() {
                    @Override
                    public void result(BaseModel domain) {
                        if(pf_single_scroll.isRefreshing()){
                            pf_single_scroll.onRefreshComplete();
                        }
                        PfSingleAssess pfSingleAssess = (PfSingleAssess) domain;
                        if (pfSingleAssess != null && pfSingleAssess.getList() != null
                                && pfSingleAssess.getList().getData() != null
                                && pfSingleAssess.getList().getData().size() > 0) {
                            assessObjectList.addAll(pfSingleAssess.getList().getData());
                        }else{
                            if(pageNo == 1){
                                addNothing();
                            }else {
                                ToastUtils.showMessage("没有更多内容了!");
                            }
                            pf_single_scroll.setMode(PullToRefreshBase.Mode.DISABLED);
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

    private void addNothing() {

    }

    private void addAssess(List<PfSingleAssessObject> data) {
        for(PfSingleAssessObject object : data){

            View assessView = View.inflate(getActivity(),R.layout.pf_info_aeesee_item,null);
            TextView tv_name = (TextView) assessView.findViewById(R.id.pf_info_name);
            TextView tv_content = (TextView) assessView.findViewById(R.id.pf_info_content);
            TextView tv_time = (TextView) assessView.findViewById(R.id.pf_info_time);
            CircleImage circleImage = (CircleImage) assessView.findViewById(R.id.pf_info_image);

            if(object != null){
                tv_name.setText(""+Utils.isNull(object.getCreate_user()));
                tv_content.setText(EmotUtil.getEmotionContent(getActivity(), Utils.isNull(object.getContent())));
                tv_time.setText(""+Utils.isNull(object.getCreate_time()));
                ImageLoaderUtil.displayImage(object.getCreate_img(),circleImage);
            }else{

            }
            pf_single_scroll.addView(assessView,0,null);
        }
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

    private void queryExtraData(String uuid) {
        UserRequest.getSinglePfExtraInfo(getActivity(), uuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                singleInfo = (SinlePfExtraInfo) domain;
                if (singleInfo != null) {
                    //初始化底部按钮显示状态
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
        if(singleInfo.getStatus() == 2){
            deleteData(sunObject);
            return;
        }
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

        mHanlder.sendEmptyMessage(UPDATE_ASSESS_COUNT);
    }

    private void initBottomBt() {
        bottom_assess = (LinearLayout) innerView.findViewById(R.id.pf_bottom_assess_linear);
        frame_bottom_tab = (FrameLayout) innerView.findViewById(R.id.bottom_tab);
        textViews = new TextView[]{
                (TextView) innerView.findViewById(R.id.pf_bottom_collect),
                (TextView) innerView.findViewById(R.id.pf_bottom_assess),
                (TextView) innerView.findViewById(R.id.pf_bottom_dianzan),
                (TextView) innerView.findViewById(R.id.pf_bottom_share),
                (TextView) innerView.findViewById(R.id.pf_bottom_more),
        };

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
                        showAssessList();
//                        beginAssess();
                        break;
                    case R.id.pf_bottom_share:
                        String note = sunObject.getNote();
                        ShareUtils.showShareDialog(getActivity(), textViews[3], note, note, sunObject.getPath(), sunObject.getPath(), false);
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
    public void cancleDianzan(){
        showDialog("取消点赞中...");
        UserRequest.commonCancleZan(getActivity(), sunObject.getUuid(), GloablUtils.MODE_OF_PF, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                cancleDialog();
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
        showDialog("点赞中...");
        UserRequest.commonZan(getActivity(), sunObject.getUuid(), GloablUtils.MODE_OF_PF, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                ToastUtils.showMessage("点赞成功");
                Utils.showDianzanStatus(getActivity(), textViews[2]);
                cancleDialog();
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
        if (popAssessWindow.isShowing()) {
            popAssessWindow.dismiss();
        }
        mHanlder.sendEmptyMessageDelayed(POP_DELAY_300, 300);
    }
    public void bottomCancle(){
        emot2.hideSoftKeyboard();
        bottom_assess.setVisibility(View.GONE);
        pf_pic_bottom_viewGroup.setVisibility(View.VISIBLE);
        showAssessList();
    }

    private void sendReply(final String message) {
        showDialog("评论回复中...");
        Utils.commonSendReply(getActivity(), sunObject.getUuid(), sunObject.getCreate_useruuid(), "", message, GloablUtils.MODE_OF_PF, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                cancleDialog();
                ToastUtils.showMessage("评论回复成功!");
                PfSingleAssessObject object = new PfSingleAssessObject();
                if (object == null) return;
                object.setContent(message);
                object.setCreate_user(sunObject.getCreate_user());
                object.setCreate_time(TimeUtil.getNowDate());
                object.setCreate_img(sunObject.getPath());
                assessObjectList.add(object);
                bottomCancle();
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
        showDialog("取消收藏中...");
        Utils.picCommonCancleStore(getActivity(), sunObject.getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                cancleDialog();
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
        showDialog("收藏中...");
        String title = sunObject.getNote();
        if(TextUtils.isEmpty(title)){
            title = "这是一张照片!";
        }
        Utils.picCommonStore(getActivity(),sunObject.getUuid(),new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                cancleDialog();
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

    public void showDialog(String text){
        if(TextUtils.isEmpty(text)){
            text = "加载数据中...请稍候";
        }
        dialog.show();
        dialog.setText(text);
    }
    public void cancleDialog(){
        if(dialog.isShowing()){
            dialog.cancel();
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
                deleteData(sunObject);
            }
        });
        pf_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                ((PfGalleryActivity)getActivity()).startEditActivity(sunObject);
            }
        });
        int [] location = Utils.getLocation(textViews[4]);
        pf_more_view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        CGLog.v("打印测量的高度 　：" + pf_more_view.getMeasuredHeight() + " 宽度 : " + pf_more_view.getMeasuredWidth());
        pf_more_view.getMeasuredHeight();
        popupWindow.showAtLocation(textViews[4], Gravity.NO_GRAVITY, location[0], location[1] - pf_more_view.getMeasuredHeight());
    }

    private void deleteData(AllPfAlbumSunObject sunObject) {
        //从数据库，网络，轮播图中删除
        pfSingleInfoFragment.deleteCurrentItem(sunObject);
    }

    public void showRightInfo(){
        View view = View.inflate(getActivity(),R.layout.pf_info_four_message,null);
        TextView pf_info_careme_time = (TextView) view.findViewById(R.id.pf_info_careme_time);
        TextView pf_info_location = (TextView) view.findViewById(R.id.pf_info_location);
        TextView pf_info_device = (TextView) view.findViewById(R.id.pf_info_device);
        TextView pf_info_upload_people = (TextView) view.findViewById(R.id.pf_info_upload_people);
            pf_info_careme_time.append("" + Utils.isNull(sunObject.getCreate_time()));
            pf_info_location.append("" + Utils.isNull(sunObject.getAddress()));
            pf_info_device.append("" + Utils.isNull(sunObject.getCreate_useruuid()));
            pf_info_upload_people.append("" + Utils.isNull(sunObject.getCreate_useruuid()));
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Utils.setPopWindow(popupWindow);
        popupWindow.showAsDropDown(normal_title_right_icon);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }
}

