

package com.wj.kindergarten.ui.messagepag;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.umeng.socialize.utils.Log;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.abstractbean.RequestFailedResult;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Login;
import com.wj.kindergarten.bean.UserInfo;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.net.upload.Result;
import com.wj.kindergarten.net.upload.UploadFile;
import com.wj.kindergarten.net.upload.UploadImage;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.mine.ChooseImage;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.FileUtil;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.UserHeadImageUtil;
import com.wj.kindergarten.utils.Utils;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import java.io.File;
import java.util.List;

public class EditMyInfoActivity extends BaseActivity {


    @ViewInject(id = R.id.et_myInfo_name)
    EditText et_myInfo_name;
    @ViewInject(id = R.id.et_myInfo_relname)
    EditText et_myInfo_relname;
    @ViewInject(id = R.id.et_myinfo_tel)
    EditText et_myinfo_tel;
    @ViewInject(id = R.id.et_myinfo_iv)
    ImageView et_myinfo_iv;
    private String urlpath;
    private String imageUrl;
    private boolean needUploadPic;


    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_edit_my_info;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate(){
        FinalActivity.initInjectedView(this);
        setTitleText("个人信息", R.drawable.bianji_jpxc_jingpin);
        initHeadData();
        initClick();
    }

    private void initClick() {
        et_myinfo_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserHeadImageUtil.showChooseImageDialog(EditMyInfoActivity.this, et_myinfo_iv, new ChooseImageImpl());
            }
        });
    }

    public boolean isNeedUploadPic() {
        return Utils.stringIsNull(imageUrl);
    }

    private class ChooseImageImpl implements ChooseImage {

        @Override
        public void chooseImage(int type) {
            if (type == UserHeadImageUtil.TAKE_PHOTO) {
                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //下面这句指定调用相机拍照后的照片存储的路径
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                startActivityForResult(takeIntent, GloablUtils.MY_INFO_REQUESTCODE_TAKE);
            } else if (type == UserHeadImageUtil.CHOOSE_IMAGE_FROM_PICTURES) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(pickIntent, GloablUtils.MY_INFO_REQUESTCODE_PICK);
            }
        }
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = createIntentCrop(uri);
        startActivityForResult(intent, GloablUtils.MY_INFO_REQUESTCODE_CUTTING);
    }
    @NonNull
    private Intent createIntentCrop(Uri uri) {
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
        return intent;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case GloablUtils.MY_INFO_REQUESTCODE_PICK:// 直接从相册获取
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;
            case GloablUtils.MY_INFO_REQUESTCODE_TAKE:// 调用相机拍照
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case GloablUtils.MY_INFO_REQUESTCODE_CUTTING:// 取得裁剪后的图片
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setPicToView(Intent picdata) {

        if (picdata != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = picdata.getParcelableExtra("data");
            Log.i("TAG", "拿到bitmap图片对象的引用" + photo);
            // Drawable drawable = new BitmapDrawable(null, photo);
            if(!Utils.stringIsNull(urlpath)){
                File file = new File(urlpath);
                if (file != null && file.exists()) {
                    file.delete();
                }
            }
            urlpath = FileUtil.saveFile(mContext, System.currentTimeMillis() + ".jpg", photo);
            ImageLoaderUtil.displayImage("file://" + urlpath, et_myinfo_iv);
            imageUrl = urlpath;
        }
    }

    private void initHeadData() {
        Login login = CGApplication.getInstance().getLogin();
        if(login == null || login.getUserinfo() == null)return;
        UserInfo userInfo = login.getUserinfo();
        String name = userInfo.getName();
        et_myInfo_name.setText(""+ Utils.isNull(name));
        String img = userInfo.getImg();
        if(!Utils.stringIsNull(img)){
            imageUrl = img;
            ImageLoaderUtil.displayImage(img,et_myinfo_iv);
        }
        String tel = userInfo.getTel();
        if(Utils.stringIsNull(tel)){
            et_myinfo_tel.setText("暂未绑定手机号码");
        }else {
            et_myinfo_tel.setText("" + Utils.isNull(tel));
        }
        et_myInfo_relname.setText("" + Utils.isNull(userInfo.getRealname()));

    }


    @Override
    protected void titleRightButtonListener() {
        upData();
    }

    private void upData() {
        if (checkData()) {
            if (isNeedUploadPic()) {
                saveInfo(imageUrl);
            } else {
                showCommonDialog();
                UploadFile uploadFile = new UploadFile(EditMyInfoActivity.this, new UploadImage() {
                    @Override
                    public void success(Result result) {
                        cancleCommonDialog();
                        if (null != result) {
                            saveInfo(result.getImgUrl());
                        }
                    }

                    @Override
                    public void failure(String message) {
                        cancleCommonDialog();
                        if (!Utils.stringIsNull(message)) {
                            Utils.showToast(EditMyInfoActivity.this, message);
                        }
                    }
                }, 1, 198, 198);

                uploadFile.upload(imageUrl);
            }
        }
    }

    private void saveInfo(String imgUrl) {
        UserRequest.saveUserInfo(this,imgUrl,et_myInfo_name.getText().toString(),
                et_myInfo_relname.getText().toString(),new RequestFailedResult() {
            @Override
            public void result(BaseModel domain) {
                ToastUtils.showMessage("修改成功");
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }
        });

    }

//    POST  http://jz.wenjienet.com/px-mobile/rest/userinfo/update.json

    private boolean checkData() {
        if(Utils.stringIsNull(et_myInfo_name.getText().toString())) return false;
        return true;
    }

}
