package com.wj.kindergarten.ui.mine.photofamilypic;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.net.upload.Result;
import com.wj.kindergarten.net.upload.UploadFile;
import com.wj.kindergarten.net.upload.UploadImage;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.imagescan.ScanPhoto_V1;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import java.util.List;

public class AddFamilyAlbumActivity extends BaseActivity {

    @ViewInject(id = R.id.add_family_album_name)
    EditText add_family_album_name;
    @ViewInject(id = R.id.add_family_album_iv_click,click = "click")
    RelativeLayout add_family_album_iv_click;
    @ViewInject(id = R.id.add_family_album_bt,click = "click")
    TextView add_family_album_bt;
    @ViewInject(id = R.id.add_family_album_iv)
    ImageView add_family_album_iv;
    private String localPath;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_add_family_album;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        setTitleText("添加家庭相册");
        FinalActivity.initInjectedView(this);
    }

    public void click(View view){
        switch (view.getId()){
            case R.id.add_family_album_iv_click:
                Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(pickIntent, GloablUtils.GET_PIC_ADD_FAMALI_ALBUM);
                break;
            case R.id.add_family_album_bt:
                addFamilyAlbum();
                break;
        }
    }

    private void addFamilyAlbum() {
        if(checkData()){
            savePic();
        }
    }

    public void savePic(){
        commonDialog.show();
        commonDialog.setText("图片上传中，请稍候...");
        UploadFile uploadFile = new UploadFile(AddFamilyAlbumActivity.this, new UploadImage() {
            @Override
            public void success(Result result) {
                if(commonDialog.isShowing()){
                    commonDialog.dismiss();
                }
                if (null != result) {
                    addFamily(result.getImgUrl());
                }
            }

            @Override
            public void failure(String message) {
                commonDialog.dismiss();
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(AddFamilyAlbumActivity.this, message);
                }
            }
        }, 1, 198, 198);
        if(!Utils.stringIsNull(localPath)){
            uploadFile.upload(localPath);
        }else {
            addFamily("");
        }

    }


    private void addFamily(String path) {
        commonDialog.show();
        commonDialog.setText("信息保存中，请稍候...");
        UserRequest.AddOrEditPfAlbum(this,add_family_album_name.getText().toString() , "", "0", path, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                if(commonDialog.isShowing()){
                    commonDialog.dismiss();
                }
                ToastUtils.showMessage("添加成功!");
                setResult(RESULT_OK);
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

    private boolean checkData() {
        String thing = add_family_album_name.getText().toString();
        if(thing == null || TextUtils.isEmpty(thing)){
            add_family_album_name.setError("请输入相册名");
            return false;
        }
//        if(localPath == null || TextUtils.isEmpty(localPath.toString())){
//            ToastUtils.showMessage("请选择图片!");
//            return false;
//        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK) return;
        switch (requestCode) {
            case GloablUtils.GET_PIC_ADD_FAMALI_ALBUM:
                Uri uri = data.getData();
                ImageLoaderUtil.displayImage(uri.toString(), add_family_album_iv);
                localPath =  ScanPhoto_V1.getPath(this,uri);
                break;
        }
    }
}
