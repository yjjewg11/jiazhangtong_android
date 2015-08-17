package com.wj.kindergarten.ui.main;

import android.content.Intent;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ActivityManger;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.InteractionFragment;
import com.wj.kindergarten.ui.func.InteractionSentActivity;
import com.wj.kindergarten.utils.Utils;


public class MainActivity extends BaseActivity {
    //Fragment界面数组
    private Class fragmentArray[] = {MainFragment.class, TeachersFragment.class, InteractionFragment.class,
            MineFragment.class};
    //Tab选项卡图片
    private int mImageViewArray[] = {R.drawable.main_tab, R.drawable.contact_tab,
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

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_main;
    }

    @Override
    protected void setNeedLoading() {
    }


    @Override
    protected void onCreate() {
        hideLeftButton();
        setTitleText("***幼儿园");

        if (getIntent().hasExtra("from") && "splash".equals(getIntent().getStringExtra("from"))) {

        }

        MainFragment.GRID_ITEM_HW = Utils.getWidthByScreenWeight(4);

        initTab();

        ActivityManger.getInstance().addActivity(this);
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
//                if (tabId.equals(mTabIdArray[2])) {
//                    MessageFragment messageFragment = (MessageFragment) getSupportFragmentManager()
//                            .findFragmentByTag(mTabIdArray[2]);
//                    if (messageFragment != null) {
//                        getNotReadMessage();
////                        messageFragment.showSystemTip(systemNotReadCount);
//                    }
//                }
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
        imageView.setImageResource(mImageViewArray[index]);
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
