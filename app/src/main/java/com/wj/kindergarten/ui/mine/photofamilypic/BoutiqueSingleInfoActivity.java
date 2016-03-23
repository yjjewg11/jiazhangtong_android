package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.nineoldandroids.animation.ObjectAnimator;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.BoutiqueAllpic;
import com.wj.kindergarten.bean.BoutiqueSingleInfo;
import com.wj.kindergarten.bean.BoutiqueSingleInfoObject;
import com.wj.kindergarten.bean.PfDianZan;
import com.wj.kindergarten.bean.PfSingleAssess;
import com.wj.kindergarten.bean.PfSingleAssessObject;
import com.wj.kindergarten.bean.UserInfo;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.emot.SendMessage;
import com.wj.kindergarten.ui.emot.ViewEmot2;
import com.wj.kindergarten.ui.func.adapter.PfCommonAssessAdapter;
import com.wj.kindergarten.ui.more.PfRefreshLinearLayout;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ShareUtils;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;
import com.wj.kindergarten.utils.WindowUtils;
import com.wj.kindergarten.wrapper.WrapperFl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BoutiqueSingleInfoActivity extends BaseActivity {

    private final int POP_DELAY_300 = 203;
    private final int UPDATE_ASSESS_COUNT = 202;
    private TextView[] textViews;
    private PullToRefreshWebView boutiqueWebView;
    String uuid;
    private BoutiqueSingleInfo boutiqueSingleInfo;
    private final int GET_DATA_SUCCESSED = 201;
    private BoutiqueSingleInfoObject infoObject;
    private List<PfSingleAssessObject> assessObjectList = new ArrayList<>();
    private Handler mHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_DATA_SUCCESSED:
                    initHttpData();
                    break;
                case UPDATE_ASSESS_COUNT:
                    int count = boutiqueSingleInfo.getReply_count();
                    if (count == 0) {
                        pf_pic_bottom_assess_count.setVisibility(View.GONE);
                    } else {
                        pf_pic_bottom_assess_count.setVisibility(View.VISIBLE);
                    }
                    pf_pic_bottom_assess_count.setText("" + count);
                    break;
                case POP_DELAY_300:
                    emot2.showSoftKeyboard();
                    boutique_single_info_assess.setVisibility(View.VISIBLE);
                    pf_pic_bottom_viewGroup.setVisibility(View.GONE);
                    break;
            }
        }
    };
    private LinearLayout boutique_single_info_assess;
    private ViewEmot2 emot2;
    private View pf_more_view;
    private ImageView pf_delete;
    private ImageView pf_edit;
    private ArrayList<AllPfAlbumSunObject> objectList = new ArrayList<>();
    private View assessView;
    private PullToRefreshListView assessListView;
    private String maxTime;
    private PfCommonAssessAdapter pfCommonAssessAdapter;
    private TextView pf_pic_bottom_assess_count;
    private PfSingleAssess pfSingleAssess;
    private LinearLayout pf_pic_bottom_viewGroup;
    private TextView pf_common_show_assess_title;
    private PopupWindow popAssessWindow;
    private FrameLayout boutqiue_single_info_top;
    private FrameLayout boutqiue_single_info_bottom;
    private ObjectAnimator animTop;
    private ObjectAnimator animBottom;
    private LinearLayout pf_common_show_asseess_send;
    //在主页面底部加入输入框后，在弹出框弹出时，会将它隐藏，所以添加到popwindow中
//    private LinearLayout pf_comon_show_assess_linear;

    private void initHttpData() {
        setTitleText("" + boutiqueSingleInfo.getData().getTitle());
        String path = "http://www.wenjienet.com/px-cc/index.html?t="+TimeUtil.getNowDate();
        boutiqueWebView.getRefreshableView().loadUrl(
//                path
                boutiqueSingleInfo.getShare_url()
        );
        if (boutiqueSingleInfo.isFavor()) {
            Utils.cancleStoreStatus(this, textViews[0]);
        } else {
            Utils.showStoreStatus(this, textViews[0]);
        }

        PfDianZan dianZan = boutiqueSingleInfo.getDianZan();
        if (dianZan != null) {
            //可以点赞
            if (dianZan.getYidianzan() == 0) {
                Utils.cancleDizanStatus(this, textViews[2]);
            } else {
                Utils.showDianzanStatus(this, textViews[2]);
            }
        }
    }

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_boutique_single_info;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        initData();
        initViews();
        initBottomView();
        initBottomClick();
        getBoutiqueInfo();
        queryAllpicClassBoutique();
        queryAssess();
        initAnim();
    }

    private void initAnim() {
        boutqiue_single_info_top.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        boutqiue_single_info_bottom.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int topHeight = boutqiue_single_info_top.getMeasuredHeight();
        int bottomHeight = boutqiue_single_info_bottom.getMeasuredHeight();
        CGLog.v("打印高度 : "+topHeight+" 宽度 : "+bottomHeight);
        animTop = ObjectAnimator.ofInt(new TopWrapper(boutqiue_single_info_top), "topMargin", -topHeight);
        animBottom = ObjectAnimator.ofInt(new BottomWrapper(boutqiue_single_info_bottom),"bottomMargin",-bottomHeight);
        animTop.setDuration(300);
        animBottom.setDuration(300);
        animTop.setInterpolator(new LinearInterpolator());
        animBottom.setInterpolator(new LinearInterpolator());
    }

    boolean isVisible = true;
    class BottomWrapper{
        private int bottomMargin;
        public BottomWrapper(FrameLayout frameLayout) {
            this.frameLayout = frameLayout;
        }
        FrameLayout frameLayout;

        public int getBottomMargin() {
            return ((FrameLayout.LayoutParams)frameLayout.getLayoutParams()).bottomMargin;
        }

        public void setBottomMargin(int bottomMargin) {
            ((FrameLayout.LayoutParams)frameLayout.getLayoutParams()).bottomMargin = bottomMargin;
            frameLayout.requestLayout();
        }
    }

    class TopWrapper{
        private int topMargin;
        public TopWrapper(FrameLayout frameLayout) {
            this.frameLayout = frameLayout;
        }
        FrameLayout frameLayout;

        public int getTopMargin() {
            return ((FrameLayout.LayoutParams)frameLayout.getLayoutParams()).topMargin;
        }

        public void setTopMargin(int topMargin) {
            ((FrameLayout.LayoutParams)frameLayout.getLayoutParams()).topMargin = topMargin;
            frameLayout.requestLayout();
        }
    }

    private void fullBoutiqueScreen(){
        mHanlder.post(new Runnable() {
            @Override
            public void run() {
                try {
                    animTop.start();
                    animBottom.start();
                    isVisible = false;
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void exitBoutiqueScreen(){
        mHanlder.post(new Runnable() {
            @Override
            public void run() {
                animBottom.reverse();
                animTop.reverse();
                isVisible = true;
            }
        });
    }

    public void showDialog() {
        commonDialog.show();
    }

    public void cancleDialog() {
        if (commonDialog.isShowing()) {
            commonDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        boutiqueWebView.getRefreshableView().destroy();
        super.onDestroy();
    }

    int pageNo = 1;

    private void queryAssess() {
        showDialog();
        UserRequest.getPfSingleAssess(this, pageNo, GloablUtils.BOUTIQUE_COMMON_TYPE, uuid, maxTime,
                new RequestResultI() {
                    @Override
                    public void result(BaseModel domain) {
                        cancleDialog();
                        if(assessListView.isRefreshing())assessListView.onRefreshComplete();
                        pfSingleAssess = (PfSingleAssess) domain;
                        if (pfSingleAssess != null && pfSingleAssess.getList() != null
                                && pfSingleAssess.getList().getData() != null
                                && pfSingleAssess.getList().getData().size() > 0) {
                            assessObjectList.addAll(pfSingleAssess.getList().getData());
                        } else {
                            if (pageNo == 1) {

                            } else {
                                assessListView.setMode(PullToRefreshBase.Mode.DISABLED);
                                ToastUtils.showMessage("没有更多数据了!");
                            }
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

    private void queryAllpicClassBoutique() {
        showDialog();
        UserRequest.getAllPicFromBoutiqueAlbum(this, uuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                cancleDialog();
                BoutiqueAllpic boutiqueAllpic = (BoutiqueAllpic) domain;
                if (boutiqueAllpic != null && boutiqueAllpic.getList() != null) {
                    objectList.addAll(boutiqueAllpic.getList());
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

    private void initBottomClick() {
        for (TextView textView : textViews) {
            textView.setOnClickListener(listener);
        }
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pf_bottom_collect:
                    String text = textViews[0].getText().toString();
                    if (text.equals("收藏")) {
                        //进行收藏
                        store();
                    } else {
                        //取消收藏
                        cacleStore();
                    }
                    break;
                case R.id.pf_bottom_assess:
                    //显示弹出框的评论
                    showAssessList();
//                    beginAssess();
                    break;
                case R.id.pf_bottom_share:
                    String note = boutiqueSingleInfo.getData().getTitle();
                    ShareUtils.showShareDialog(BoutiqueSingleInfoActivity.this, textViews[3], note, note, boutiqueSingleInfo.getData().getHerald(), boutiqueSingleInfo.getShare_url(), false);
                    break;
                case R.id.pf_bottom_dianzan:
                    String dianzanText = textViews[2].getText().toString();
                    if (dianzanText.equals("点赞")) {
                        dianzan();
                    } else {
                        cancleDianzan();
                    }
                    break;
                case R.id.pf_bottom_more:
                    showMore();
                    break;
            }
        }
    };

    private void showAssessList() {
        if (assessView == null) {
            assessView = View.inflate(this, R.layout.pf_common_show_assess_layout, null);
            pf_common_show_asseess_send = (LinearLayout)assessView.findViewById(R.id.pf_common_show_asseess_send);
            pf_common_show_asseess_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomShow();
                }
            });
            assessListView = (PullToRefreshListView) assessView.findViewById(R.id.pulltorefresh_list);
            assessListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    pageNo++;
                    queryAssess();
                }
            });
            assessListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

            pf_common_show_assess_title = (TextView) assessView.findViewById(R.id.pf_common_show_assess_title);

            pf_common_show_assess_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popAssessWindow.dismiss();
                }
            });
            pfCommonAssessAdapter = new PfCommonAssessAdapter(this);
            pfCommonAssessAdapter.setDeleteDataListener(new PfCommonAssessAdapter.DeleteAssessItemListener() {
                @Override
                public void deleteData(PfSingleAssessObject object) {
                    assessObjectList.remove(object);
                    initBottomAssessCount();
                }
            });
            assessListView.setAdapter(pfCommonAssessAdapter);
            emot2 = new ViewEmot2(this, new SendMessage() {
                @Override
                public void send(String message) {
                    sendReply(message);
                }
            });
            boutique_single_info_assess.addView(emot2);
        }
        pfCommonAssessAdapter.setObjectList(assessObjectList);

        //指定显示高度
        int height = WindowUtils.dm.heightPixels / 2;
        CGLog.v("打印高度 : " + height);
        popAssessWindow = new PopupWindow(assessView, ViewGroup.LayoutParams.MATCH_PARENT, height);
        Utils.setPopWindow(popAssessWindow);
        popAssessWindow.showAsDropDown(pf_common_show_asseess_send);
    }
    int firstItem;
    int visibleItem;
    int totalItem;
    private void showMore() {
        if (pf_more_view == null) {
            pf_more_view = View.inflate(this, R.layout.pf_single_more, null);
            pf_delete = (ImageView) pf_more_view.findViewById(R.id.pf_single_delete);
            pf_edit = (ImageView) pf_more_view.findViewById(R.id.pf_single_edit);
        }
        final PopupWindow popupWindow = new PopupWindow(pf_more_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Utils.setPopWindow(popupWindow,R.style.ShareAnimTopAndBottom);
        pf_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                ToastUtils.showDialog(BoutiqueSingleInfoActivity.this, "提示", "您确定要删除吗？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteData(boutiqueSingleInfo.getData().getUuid());
                    }
                });
            }
        });
        pf_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(BoutiqueSingleInfoActivity.this, PfChoosedPicActivity.class);
                intent.putExtra("object", boutiqueSingleInfo.getData());
                intent.putExtra("objectList", objectList);
                intent.putExtra("edit", true);
                startActivity(intent);
                finish();
            }
        });
        int[] location = Utils.getLocation(textViews[4]);
        pf_more_view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pf_more_view.getMeasuredHeight();
        popupWindow.showAtLocation(textViews[4], Gravity.NO_GRAVITY, location[0], location[1] - pf_more_view.getMeasuredHeight());
    }

    private void deleteData(final String uuid) {
        UserRequest.deleteBoutiqueSingle(this, uuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                ToastUtils.showMessage("删除成功!");
                Intent intent = new Intent();
                intent.putExtra("uuid", uuid);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    private void cancleDianzan() {
        commonDialog.show();
        commonDialog.setText("取消点赞中...请稍候!");
        UserRequest.commonCancleZan(this, boutiqueSingleInfo.getData().getUuid(), GloablUtils.BOUTIQUE_COMMON_TYPE, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                cancleDialog();
                Utils.cancleDizanStatus(BoutiqueSingleInfoActivity.this, textViews[2]);
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
        commonDialog.show();
        commonDialog.setText("点赞中...");
        UserRequest.commonZan(this, boutiqueSingleInfo.getData().getUuid(), GloablUtils.BOUTIQUE_COMMON_TYPE, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                ToastUtils.showMessage("点赞成功");
                Utils.showDianzanStatus(BoutiqueSingleInfoActivity.this, textViews[2]);
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

    public void bottomShow() {
        if (popAssessWindow.isShowing()) {
            popAssessWindow.dismiss();
        }
        mHanlder.sendEmptyMessageDelayed(POP_DELAY_300, 300);
    }

    public void bottomCancle() {
        emot2.hideSoftKeyboard();
        boutique_single_info_assess.setVisibility(View.GONE);
        pf_pic_bottom_viewGroup.setVisibility(View.VISIBLE);
        showAssessList();
    }

    private void cacleStore() {
        commonDialog.show();
        commonDialog.setText("取消收藏中...");
        Utils.commonCancleStore(this, boutiqueSingleInfo.getData().getUuid(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                if (commonDialog.isShowing()) {
                    commonDialog.dismiss();
                    Utils.cancleStoreStatus(BoutiqueSingleInfoActivity.this, textViews[0]);
                    ToastUtils.showMessage("取消收藏成功!");
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

    private void store() {
        commonDialog.show();
        commonDialog.setText("收藏中...");
        Utils.commonStore(this, boutiqueSingleInfo.getData().getTitle(), GloablUtils.BOUTIQUE_COMMON_TYPE,
                boutiqueSingleInfo.getData().getUuid(), "", new RequestResultI() {
                    @Override
                    public void result(BaseModel domain) {
                        if (commonDialog.isShowing()) {
                            commonDialog.dismiss();
                            Utils.showStoreStatus(BoutiqueSingleInfoActivity.this, textViews[0]);
                            ToastUtils.showMessage("收藏成功!");
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

    private void getBoutiqueInfo() {
        UserRequest.getBoutiqueSingleInfo(this, uuid, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                boutiqueSingleInfo = (BoutiqueSingleInfo) domain;
                if (boutiqueSingleInfo != null) {
                    infoObject = boutiqueSingleInfo.getData();
                    mHanlder.sendEmptyMessage(GET_DATA_SUCCESSED);
                    mHanlder.sendEmptyMessage(UPDATE_ASSESS_COUNT);
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

    private void initData() {
        uuid = getIntent().getStringExtra("uuid");
        maxTime = TimeUtil.getStringDate(new Date());
    }

    private void initViews() {
        boutqiue_single_info_top = (FrameLayout) findViewById(R.id.boutqiue_single_info_top);
        boutqiue_single_info_bottom = (FrameLayout) findViewById(R.id.boutqiue_single_info_bottom);
        pf_pic_bottom_assess_count = (TextView) findViewById(R.id.pf_pic_bottom_assess_count);
        boutiqueWebView = (PullToRefreshWebView) findViewById(R.id.boutique_single_info_webview);
        boutiqueWebView.setMode(PullToRefreshBase.Mode.DISABLED);
        setWebView(boutiqueWebView.getRefreshableView(),new WebJavaScript(boutiqueWebView.getRefreshableView()){

//            进入全屏播放
            @JavascriptInterface
             public void fullScreen(){
                fullBoutiqueScreen();
            }
//            退出全屏播放
            @JavascriptInterface
            public void exitFullScreen(){
                exitBoutiqueScreen();
            }

            @JavascriptInterface
            public void makeFPMovie(){
                startGalleryActivity();
                finish();
            }


        });
        boutique_single_info_assess = (LinearLayout) findViewById(R.id.boutique_single_info_assess);
        boutiqueWebView.getRefreshableView().setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showDialog();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                cancleDialog();
            }
        });
    }


    private void startGalleryActivity(){
        Intent intent = new Intent(this, BoutiqueGalleryActivity.class);
        startActivity(intent);
    }

    private void sendReply(final String message) {
        commonDialog.show();
        Utils.commonSendReply(this, boutiqueSingleInfo.getData().getUuid(), "", "", message, GloablUtils.BOUTIQUE_COMMON_TYPE, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                if (commonDialog.isShowing()) {
                    commonDialog.dismiss();
                    ToastUtils.showMessage("评论回复成功!");
                    bottomCancle();
                    PfSingleAssessObject object = new PfSingleAssessObject();
                    UserInfo userInfo = CGApplication.getInstance().getLogin().getUserinfo();
                    object.setContent(message);
                    object.setCreate_user(userInfo.getName());
                    object.setCreate_img(userInfo.getImg());
                    object.setCreate_useruuid(boutiqueSingleInfo.getData().getUuid());
                    object.setCreate_time(TimeUtil.getNowDate());
                    assessObjectList.add(0, object);
                    pfCommonAssessAdapter.setObjectList(assessObjectList);
                    initBottomAssessCount();
                    emot2.cleanEditText();
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

    private void initBottomAssessCount() {
        if(assessObjectList.size() <= 0){
            pf_pic_bottom_assess_count.setVisibility(View.GONE);
        }else {
            pf_pic_bottom_assess_count.setVisibility(View.VISIBLE);
            pf_pic_bottom_assess_count.setText(""+assessObjectList.size());
        }
    }

    private void initBottomView() {
        textViews = new TextView[]{
                (TextView) findViewById(R.id.pf_bottom_collect),
                (TextView) findViewById(R.id.pf_bottom_assess),
                (TextView) findViewById(R.id.pf_bottom_dianzan),
                (TextView) findViewById(R.id.pf_bottom_share),
                (TextView) findViewById(R.id.pf_bottom_more),
        };
        pf_pic_bottom_viewGroup = (LinearLayout) findViewById(R.id.pf_pic_bottom_viewGroup);
    }

    @Override
    protected void onPause() {
        super.onPause();
        boutiqueWebView.getRefreshableView().onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        boutiqueWebView.getRefreshableView().onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(!isVisible){
                exitBoutiqueScreen();
            }else {
                CGLog.v("打印bottom : "+boutqiue_single_info_bottom.getVisibility()+
                "打印assess : "+boutique_single_info_assess.getVisibility());
                if(boutique_single_info_assess.getVisibility() == View.VISIBLE &&
                        pf_pic_bottom_viewGroup.getVisibility() == View.GONE){
                    boutique_single_info_assess.setVisibility(View.GONE);
                    pf_pic_bottom_viewGroup.setVisibility(View.VISIBLE);
                    return true;
                }else {
                    finish();
                }
            }
        }
        return false;
    }
}
