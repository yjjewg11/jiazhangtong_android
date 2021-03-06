package com.wj.kindergarten.ui.mine.photofamilypic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ActivityManger;
import com.wj.kindergarten.abstractbean.RequestFailedResult;
import com.wj.kindergarten.bean.AllPfAlbumSun;
import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.BoutiqueReviewAddress;
import com.wj.kindergarten.bean.BoutiqueSingleInfoObject;
import com.wj.kindergarten.bean.PfModeName;
import com.wj.kindergarten.bean.PfModeNameObject;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.FindMusicOfPfActivity;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.Utils;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BoutiqueModeReviewActivity extends BaseActivity {

    public final int GET_MODE_MUSIC = 6700;

    @ViewInject(id = R.id.boutique_review_webview)
    private WebView boutique_review_webview;
    @ViewInject(id = R.id.boutique_review_tv_upload)
    private TextView boutique_review_tv_upload;
    private EditText editText;
    private PfModeNameObject modeObj;
    private int status = 1;//可见状态,临存，未发布
    private String title;//上传标题
    private String key;//模板主键
    private String uuid;//精品相册uuid
    private String mp3;//音乐地址
    private String hearld;//封面图片
    private ArrayList<AllPfAlbumSunObject> objectList;//照片地址集合
    private BoutiqueReviewAddress address;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_boutique_mode_review;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        //清空前面的选择集合。

        FinalActivity.initInjectedView(this);
        setTitleText("精品相册", "音乐");
        initViews();
        getData();
        loadData(1);
    }

    @Override
    protected void titleLeftButtonListener() {
        boutique_review_webview.destroy();
        super.titleLeftButtonListener();
    }

    private void getData() {
        Intent intent = getIntent();
        objectList = (ArrayList<AllPfAlbumSunObject>) intent.getSerializableExtra("objectList");
        modeObj = (PfModeNameObject) intent.getSerializableExtra("objectMode");
        if (modeObj != null) {
            title = modeObj.getTitle();
            hearld = modeObj.getHerald();
            uuid = modeObj.getAlbumUUid();
            mp3 = modeObj.getMp3();
            key = modeObj.getKey();
        }
    }

    @Override
    protected void onResume() {
        boutique_review_webview.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void titleRightButtonListener() {
        Intent intent = new Intent(this, FindMusicOfPfActivity.class);
        intent.putExtra("music", mp3);
        startActivityForResult(intent, GET_MODE_MUSIC, null);
        boutique_review_webview.loadUrl(address.getShare_url());
    }

    private void initViews() {
        setWebView(boutique_review_webview);
        boutique_review_webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (commonDialog.isShowing()) {
                    commonDialog.dismiss();
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                commonDialog.show();
            }
        });
        boutique_review_tv_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(BoutiqueModeReviewActivity.this, R.layout.boutique_review_custom_title, null);
                editText = (EditText) view.findViewById(R.id.boutique_custom_edittext);
                editText.setText("" + Utils.isNull(title));
                AlertDialog.Builder builder = new AlertDialog.Builder(BoutiqueModeReviewActivity.this);
                builder.setView(view);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        hideSoft();
                        onBackPressed();
                        saveAlbum();
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void saveAlbum() {
        Editable editable = editText.getText();
        if (editable != null) {
            title = editable.toString();
        }
        loadData(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case GET_MODE_MUSIC:
                mp3 = data.getStringExtra("mp3");
                loadData(1);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void loadData(final int cacheStatus) {
        commonDialog.show();
        commonDialog.setText("正在加载模板，请稍候!");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < objectList.size(); i++) {
            AllPfAlbumSunObject object = objectList.get(i);
            if (i != objectList.size() - 1) {
                builder.append(object.getUuid() + ",");
            } else {
                builder.append(object.getUuid());
            }
        }
        if (TextUtils.isEmpty(title)) {
            title = "相册预览!";
        }
        UserRequest.getBoutiqueReviewUrl(this, title,
                mp3, uuid, hearld, key
                , builder.toString(), cacheStatus, new RequestFailedResult(commonDialog) {
                    @Override
                    public void result(BaseModel domain) {
                        try {
                            if (commonDialog.isShowing()) {
                                commonDialog.dismiss();
                            }
                            if (cacheStatus == 0) {
                                boutique_review_webview.destroy();
                                ActivityManger.getInstance().finishPfActivities();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        sendBroadcast(new Intent(GloablUtils.UPDATE_BOUTIQUE_ALBUM_SUCCESSED));
                                        finish();
                                    }
                                }, 300);
                                finish();
                                return;
                            }
                            address = (BoutiqueReviewAddress) domain;
                            if (address != null) {
                                updateData();
                            }
                        } catch (Exception e) {
                            CGLog.v("这是保存精品相册后的异常!");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void result(List<BaseModel> domains, int total) {

                    }

                });
    }

    private void hideSoft() {
        Utils.inputMethod(BoutiqueModeReviewActivity.this, false, editText);
    }

    private void updateData() {
        boutique_review_webview.loadUrl(address.getShare_url());
        uuid = address.getData_id();
    }
}
