package com.wj.kindergarten.ui.main;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;


import com.adsmogo.adview.AdsMogoLayout;
import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.videogo.openapi.EZOpenSDK;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ActivityManger;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.abstractbean.RequestFailedResult;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.ConfigObject;
import com.wj.kindergarten.bean.FoundTypeCount;
import com.wj.kindergarten.bean.ImUserInfo;
import com.wj.kindergarten.bean.ImUserInfoSun;
import com.wj.kindergarten.bean.Login;
import com.wj.kindergarten.bean.PfAlbumList;
import com.wj.kindergarten.bean.PfAlbumListSun;
import com.wj.kindergarten.bean.TrainChildInfoList;
import com.wj.kindergarten.bean.TrainClass;

import com.wj.kindergarten.bean.VideoAccessToken;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.handler.GlobalHandler;
import com.wj.kindergarten.handler.MessageHandlerListener;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.AddressBookRequest;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.services.PicUploadService;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.InteractionSentActivity;
import com.wj.kindergarten.ui.mine.LoginActivity;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.Constant.MessageConstant;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ShareUtils;
import com.wj.kindergarten.utils.Utils;
import com.xiaomi.market.sdk.XiaomiUpdateAgent;


import java.util.List;


public class MainActivity extends BaseActivity {
    private final int START_UPLOAD_PIC = 999;
    //Fragment界面数组
    private Class fragmentArray[] = {MainFragment.class, FoundFragment.class, MessageFragment.class,
            PhotoFamilyFragment.class, MineFragment.class};
    //Tab选项卡图片
    private int mImageViewArray[] = {R.drawable.school_tab, R.drawable.found_tab,
            R.drawable.message_tab_2, R.drawable.pf_album, R.drawable.mine_tab};

    private int mImageViewArray2[] = {R.drawable.school_tab, R.drawable.found_tab,
            R.drawable.message_tab, R.drawable.pf_album, R.drawable.mine_tab};
    private int[] typeCount = new int[3];
    private List<PfAlbumListSun> albumList;
    private UploadBroadCast receiver;
    private List<ImUserInfoSun> imUserInfoList;
    private YWIMKit mIMKit;
    private ImUserInfoSun singleInfo;

    public ImUserInfoSun getSingleInfo() {
        return singleInfo;
    }

    public YWIMKit getmIMKit() {
        return mIMKit;
    }

    public List<ImUserInfoSun> getImUserInfoList() {
        return imUserInfoList;
    }

    public List<PfAlbumListSun> getAlbumList() {
        return albumList;
    }

    public int[] getTypeCount() {
        return typeCount;
    }

    //Tab选项卡的文字
    private String mTabIdArray[] = {"学校", "发现", "消息", "家庭相册", "我的"};

    public String[] getmTabIdArray() {
        return mTabIdArray;
    }

    private FragmentTabHost mTabHost;
    //message bottom tab item view
    private View[] mTabViews = new View[5];
    private String nowTab = mTabIdArray[0];
    private static final int BACK_QUIT = 2000;
    public static final int FIND_TO_MAP = 1;
    private long pre_back = 0;
    private boolean isClickMessage = false;
    private ImageView msgImageView = null;
    private HintInfoDialog dialog;
    private List<TrainClass> TC_list;
    private static TrainChildInfoList TCI;
    public static MainActivity instance;


    public TrainChildInfoList getTrainChildInfoList() {
        return TCI;
    }

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_main;
    }

    @Override
    protected void setNeedLoading() {
    }

    public HintInfoDialog getDialog() {
        return dialog;
    }

    public void setText(String text) {
        titleCenterTextView.setText(text);
    }

    @Override
    protected void onCreate() {

        getSupportActionBar().setElevation(0);
        getCount();
        instance = this;
        hideLeftButton();
        dialog = new HintInfoDialog(this);
        setTitleText("");
        MainFragment.GRID_ITEM_HW = Utils.getWidthByScreenWeight(4);
        XiaomiUpdateAgent.update(this);
        ActivityManger.getInstance().addActivity(this);
        handler.sendEmptyMessageAtTime(1, 2000);
        initTab();
        listener();
        checkVersion();
        initPfAlbum();
        //启动服务上传图片

        registerUpload();


        //获取系统参数
//        if(CGSharedPreference.getEnoughOneDay()){
        getTopicConfig();
//        }
        //获取Im账号
        getImAccount();

        //获取视频的assessToken
        getVideoAccessToken();

        handler.sendEmptyMessageDelayed(START_UPLOAD_PIC, 1000);
        handler.sendEmptyMessageDelayed(2, 1000);
        //每次应用启动获取话题

        //注册广播
//        IntentFilter intentFilter = new IntentFilter(GloablUtils.MINE_ADD_CHILD_FINISH);
//        receive = new MyReceiver();
//        getActivity().registerReceiver(receive, intentFilter);

    }

    private void getVideoAccessToken() {
        UserRequest.getVideoAccessToken(this, new RequestFailedResult() {
            @Override
            public void result(BaseModel domain) {
                VideoAccessToken videoAccessToken = (VideoAccessToken) domain;
                if(videoAccessToken != null){
                    if(!Utils.stringIsNull(videoAccessToken.getAccessToken())){
                        CGSharedPreference.setVideoAccessToken(videoAccessToken.getAccessToken());
                    }
//                    if(!Utils.stringIsNull(videoAccessToken.getAppKey())){
//                        CGSharedPreference.setVideoAppkey(videoAccessToken.getAppKey());
////                        EZOpenSDK.initLib(CGApplication.getInstance(), videoAccessToken.getAppKey(), "");
//                    }

                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }
        });
    }

    private void getImAccount() {
        UserRequest.getImAccount(this, new RequestFailedResult(dialog) {
            @Override
            public void result(BaseModel domain) {
                ImUserInfo imUserInfo = (ImUserInfo) domain;
                if(imUserInfo != null && imUserInfo.getUserInfos() != null && imUserInfo.getUserInfos().size() > 0){
                    imUserInfoList = imUserInfo.getUserInfos();
                    singleInfo = imUserInfo.getUserInfos().get(0);
                    mIMKit = YWAPI.getIMKitInstance(singleInfo.getUserid(), GloablUtils.TABBAO_APPKEY);
                    imLogin(singleInfo.getUserid(),singleInfo.getPassword());
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

    private void imLogin(String userid, String password) {
        IYWLoginService loginService = mIMKit.getLoginService();
        YWLoginParam loginParam = YWLoginParam.createLoginParam(userid, password);
        loginService.login(loginParam, new IWxCallback() {

            @Override
            public void onSuccess(Object... arg0) {
                CGLog.v("打印测试登录 : "+arg0.toString());
            }

            @Override
            public void onProgress(int arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onError(int errCode, String description) {
                //如果登录失败，errCode为错误码,description是错误的具体描述信息
                CGLog.v("打印失败 : "+errCode+" 错误信息 : "+description);
            }
        });
    }

    private void registerUpload() {
        receiver = new UploadBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(GloablUtils.ALREADY_UPLOADING);
        filter.addAction(GloablUtils.ALREADY_UPLOADING_FINISHED);
        filter.addAction(GloablUtils.REQUEST_PIC_NEW_DATA);
        filter.addAction(GloablUtils.DELETE_PF_SINGLE_INFO_SUCCESSED);
        filter.addAction(GloablUtils.UPDATE_BOUTIQUE_ALBUM_SUCCESSED);
        filter.addAction(GloablUtils.ADD_NEW_CHILD);
        registerReceiver(receiver, filter);
    }

    private void initPfAlbum() {
        loadPfData();
    }

    //获取家庭相册集
    private void loadPfData() {
        UserRequest.getPfAlbumList(this, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                PfAlbumList pfAlbumList = (PfAlbumList) domain;
                if (pfAlbumList != null && pfAlbumList.getList() != null && pfAlbumList.getList().size() > 0) {
                    albumList = pfAlbumList.getList();
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


    private void getTopicConfig() {
        UserRequest.getTopicConfig(this, CGSharedPreference.getConfigMD5(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                ConfigObject object = (ConfigObject) domain;
                if (object != null) {
                    CGSharedPreference.setConfigMD5(object.getMd5(), object.getData().getSns_url());
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


    private void login() {
        if (getIntent().hasExtra("from") && "splash".equals(getIntent().getStringExtra("from"))) {
            if (CGApplication.getInstance().getLogin() == null) {
                showProgressDialog("登录中，请稍候...");
                setProgressDialogCancelable(false);
                UserRequest.login(mContext, CGSharedPreference.getLogin()[0], CGSharedPreference.getLogin()[1],
                        new RequestResultI() {
                            @Override
                            public void result(BaseModel domain) {
                                Login login = (Login) domain;
                                CGApplication.getInstance().setLogin(login);
                                hideProgressDialog();
                            }

                            @Override
                            public void result(List<BaseModel> domains, int total) {

                            }

                            @Override
                            public void failure(String message) {
                                hideProgressDialog();
                                finish();
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    AddressBookRequest.getEmot(MainActivity.this);
                    break;//获取表情列表
                case 2:
                    if (getIntent().hasExtra("from") && "Push".equals(getIntent().getStringExtra("from"))) {
                        if (!mTabIdArray[2].equals(nowTab)) {
                            mTabHost.setCurrentTabByTag(mTabIdArray[2]);
                        }
                    }
                    if (getIntent().hasExtra("tiaozhuan") && "now".equals(getIntent().getStringExtra("tiaozhuan"))) {
                        if (!mTabIdArray[3].equals(nowTab)) {
                            mTabHost.setCurrentTabByTag(mTabIdArray[3]);
                        }
                    }
                    break;
                case START_UPLOAD_PIC:
                    startService(new Intent(MainActivity.this, PicUploadService.class));
                    break;
            }

        }
    };

    private void listener() {
        GlobalHandler.getHandler().addMessageHandlerListener(new MessageHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    if (!mTabIdArray[2].equals(nowTab)) {
                        mTabHost.setCurrentTabByTag(mTabIdArray[2]);
                    }
                    MessageFragment messageFragment = (MessageFragment) getSupportFragmentManager()
                            .findFragmentByTag(mTabIdArray[2]);
                    if (messageFragment != null) {
//                        messageFragment.loadMessage();
                    }
                } else if (msg.what == 1010) {
                    Utils.showToast(MainActivity.this, "登录超时，请重新登录。");
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else if (msg.what == 1011) {
                    if (mTabIdArray[0].equals(nowTab)) {
                        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(mTabIdArray[0]);
                        if (mainFragment != null) {
                            mainFragment.loadData();
                        }
                    }
                } else if (msg.what == 1088) {
                    if (!mTabIdArray[2].equals(nowTab)) {
                        msgImageView.setImageResource(R.drawable.message_tab_2);
                        CGSharedPreference.setMessageState(false);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTabIdArray[4].equals(nowTab)) {
            MineFragment mineFragment = (MineFragment) getSupportFragmentManager().findFragmentByTag(mTabIdArray[4]);
//            mineFragment.addChildren();
        }


    }

    /**
     * <<<<<<< HEAD
     * 版本检测,设置统计在线参数
     */
    private void checkVersion() {

        UmengUpdateAgent.setDeltaUpdate(false);
        UmengUpdateAgent.setUpdateOnlyWifi(true);
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateCheckConfig(false);
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

            @Override
            public void onUpdateReturned(int updateStatus,
                                         UpdateResponse updateInfo) {
                if (updateStatus == 0 && updateInfo != null) {
                    showUpdateDialog(updateInfo.path, updateInfo.updateLog);
                } else if (updateStatus == 1) {
                    // Utils.showToast(MainActivity.this, "已经是最新版本");
                } else if (updateStatus == 2) {
                    //  Utils.showToast(MainActivity.this, "只能在Wifi环境下才可更新");
                } else if (updateStatus == 3) {
                    //  Utils.showToast(MainActivity.this, "检查更新超时");
                }

                CGLog.d("status: " + updateStatus);
            }
        });

        UmengUpdateAgent.update(getApplicationContext());
    }

    private void showUpdateDialog(final String downloadUrl, final String message) {
        CGLog.d("message: " + message);
        AlertDialog.Builder updateAlertDialog = new AlertDialog.Builder(this);
        updateAlertDialog.setIcon(R.drawable.ic_launcher);
        updateAlertDialog.setTitle(R.string.update_hint);
        updateAlertDialog.setMessage(message);
        updateAlertDialog.setNegativeButton(R.string.update_ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                    .parse(downloadUrl)));
                        } catch (Exception ex) {

                        }
                    }
                }).setPositiveButton(R.string.dialog_no, null);
        if (!isFinishing())
            updateAlertDialog.show();
    }


    public void getCount() {
        UserRequest.getTypeCount(this, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                //获取成功，
                FoundTypeCount foundTypeCount = (FoundTypeCount) domain;
                if (foundTypeCount != null && foundTypeCount.getData() != null)
                    typeCount[0] = foundTypeCount.getData().getToday_goodArticle();
                typeCount[1] = foundTypeCount.getData().getToday_snsTopic();
                typeCount[2] = foundTypeCount.getData().getToday_pxbenefit();

            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    /**
     * init tab
     */
    boolean isSecondOnce;

    private void initTab() {
        //实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost) findViewById(R.id.main_tab_host);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.main_tab_content);
        TabWidget tabWidget = mTabHost.getTabWidget();
        tabWidget.setDividerDrawable(null);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                nowTab = tabId;
                if (tabId.equals(mTabIdArray[3])) {
                    titleLeftImageView.setImageResource(R.drawable.xiajiatou);
                } else {
                    titleLeftImageView.setImageResource(0);
                }
                if (tabId.equals(mTabIdArray[2])) {
                    Utils.registerUmengClickEvent(MessageConstant.MINE_MSG);
                    isClickMessage = true;

                    CGSharedPreference.setMessageState(true);
                } else {
                    if (isClickMessage) {
                        msgImageView.setImageResource(R.drawable.message_tab);
                    }
                }


                if (mTabIdArray[4].equals(nowTab)) {
                    getSupportActionBar().hide();
                } else {
                    getSupportActionBar().show();
                }

                //如果是发现，判断是不是webFragment
                if (mTabIdArray[1].equals(nowTab)) {
                    Utils.registerUmengClickEvent(MessageConstant.FOUND);
                    FoundFragment foundFragment = (FoundFragment) getSupportFragmentManager().findFragmentByTag(mTabIdArray[1]);

                    if (foundFragment != null) {
                        getCount();
                        if (foundFragment.webIsShow()) {
                            getSupportActionBar().hide();
                        } else {
                            getSupportActionBar().show();
                        }
                    }
                }
            }
        });

        loadTab();
    }

    private void reagain(String tabId) {
        if (mTabIdArray[1].equals(tabId)) {
            //如果topicFragment还显示，就隐藏掉
            cancleMainWeb();
        }
    }

    public void cancleMainWeb() {
        FoundFragment fragment = (FoundFragment) getSupportFragmentManager().findFragmentByTag(mTabIdArray[1]);
        if (fragment.webIsShow()) {
            fragment.cancleWeb();
        }
    }

    public String[] getTabIds() {
        return mTabIdArray;
    }

    public void jump2Tab(String tabId) {
        jump2Tab(tabId, "");
    }

    public void jump2Tab(String tabId, String action) {
        mTabHost.setCurrentTabByTag(tabId);

        if (Utils.stringIsNull(action)) {
            return;
        }

        if ("voice".equals(action)) {
            titleLeftButtonListener();
        }
    }

    /**
     * load tab
     */
    private void loadTab() {
        //得到fragment的个数
        int count = fragmentArray.length;
        TabHost.TabSpec tabSpec;
        for (int i = 0; i < count; i++) {
            //为每一个Tab按钮设置图标、文字和内容
            tabSpec = mTabHost.newTabSpec(mTabIdArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            //给tabSpec添加监听事件
        }
        mTabHost.getTabWidget().getChildTabViewAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reagain(nowTab);
                mTabHost.setCurrentTab(1);
            }
        });
    }


    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.main_tab_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.main_tab_item_image);
        if (index == 2) {
            msgImageView = imageView;
        }
        if (!CGSharedPreference.getMessageState()) {
            imageView.setImageResource(mImageViewArray[index]);
        } else {
            imageView.setImageResource(mImageViewArray2[index]);
        }
//        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.main_tab_item_text);
        textView.setText(mTabIdArray[index]);
        mTabViews[index] = view;

        return view;
    }

    /**
     * change mine tab item icon
     *
     * @param count is show tip icon
     */
    public void showMessageTip(int count) {
        if (mTabViews[2] != null) {
            TextView imageView = (TextView) mTabViews[2].findViewById(R.id.main_tab_tip);
            imageView.setText(count + "");
            if (count > 0) {
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void titleRightButtonListener() {
        if (mTabIdArray[2].equals(nowTab)) {
            Intent intent = new Intent(mContext, InteractionSentActivity.class);
            mContext.startActivity(intent);
        }
    }

    @Override
    protected void titleRightImageListener() {
        if (mTabIdArray[3].equals(nowTab)) {
            ((PhotoFamilyFragment) getSupportFragmentManager().findFragmentByTag(mTabIdArray[3])).addRightListener();
        }
    }

    @Override
    protected void titleLeftButtonListener() {
        super.titleLeftButtonListener();
    }

    //屏蔽切换学校
//    @Override
//    protected void titleCenterButtonListener() {
//        super.titleCenterButtonListener();
//        if (nowTab.equals(mTabIdArray[0])) {
//            MainFragment mainFragment = (MainFragment) getSupportFragmentManager()
//                    .findFragmentByTag(mTabIdArray[0]);
//            if (mainFragment != null) {
//                mainFragment.loadData();
//                mainFragment.changeTitle();
//            }
//        }
//
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mTabHost.getCurrentTab() == 1 && keyCode == KeyEvent.KEYCODE_BACK) {
            return super.onKeyDown(keyCode, event);
        } else {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                quitApp();
                return true;
            }

        }

        return true;
    }

    public void quitApp() {
        long now_back = System.currentTimeMillis();
        if (now_back - pre_back <= BACK_QUIT) {
            ShareUtils.clear();
            sendBroadcast(new Intent(GloablUtils.FINISH_UPLOAD_PIC));
            ActivityManger.getInstance().exitAll();
            return;
        } else {
            Utils.showToast(this, getString(R.string.quit_app));
        }
        pre_back = now_back;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        instance = null;
        Log.i("TAG", "页面被销毁!");
        UmengUpdateAgent.setUpdateListener(null);
        AdsMogoLayout.clear();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        Log.i("TAG","添加运行任务 ： "+activityManager.getAppTasks().size());
        Log.i("TAG", "添加任务栈  ： " + activityManager.getRunningTasks(10).size());
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case GloablUtils.UPDATE_SUCCESSED_REFRESH:
                CGLog.v("打印currentTab : " + mTabHost.getCurrentTabTag() + " tab ： " + mTabHost.getCurrentTab());
                if (mTabHost.getCurrentTab() == 3 && getSupportFragmentManager().findFragmentByTag(mTabIdArray[3]) != null) {
                    PhotoFamilyFragment familyFragment = (PhotoFamilyFragment) getSupportFragmentManager().findFragmentByTag(mTabIdArray[3]);
                    familyFragment.initHeadBack();
                }
                break;
            case GloablUtils.DELETE_BOUTIQUE_ALBUM_SUCCESSED:
                if (mTabHost.getCurrentTab() == 3 && getSupportFragmentManager().findFragmentByTag(mTabIdArray[3]) != null) {
                    PhotoFamilyFragment familyFragment = (PhotoFamilyFragment) getSupportFragmentManager().findFragmentByTag(mTabIdArray[3]);
                    String uuid = data.getStringExtra("uuid");
                    if (!TextUtils.isEmpty(uuid)) {
                        familyFragment.updateBoutiqueFragmentData(uuid);
                    }
                }
                break;
            case GloablUtils.DELETE_FUSION_INFO_SUCCESSED:
                if (mTabHost.getCurrentTab() == 3 && getSupportFragmentManager().findFragmentByTag(mTabIdArray[3]) != null) {
                    PhotoFamilyFragment familyFragment = (PhotoFamilyFragment) getSupportFragmentManager().findFragmentByTag(mTabIdArray[3]);
                    familyFragment.refreshFusionData();
                }
                break;
            case GloablUtils.ADD_NEW_ALBUM_SUCCESSED:
                if (mTabHost.getCurrentTab() == 3 && getSupportFragmentManager().findFragmentByTag(mTabIdArray[3]) != null) {
                    PhotoFamilyFragment familyFragment = (PhotoFamilyFragment) getSupportFragmentManager().findFragmentByTag(mTabIdArray[3]);
                    familyFragment.reqetFamilyAlbum();
                }
                break;
            case GloablUtils.UPDATE_MY_INFO:
                if (mTabHost.getCurrentTab() == 4 && getSupportFragmentManager().findFragmentByTag(mTabIdArray[4]) != null) {
                    MineFragment mineFragment = (MineFragment) getSupportFragmentManager().findFragmentByTag(mTabIdArray[4]);
                    mineFragment.initHeadData();
                }
                break;
        }
    }

    class UploadBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(GloablUtils.ADD_NEW_CHILD)) {
                MineFragment mineFragment = (MineFragment) getSupportFragmentManager().findFragmentByTag(mTabIdArray[4]);
                if(mineFragment != null)
                mineFragment.initChildCount();
                return;
            }
            PhotoFamilyFragment photoFragment = (PhotoFamilyFragment) getSupportFragmentManager().findFragmentByTag(mTabIdArray[3]);
            if (photoFragment == null) return;
            switch (intent.getAction()) {
                case GloablUtils.ALREADY_UPLOADING:
                    photoFragment.startGif();
                    break;
                case GloablUtils.ALREADY_UPLOADING_FINISHED:
                    photoFragment.stopGif();
                    break;
                case GloablUtils.REQUEST_PIC_NEW_DATA:
                    photoFragment.requestNewData();
                    break;
                case GloablUtils.DELETE_PF_SINGLE_INFO_SUCCESSED:
                    photoFragment.refreshUpdateData();
                    break;
                case GloablUtils.UPDATE_BOUTIQUE_ALBUM_SUCCESSED:
                    photoFragment.reBoutiqueData();
                    break;
            }
        }
    }

    public void setCurrentTab(int position) {
        mTabHost.setCurrentTab(position);
    }
}
