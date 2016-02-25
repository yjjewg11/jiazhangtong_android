package com.wj.kindergarten.ui.mine.photofamilypic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.FindMusicOfPfActivity;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

public class BoutiqueModeReviewActivity extends BaseActivity {

    public final int GET_MODE_MUSIC = 6700;

    @ViewInject(id = R.id.boutique_review_webview)
    private WebView boutique_review_webview;
    @ViewInject(id = R.id.boutique_review_tv_upload)
    private TextView boutique_review_tv_upload;
    private EditText editText;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_boutique_mode_review;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        FinalActivity.initInjectedView(this);
        setTitleText("相册模板预览","音乐");
        initViews();
    }

    @Override
    protected void titleRightButtonListener() {
        Intent intent = new Intent(this,FindMusicOfPfActivity.class);
        startActivityForResult(intent, GET_MODE_MUSIC, null);
    }

    private void initViews() {
        setWebView(boutique_review_webview);
        boutique_review_tv_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(BoutiqueModeReviewActivity.this, R.layout.boutique_review_custom_title, null);
                editText = (EditText) view.findViewById(R.id.boutique_custom_edittext);
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
                        saveAlbum();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void saveAlbum() {
        Editable editable = editText.getText();
        String title = null;
        if(editable != null){
            title = editable.toString();
        }
//        UserRequest.saveBoutiqueAlbum(this,title,);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) return;
        switch (requestCode){
            case GET_MODE_MUSIC :

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
