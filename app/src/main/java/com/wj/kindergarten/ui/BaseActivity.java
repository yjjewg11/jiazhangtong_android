package com.wj.kindergarten.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.compounets.NormalProgressDialog;
import com.wj.kindergarten.ui.func.adapter.SpinnerAreaAdapter;
import com.wj.kindergarten.ui.mine.LoginActivity;
import com.wj.kindergarten.ui.webview.WebviewActivity;
import com.wj.kindergarten.utils.Utils;


/**
 * BaseActivity
 *
 * @author Wave
 * @data: 2015/5/20
 * @version: v1.0
 */
public abstract class BaseActivity extends ActionBarActivity {
    //标题栏左边点击控件
    protected LinearLayout titleLeftButton = null;
    //标题栏左边图标
    protected ImageView titleLeftImageView = null;
    //标题栏中间标题
    protected TextView titleCenterTextView = null;
    //标题栏右边点击控件
//    protected RelativeLayout titleRightButton = null;
    //标题栏右边图标
    protected ImageView titleRightImageView = null;
    //标题栏右边文本
    protected TextView titleRightTextView = null;
    private View titleLine = null;
    //instance of this activity
    protected Context mContext = null;
    private ActionBar actionBar = null;

    public final static int TITLE_CENTER_TYPE_LEFT = 0;
    public final static int TITLE_CENTER_TYPE_RIGHT = 1;

    //activity layout
    protected int layoutId = 0;
    //是否显示加载界面
    protected boolean isNeedLoading = false;
    private NormalProgressDialog progressDialog = null;//加载对话框

    private boolean isReload = false;
    private TextView loadText;
    private ProgressBar progressBar;
    private ImageView ivReload;
    private RelativeLayout layoutReload;
    private RelativeLayout titleRightButton;

    /**
     * set content view id ,it must be: layout = the layout id;such as,layout = R.layout.activity_main;
     */
    protected abstract void setContentLayout();

    /**
     * 设置是否需要显示加载界面 ,如果要显示加载界面必须包含如下：isNeedLoading = true;true则显示，false不显示
     */
    protected abstract void setNeedLoading();

    /**
     *
     *
     */
    protected abstract void onCreate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentLayout();
        setNeedLoading();

        if (isNeedLoading) {
            setContentView(R.layout.normal_loading_data_layout);
            setActionBarLayout();
            initLoadView();
            loadData();
        } else {
            setContentView(layoutId);
            setActionBarLayout();
            onCreate();
        }

        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (!getClass().getSimpleName().equals("LoginActivity")
                && !getClass().getSimpleName().equals("MainActivity")
                && !getClass().getSimpleName().equals("RegisterActivity")) {
            if (CGApplication.getInstance().getLogin() == null) {
                hideProgressDialog();
                Utils.showToast(mContext, "登录超时，请重新登录");
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initLoadView() {
        loadText = (TextView) findViewById(R.id.loading_text);
        progressBar = (ProgressBar) findViewById(R.id.loading_bar);
        ivReload = (ImageView) findViewById(R.id.iv_reload);
        layoutReload = (RelativeLayout) findViewById(R.id.layout_reload);
    }

    /**
     * 如果isNeedLoading=true；必须重写此方法用以加载数据
     */
    protected void loadData() {
    }

    /**
     * 如果isNeedLoading=true，加载数据完成后调用此方法完成界面刷新
     */
    protected void loadSuc() {
        setContentView(layoutId);
        setActionBarLayout();
        onCreate();
    }

    /**
     * 请求数据为空
     */
    protected void loadEmpty() {
        if (null != progressBar) {
            progressBar.setVisibility(View.GONE);
        }
        ivReload.setVisibility(View.VISIBLE);
        loadText.setText(getString(R.string.loading_empty));
        loadText.setGravity(Gravity.CENTER);
    }

    /**
     * 加载数据失败
     */
    protected void loadFailed() {
        isReload = false;
        progressBar.setVisibility(View.GONE);
        ivReload.setVisibility(View.VISIBLE);
        loadText.setText(getString(R.string.loading_failed));
        layoutReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isReload) {
                    isReload = true;
                    reload();
                }
            }
        });
    }

    protected void reload() {
        ivReload.setVisibility(View.GONE);
        loadText.setText(getResources().getString(R.string.loading_content));
        progressBar.setVisibility(View.VISIBLE);
        loadData();
    }

    /**
     * actionbar is showing
     *
     * @return action bar is showing
     */
    public boolean isActionbarShowing() {
        return actionBar.isShowing() ? true : false;
    }

    /**
     * hide actionbar
     */
    public void hideActionbar() {
        actionBar.hide();
    }

    /**
     * show actionbar
     */
    public void showActionbar() {
        actionBar.show();
    }

    /**
     * set custom actionbar
     */
    public void setActionBarLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        if(toolbar!=null) {
            setSupportActionBar(toolbar);
            actionBar = getSupportActionBar();
            initTitle();
            actionBar.show();
        }
    }

    /**
     * init actionbar widgets
     */
    private void initTitle() {
        titleLeftButton = (LinearLayout) findViewById(R.id.normal_title_left_layout);
        titleLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftButtonListener();
            }
        });

        titleLeftImageView = (ImageView) findViewById(R.id.normal_title_left_image);
        titleCenterTextView = (TextView) findViewById(R.id.normal_title_center_text);
        titleRightButton = (RelativeLayout) findViewById(R.id.normal_title_right_layout);
        titleRightTextView = (TextView) findViewById(R.id.normal_title_right_text);
        titleRightImageView = (ImageView) findViewById(R.id.normal_title_right_icon);
        titleLine = findViewById(R.id.normal_title_line);
    }

    protected void showTitleLine() {
        titleLine.setVisibility(View.VISIBLE);
    }

    protected void hideTitleLine() {
        titleLine.setVisibility(View.GONE);
    }

    /**
     * hide title left back button
     */
    public void hideLeftButton() {
        titleLeftButton.setVisibility(View.GONE);
    }

    /**
     * show title left back button
     */
    public void showLeftButton() {
        titleLeftButton.setVisibility(View.VISIBLE);
    }

    /**
     * show title left back button
     */
    public void showLeftButton(int iconId) {
        if (iconId != 0) {
            titleLeftImageView.setImageResource(iconId);
        }
        titleLeftButton.setVisibility(View.VISIBLE);
    }

    public void showLeftButton(int iconId, ImageView.ScaleType scaleType) {
        if (iconId != 0) {
            titleLeftImageView.setImageResource(iconId);
        }
        titleLeftImageView.setScaleType(scaleType);
        titleLeftButton.setVisibility(View.VISIBLE);
    }

    /**
     * show title center imageView and bind listener
     */
    public void showCenterIcon(int type, int iconResource) {
        Drawable drawable = getResources().getDrawable(iconResource);
        if (type == TITLE_CENTER_TYPE_LEFT) {//left
            titleCenterTextView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        } else if (type == TITLE_CENTER_TYPE_RIGHT) {//right
            titleCenterTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }
        titleCenterTextView.setCompoundDrawablePadding(10);
        titleCenterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleCenterButtonListener();
            }
        });
    }

    /**
     * show title center imageView and bind listener
     */
    public void showCenterIcon(int type, Drawable drawable) {
        if (type == TITLE_CENTER_TYPE_LEFT) {//left
            titleCenterTextView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        } else if (type == TITLE_CENTER_TYPE_RIGHT) {//right
            titleCenterTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }
    }

    /**
     * set activity's title
     */
    public void clearCenterIcon() {
        titleCenterTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        titleCenterTextView.setCompoundDrawablePadding(1);
    }

    /**
     * set activity's title
     *
     * @param titleText the title
     */
    public void setTitleText(String titleText) {
        setTitleText(titleText, "");
    }

    /**
     * set activity's title
     *
     * @param titleText the title
     * @param rightText the title right action name
     */
    public void setTitleText(String titleText, String rightText) {
        if (Utils.stringIsNull(titleText)) {
            titleCenterTextView.setText(getString(R.string.app_name));
        } else {
            titleCenterTextView.setText(titleText);
        }

        if (!Utils.stringIsNull(rightText)) {
            titleRightTextView.setVisibility(View.VISIBLE);
            titleRightTextView.setText(rightText);
            titleRightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titleRightButtonListener();
                }
            });
        } else {
            titleRightTextView.setVisibility(View.GONE);
        }
    }

    /**
     * set activity's title
     *
     * @param titleText the title
     * @param rightImg  the title right action image
     */
    public void setTitleText(String titleText, int rightImg) {
        if (Utils.stringIsNull(titleText)) {
            titleCenterTextView.setText(getString(R.string.app_name));
        } else {
            titleCenterTextView.setText(titleText);
        }
        titleLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftButtonListener();
            }
        });

        if (rightImg != 0) {
            Drawable drawable = getResources().getDrawable(rightImg);
            titleRightTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            titleRightTextView.setCompoundDrawablePadding(1);
            titleRightTextView.setVisibility(View.VISIBLE);
            titleRightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titleRightButtonListener();
                }
            });
        } else {
            titleRightTextView.setVisibility(View.GONE);
        }
    }

    public void setTitleRightImage(int rightImage1, int rightImage2) {
        if (rightImage1 != 0) {
            titleRightImageView.setVisibility(View.VISIBLE);
            titleRightImageView.setImageResource(rightImage1);
            titleRightImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titleRightImageListener();
                }
            });
        }

        if (rightImage2 != 0) {
            Drawable drawable = getResources().getDrawable(rightImage2);
            titleRightTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            titleRightTextView.setCompoundDrawablePadding(1);
            titleRightTextView.setVisibility(View.VISIBLE);
            titleRightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    titleRightButtonListener();
                }
            });
        }
    }

    /**
     * 显示菊花对话框
     */
    public void showProgressDialog() {
        showProgressDialog(getString(R.string.loading_content));
    }

    /**
     * 显示菊花对话框
     *
     * @param info 提示内容
     */
    public void showProgressDialog(String info) {
        if (progressDialog == null) {
            progressDialog = new NormalProgressDialog(this, info);
            progressDialog.show();
        } else {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
            progressDialog.setInfo(info);
        }
    }

    public void setProgressDialogCancelable(boolean isCancel) {
        if (progressDialog != null) {
            progressDialog.setCancelable(isCancel);
        }
    }

    /**
     * 隐藏菊花对话框
     */
    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public boolean isProgressDialogIsShowing() {
        return progressDialog != null && progressDialog.isShowing();
    }

    /**
     * set left button's click listener to finish this activity
     */
    protected void titleLeftButtonListener() {
        finish();
    }

    /**
     * set right button's click listener to finish this activity
     */
    protected void titleRightButtonListener() {
    }

    protected void titleRightImageListener() {
    }

    /**
     * click center imageView
     */
    protected void titleCenterButtonListener() {

    }

    public interface ReLoginConfig {
        public void getConfig();
    }

    //如果没有内容，则显示无内容视图
    public void noView(View view){
        View noView = View.inflate(this,R.layout.nothing_view,null);
        if(view != null){
            if(view instanceof ViewGroup){
                ((ViewGroup) view).removeAllViews();
                ((ViewGroup) view).addView(noView);
            }else{
                ViewGroup viewGroup = (ViewGroup) view.getParent();
                viewGroup.removeView(view);
                viewGroup.addView(noView);
            }

        }



    }


    public void cityChoose(final TextView tv_view){
        tv_view.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                //弹出popupwinodw选择城市
                int [] location = new int[2];
                v.getLocationOnScreen(location);
                ListView listView = new ListView(BaseActivity.this);
                final SpinnerAreaAdapter text_adapter = new SpinnerAreaAdapter(BaseActivity.this);
                listView.setAdapter(text_adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        tv_view.setText(""+text_adapter.getItem(position-1));
                    }
                });
                PopupWindow popupWindowss = new PopupWindow(listView,tv_view.getWidth()*2, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindowss.setAnimationStyle(R.style.ShareAnimBase);
                popupWindowss.setFocusable(true);
                popupWindowss.setTouchable(true);
                popupWindowss.setOutsideTouchable(true);
                popupWindowss.getContentView().setFocusableInTouchMode(true);
                popupWindowss.getContentView().setFocusable(true);
                popupWindowss.setBackgroundDrawable(new BitmapDrawable());
                popupWindowss.update();
                popupWindowss.showAtLocation(tv_view, Gravity.NO_GRAVITY, location[0]-10,location[1]+tv_view.getHeight());
            }
    });
    }


}
