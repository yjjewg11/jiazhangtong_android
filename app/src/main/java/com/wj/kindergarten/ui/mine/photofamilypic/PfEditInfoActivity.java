package com.wj.kindergarten.ui.mine.photofamilypic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.mine.ChooseImage;
import com.wj.kindergarten.utils.DateTimePickDialogUtil;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.UserHeadImageUtil;

import java.io.File;

/**
 * Created by tangt on 2016/1/14.
 */
public class PfEditInfoActivity extends BaseActivity {

    private static final String IMAGE_FILE_NAME = "pfeditInfo.jpg";
    private RelativeLayout[] relativeLayouts;
    private String title;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private String name;
    private TextView tv_album_name;
    private ImageView iv_appear_image;
    private TextView pf_tv_birthday;

    @Override
    protected void setContentLayout() {

        layoutId = R.layout.pf_edit_info;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        setTitleText("相册信息");
        initViews();
        initClicks();

    }

    private void initClicks() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.album_name:
                        dialog.show();
                        break;
                    case R.id.create_time:
                        DateTimePickDialogUtil dialogUtil = new DateTimePickDialogUtil(PfEditInfoActivity.this,
                                pf_tv_birthday.getText().toString(), new DateTimePickDialogUtil.ChooseTime() {
                            @Override
                            public void choose(String time) {
                                pf_tv_birthday.setText(time);
                            }
                        });
                        dialogUtil.dateTimePicKDialog();
                        break;
                    case R.id.appreance_photo:

                        UserHeadImageUtil.showChooseImageDialog(PfEditInfoActivity.this, v, new ChooseImage() {
                            @Override
                            public void chooseImage(int type) {
                                if (type == UserHeadImageUtil.TAKE_PHOTO) {
                                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    //下面这句指定调用相机拍照后的照片存储的路径
                                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                                    startActivityForResult(takeIntent, GloablUtils.PF_EDIT_TAKE_PHOTO);
                                } else if (type == UserHeadImageUtil.CHOOSE_IMAGE_FROM_PICTURES) {
                                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                                    // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                    startActivityForResult(pickIntent, GloablUtils.PF_EDIT_CHOOSE_IMAGE);
                                }
                            }
                        });
                        break;
                }
            }
        };
        for (RelativeLayout relativeLayout : relativeLayouts){
            relativeLayout.setOnClickListener(listener);
        }
    }

    private void initViews() {
        relativeLayouts = new RelativeLayout[]{
                (RelativeLayout) findViewById(R.id.album_name),
                (RelativeLayout) findViewById(R.id.appreance_photo),
                (RelativeLayout) findViewById(R.id.create_time),
        };
        tv_album_name = (TextView) findViewById(R.id.tv_album_name);
        iv_appear_image = (ImageView) findViewById(R.id.iv_appear_image);
        pf_tv_birthday = (TextView) findViewById(R.id.pf_tv_birthday);

        final EditText editText = new EditText(this);
        builder  = new AlertDialog.Builder(this);
        dialog = builder.setTitle(title).setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = editText.getText().toString();
                tv_album_name.setText(editText.getText().toString());
            }
        }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setView(editText).create();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case GloablUtils.PF_EDIT_TAKE_PHOTO :
            case GloablUtils.PF_EDIT_CHOOSE_IMAGE :
                ImageLoaderUtil.displayMyImage(data.getData().toString(),iv_appear_image);
                break;
        }
    }
}
