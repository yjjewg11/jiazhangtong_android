package com.wj.kindergarten.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.UserHeadImageUtil;

import java.io.File;

/**
 * Created by tangt on 2016/1/22.
*/
public class CommonChooseImageImpl implements ChooseImage {
    private BaseActivity activity;

    public CommonChooseImageImpl( BaseActivity activity, int takePhoto,int choosePic) {
        this.choosePic = choosePic;
        this.takePhoto = takePhoto;
        this.activity = activity;
    }

    int takePhoto;
    int choosePic;
    @Override
    public void chooseImage(int type) {
        switch (type){
            case UserHeadImageUtil.TAKE_PHOTO:
                //下面这句指定调用相机拍照后的照片存储的路径
                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), BaseActivity.IMAGE_FILE_NAME)));
                activity.startActivityForResult(takeIntent, takePhoto);
                break;
            case UserHeadImageUtil.CHOOSE_IMAGE_FROM_PICTURES:
                Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                activity.startActivityForResult(pickIntent, choosePic);
                break;
        }
    }

}
