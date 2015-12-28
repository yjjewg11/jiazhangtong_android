package com.wj.kindergarten.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.compounets.NormalProgressDialog;
import com.wj.kindergarten.net.upload.Result;
import com.wj.kindergarten.net.upload.UploadFile;
import com.wj.kindergarten.net.upload.UploadImage;
import com.wj.kindergarten.ui.emot.ViewEmot2;
import com.wj.kindergarten.ui.func.adapter.SpinnerAreaAdapter;
import com.wj.kindergarten.ui.imagescan.GalleryImagesActivity;
import com.wj.kindergarten.ui.imagescan.NativeImageLoader;
import com.wj.kindergarten.ui.main.FoundFragment;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.ui.mine.ChooseImage;
import com.wj.kindergarten.ui.mine.EditChildActivity;
import com.wj.kindergarten.ui.mine.LoginActivity;
import com.wj.kindergarten.ui.more.SystemBarTintManager;

import com.wj.kindergarten.utils.FileUtil;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ShareUtils;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.UserHeadImageUtil;
import com.wj.kindergarten.utils.Utils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


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
    public RelativeLayout titleRightButton;
    private RelativeLayout title_rl_top;
    private SystemBarTintManager.SystemBarConfig config;
    public static final int WEB_SECLECT_PIC = 150;
    public static final int RECEIVER_PIC_TO_WEB = 5;
    private HintInfoDialog upLoadImageDialog;
    private String registerWeb;
    protected HintInfoDialog commonDialog;
    private Handler fatherHanle = new Handler();
    //用于每个单独的activity注册并对webview执行系列操作
    public ArrayList<WebView> webList = new ArrayList<>();

    public void addWeb(WebView webView){
        webList.add(webView);
    }

    public ArrayList<WebView> getWebList() {
        return webList;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(webList.size() > 0){
            for(WebView webView : webList){
                stopWebview(webView);
            }
        }
    }

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

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载顶部的状态栏颜色，使用于4.4以上版本
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(true);
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintEnabled(false);
//            tintManager.setStatusBarTintResource(R.color.title_bg);//通知栏所需颜色
//            config = tintManager.getConfig();
//        }
        mContext = this;
        //新开界面将webview置空
        webView = null;
        registerWeb = null;
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

    public void onPause(WebView webView){

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
        title_rl_top = (RelativeLayout)findViewById(R.id.title_rl_top);
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
            titleCenterTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
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

    public void stopWebview(WebView webView){
        if(webView == null ) return ;
        webView.destroy();
//        try {
//            webView.getClass().getMethod("onPause").invoke(webView,(Object[])null);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
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

    //hide softkeyboard and inputmethond

    public void hideSoftKeyBoard(ViewEmot2 viewEmot2,LinearLayout linearLayout){
        linearLayout.setVisibility(View.GONE);
        viewEmot2.hideSoftKeyboard();
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




    private WebView webView;

    //适用于所有页面的webview的回退操作


    //返回值判断是否需要子类做处理
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(webView != null && !TextUtils.isEmpty(registerWeb)){
            webView.loadUrl(registerWeb);
            return false;
        }else{
            if(BaseActivity.this instanceof MainActivity){
                ((MainActivity)BaseActivity.this).quitApp();
                return true;
            }
        }
        return super.onKeyDown(keyCode,event);
    }

    public void setWebView(WebView webView){
        this.webView = webView;
        if(webView == null) return;
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient(){});
        //给所有的webview添加接口
        webView.addJavascriptInterface(new WebJavaScript(webView),"JavaScriptCall");
        WebSettings webSettings = webView.getSettings();
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(false);
        webSettings.setJavaScriptEnabled(true);
////		ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// ���ö�λ�����ݿ�·��
        webSettings.setDomStorageEnabled(true);
    }


    //设置关闭下拉刷新菜单
    public void commonClosePullToRefreshListGridView(PullToRefreshAdapterViewBase pullView){
        if(pullView.isRefreshing()){
            pullView.onRefreshComplete();
        }
        ToastUtils.showMessage("没有更多内容了!");
        pullView.setMode(PullToRefreshBase.Mode.DISABLED);
    }
    public void commonClosePullToRefreshScrollView(PullToRefreshScrollView pullView, int pageNo){
        if(pageNo == 1){
            return ;
        }
        if(pullView.isRefreshing()){
            pullView.onRefreshComplete();
        }
        ToastUtils.showMessage("没有更多内容了!");
        pullView.setMode(PullToRefreshBase.Mode.DISABLED);
    }

    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";
    private static final int REQUESTCODE_PICK_WEB = 100101;        // 相册选图标记
    private static final int REQUESTCODE_TAKE_WEB = 100102;        // 相机拍照标记
    private static final int REQUESTCODE_CUTTING_WEB = 2;
    protected static final int UPLOAD_PIC_TO_WEB = 100103;
    public boolean isTure;
    class WebJavaScript{
        public WebJavaScript(View view) {
            this.view = view;
        }
        private View view;
        //回调注册web回退事件
        @JavascriptInterface
        public void setDoBackFN(String str){
            registerWeb = str;
        }
        @JavascriptInterface
        public String getJsessionid(){
            return CGApplication.getInstance().getLogin().getUserinfo().getJSESSIONID();
        }

        @JavascriptInterface
        public void finishProject(){
            if(BaseActivity.this instanceof MainActivity){
                try{
                    final MainActivity activity = (MainActivity)BaseActivity.this;
                    if(!isTure){
                        isTure = true;
                        activity.handler.post(new Runnable() {
                            @Override
                            public void run() {
                                activity.cancleMainWeb();
                            }
                        });

                    }else{
                        activity.quitApp();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


            }

        }

        @JavascriptInterface
        public void setShareContent(final String title, final String content, final String picUrl, final String httpUrl){

            fatherHanle.post(new Runnable() {
                @Override
                public void run() {
                    ShareUtils.showShareDialog(BaseActivity.this, view, title, content, picUrl, httpUrl, false);
                }
            });
        }
        //启动选择图片程序并且上传
        @JavascriptInterface
        public void selectImgPic(){
            Intent intent = new Intent(BaseActivity.this, GalleryImagesActivity.class);
            startActivityForResult(intent, RECEIVER_PIC_TO_WEB, null);
        }
        //选择头像图片
        @JavascriptInterface
        public void selectHeadPic(){
            UserHeadImageUtil.showChooseImageDialog(CGApplication.context, webView, new ChooseImage() {
                @Override
                public void chooseImage(int type) {
                    if (type == UserHeadImageUtil.TAKE_PHOTO) {
                        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //下面这句指定调用相机拍照后的照片存储的路径
                        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                        startActivityForResult(takeIntent, REQUESTCODE_TAKE_WEB);
                    } else if (type == UserHeadImageUtil.CHOOSE_IMAGE_FROM_PICTURES) {
                        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                        // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/jpeg");
                        startActivityForResult(pickIntent, REQUESTCODE_PICK_WEB);
                    }
                }
            });
        }
    }

    int count = 0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //选择单张图片进行裁剪

        if(resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUESTCODE_PICK_WEB:// 直接从相册获取
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                return;
            case REQUESTCODE_TAKE_WEB:// 调用相机拍照
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(temp));
                return;
            case WEB_SECLECT_PIC:// 取得裁剪后的图片
                if (data != null) {
//                    setPicToView(data);
                    String base =  getCompleteBase((Bitmap)data.getParcelableExtra("data"));
                    webView.loadUrl("javascript:G_jsCallBack.selectHeadPic_callback('" + base + "')");
                }
                return;
        }


        if(requestCode == RECEIVER_PIC_TO_WEB){
                ArrayList<String> images = data.getStringArrayListExtra(GalleryImagesActivity.RESULT_LIST);
                for(String image : images){
                    upLoadBaseImage(image);
                }
        }

    }

    private void upLoadBaseImage(String image) {
       Bitmap bitmap =  NativeImageLoader.getInstance().loadNativeImage(image, new NativeImageLoader.NativeImageCallBack() {
            @Override
            public void onImageLoader(Bitmap bitmap, String path) {
                upBitmap(bitmap);
            }
        });
        if(bitmap != null){
            upBitmap(bitmap);
        }
    }

    private void upBitmap(Bitmap bitmap) {
        String base = getCompleteBase(bitmap);
//        webView.loadUrl("http://www.baidu.com");
        webView.loadUrl("javascript:G_jsCallBack.selectPic_callback('" + base + "')");
    }

    @NonNull
    private String getCompleteBase(Bitmap bitmap) {
        return "data:image/png;base64,"+ FileUtil.getBase64FromBitmap(bitmap);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 198);
        intent.putExtra("outputY", 198);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, WEB_SECLECT_PIC);
    }
}
