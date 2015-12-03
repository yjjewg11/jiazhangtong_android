package com.wj.kindergarten.ui.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;


import com.umeng.onlineconfig.OnlineConfigAgent;
import com.umeng.onlineconfig.OnlineConfigLog;
import com.umeng.onlineconfig.UmengOnlineConfigureListener;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ActivityManger;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.ConfigObject;
import com.wj.kindergarten.bean.Login;
import com.wj.kindergarten.bean.MainTopic;
import com.wj.kindergarten.bean.TrainChildInfoList;
import com.wj.kindergarten.bean.TrainClass;

import com.wj.kindergarten.bean.VersionInfo;

import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.handler.GlobalHandler;
import com.wj.kindergarten.handler.MessageHandlerListener;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.AddressBookRequest;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.InteractionSentActivity;
import com.wj.kindergarten.ui.mine.LoginActivity;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.ShareUtils;
import com.wj.kindergarten.utils.Utils;


import org.json.JSONObject;

import java.util.List;


public class MainActivity extends BaseActivity {
    //Fragment界面数组
    private Class fragmentArray[] = {MainFragment.class, TeachersFragment.class, MessageFragment.class,
            MineFragment.class};
    //Tab选项卡图片
    private int mImageViewArray[] = {R.drawable.main_tab, R.drawable.contact_tab,
            R.drawable.message_tab_2, R.drawable.mine_tab};

    private int mImageViewArray2[] = {R.drawable.main_tab, R.drawable.contact_tab,
            R.drawable.message_tab, R.drawable.mine_tab};
    //Tab选项卡的文字
    private String mTabIdArray[] = {"首页", "通讯录", "消息", "我的"};
    private FragmentTabHost mTabHost;
    //message bottom tab item view
    private View[] mTabViews = new View[4];
    private String nowTab = mTabIdArray[0];
    private static final int BACK_QUIT = 2000;
    public static final int FIND_TO_MAP = 1;
    private long pre_back = 0;
    private boolean isClickMessage = false;
    private ImageView msgImageView = null;
    private List<TrainClass> TC_list ;
    private static TrainChildInfoList TCI;
    public static  MainActivity instance;
    private MainTopic topic;


    public TrainChildInfoList getTrainChildInfoList(){
        return TCI;
    }
    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_main;
    }

    @Override
    protected void setNeedLoading() {
    }


    @Override
    protected void onCreate() {
        instance = this;
        hideLeftButton();
        setTitleText("");
        MainFragment.GRID_ITEM_HW = Utils.getWidthByScreenWeight(4);
        ActivityManger.getInstance().addActivity(this);
        handler.sendEmptyMessageAtTime(1, 2000);
        initTab();
        listener();
        checkVersion();
        handler.sendEmptyMessageDelayed(2, 500);

        //获取系统参数
        if(CGSharedPreference.getEnoughOneDay()){
            getTopicConfig();
        }

        //每次应用启动获取话题
        getMainTopic();


    }

    private void getMainTopic() {
        UserRequest.getMainTopic(this, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                topic = (MainTopic) domain;
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
        UserRequest.getTopicConfig(this,CGSharedPreference.getConfigMD5(), new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                ConfigObject object = (ConfigObject) domain;
                if(object != null){
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

    private Handler  handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1: AddressBookRequest.getEmot(MainActivity.this); break;//获取表情列表
                case 2:
                    if (getIntent().hasExtra("from") && "Push".equals(getIntent().getStringExtra("from"))) {
                        if (!mTabIdArray[2].equals(nowTab)) {
                            mTabHost.setCurrentTabByTag(mTabIdArray[2]);
                        }
                    }
                    break;
                case 100:

                    //
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
                        messageFragment.loadMessage();
                    }
                } else if (msg.what == 1010) {
                    Utils.showToast(MainActivity.this, "登录超时，请重新登录。");
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else if (msg.what == 1011) {
                    if (mTabIdArray[0].equals(nowTab)) {
                        MainFragment mineFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(mTabIdArray[0]);
                        if (mineFragment != null) {
                            mineFragment.loadData();
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
        if (mTabIdArray[3].equals(nowTab)) {
            MineFragment mineFragment = (MineFragment) getSupportFragmentManager().findFragmentByTag(mTabIdArray[3]);
            mineFragment.addChildren();
        }
    }

    /**
<<<<<<< HEAD
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

        UmengUpdateAgent.update(this);
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

    /**
     * init tab
     */
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
                if (tabId.equals(mTabIdArray[2])) {
                    isClickMessage = true;
                    CGSharedPreference.setMessageState(true);
                } else {
                    if (isClickMessage) {
                        msgImageView.setImageResource(R.drawable.message_tab);
                    }
                }
            }
        });
        loadTab();
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
        }
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
    protected void titleCenterButtonListener() {
        super.titleCenterButtonListener();
        if (nowTab.equals(mTabIdArray[0])) {
            MainFragment mainFragment = (MainFragment) getSupportFragmentManager()
                    .findFragmentByTag(mTabIdArray[0]);
            if (mainFragment != null) {
                mainFragment.loadData();
                mainFragment.changeTitle();
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            quitApp();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void quitApp() {
        long now_back = System.currentTimeMillis();
        if (now_back - pre_back <= BACK_QUIT) {
            ShareUtils.clear();
            finish();
            return;
        } else {
            Utils.showToast(this, getString(R.string.quit_app));
        }
        pre_back = now_back;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
