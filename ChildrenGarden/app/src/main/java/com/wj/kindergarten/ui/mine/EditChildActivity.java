package com.wj.kindergarten.ui.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.ChildInfo;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.net.upload.Result;
import com.wj.kindergarten.net.upload.UploadFile;
import com.wj.kindergarten.net.upload.UploadImage;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.DateTimePickDialogUtil;
import com.wj.kindergarten.utils.EditTextCleanWatcher;
import com.wj.kindergarten.utils.FileUtil;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.IDCardUtil;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.UserHeadImageUtil;
import com.wj.kindergarten.utils.Utils;

import java.io.File;
import java.util.List;

/**
 * EditChildActivity
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/26 1:37
 */
public class EditChildActivity extends BaseActivity implements View.OnClickListener {
    private CircleImage circleImage;
    private TextView headTv;
    private EditText nameEt;
    private EditText nickEt;
    private TextView birthEt;
    private EditText idEt;
    private ChildInfo childInfo = null;

    private RelativeLayout layout1 = null;
    private RelativeLayout layout2 = null;
    private RelativeLayout layout4 = null;
    private ImageView imageView1 = null;
    private ImageView imageView2 = null;
    private ImageView imageView4 = null;
    private LinearLayout layoutMan = null;
    private LinearLayout layoutWeman = null;
    private ImageView imgMan = null;
    private ImageView imgWoman = null;
    private int sex = 0;

    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// 头像文件名称
    private static final int REQUESTCODE_PICK = 0;        // 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;        // 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2;    // 图片裁切标记
    private String urlpath = "";//裁剪后图片的路径

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_edit_child;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        setTitleText("详细信息", "保存");

        initView();

        childInfo = (ChildInfo) getIntent().getSerializableExtra("childInfo");
        ImageLoaderUtil.displayImage(childInfo.getHeadimg(), circleImage);
        nameEt.setText(childInfo.getName());
        nickEt.setText(childInfo.getNickname());
        birthEt.setText(childInfo.getBirthday());
        idEt.setText(childInfo.getIdcard());
        sex = childInfo.getSex();
        if (childInfo.getSex() == 0) {
            imgMan.setImageDrawable(getResources().getDrawable(R.drawable.man));
            imgWoman.setImageDrawable(getResources().getDrawable(R.drawable.woman2));
        } else {
            imgMan.setImageDrawable(getResources().getDrawable(R.drawable.man1));
            imgWoman.setImageDrawable(getResources().getDrawable(R.drawable.woman));
        }
    }

    private void initView() {
        circleImage = (CircleImage) findViewById(R.id.child_edit_head);
        headTv = (TextView) findViewById(R.id.child_edit_head_tv);
        nameEt = (EditText) findViewById(R.id.child_edit_name);
        nickEt = (EditText) findViewById(R.id.child_edit_nick);
        birthEt = (TextView) findViewById(R.id.child_edit_birth);
        idEt = (EditText) findViewById(R.id.child_edit_id);
        circleImage.setOnClickListener(this);
        headTv.setOnClickListener(this);

        layout1 = (RelativeLayout) findViewById(R.id.ll_clean_1);
        layout2 = (RelativeLayout) findViewById(R.id.ll_clean_2);
        layout4 = (RelativeLayout) findViewById(R.id.ll_clean_4);
        imageView1 = (ImageView) findViewById(R.id.iv_clean_1);
        imageView2 = (ImageView) findViewById(R.id.iv_clean_2);
        imageView4 = (ImageView) findViewById(R.id.iv_clean_4);
        nameEt.addTextChangedListener(new EditTextCleanWatcher(imageView1, nameEt));
        nickEt.addTextChangedListener(new EditTextCleanWatcher(imageView2, nickEt));
        idEt.addTextChangedListener(new EditTextCleanWatcher(imageView4, idEt));

        birthEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickDialogUtil dialogUtil = new DateTimePickDialogUtil(EditChildActivity.this,
                        birthEt.getText().toString(), new DateTimePickDialogUtil.ChooseTime() {
                    @Override
                    public void choose(String time) {
                        birthEt.setText(time);
                    }
                });
                dialogUtil.dateTimePicKDialog();
            }
        });

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameEt.setText("");
            }
        });

        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickEt.setText("");
            }
        });

        layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idEt.setText("");
            }
        });

        layoutMan = (LinearLayout) findViewById(R.id.layout_man);
        layoutWeman = (LinearLayout) findViewById(R.id.layout_woman);
        imgMan = (ImageView) findViewById(R.id.iv_man);
        imgWoman = (ImageView) findViewById(R.id.iv_woman);

        layoutMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgMan.setImageDrawable(getResources().getDrawable(R.drawable.man));
                imgWoman.setImageDrawable(getResources().getDrawable(R.drawable.woman2));
                sex = 0;
            }
        });

        layoutWeman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgMan.setImageDrawable(getResources().getDrawable(R.drawable.man1));
                imgWoman.setImageDrawable(getResources().getDrawable(R.drawable.woman));
                sex = 1;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.child_edit_head:
            case R.id.child_edit_head_tv:
                UserHeadImageUtil.showChooseImageDialog(EditChildActivity.this, circleImage, new ChooseImageImpl());
                break;
        }
    }

    private class ChooseImageImpl implements ChooseImage {

        @Override
        public void chooseImage(int type) {
            if (type == UserHeadImageUtil.TAKE_PHOTO) {
                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //下面这句指定调用相机拍照后的照片存储的路径
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                startActivityForResult(takeIntent, REQUESTCODE_TAKE);
            } else if (type == UserHeadImageUtil.CHOOSE_IMAGE_FROM_PICTURES) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(pickIntent, REQUESTCODE_PICK);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUESTCODE_PICK:// 直接从相册获取
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;
            case REQUESTCODE_TAKE:// 调用相机拍照
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case REQUESTCODE_CUTTING:// 取得裁剪后的图片
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }


    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = extras.getParcelable("data");
            // Drawable drawable = new BitmapDrawable(null, photo);
            File file = new File(urlpath);
            if (file.exists()) {
                file.delete();
            }
            urlpath = FileUtil.saveFile(mContext, System.currentTimeMillis() + ".jpg", photo);
            ImageLoaderUtil.displayImage("file://" + urlpath, circleImage);
            childInfo.setHeadimg(urlpath);
        }
    }

    @Override
    protected void titleRightButtonListener() {
        updateInfo();
    }

    private void updateInfo() {
        if (check()) {
            if (Utils.stringIsNull(urlpath)) {
                saveInfo("");
            } else {
                final HintInfoDialog dialog = new HintInfoDialog(EditChildActivity.this, "图片上传中，请稍后...");
                dialog.show();
                UploadFile uploadFile = new UploadFile(EditChildActivity.this, new UploadImage() {
                    @Override
                    public void success(Result result) {
                        dialog.dismiss();
                        if (null != result) {
                            saveInfo(result.getImgUrl());
                        }
                    }

                    @Override
                    public void failure(String message) {
                        dialog.dismiss();
                        if (!Utils.stringIsNull(message)) {
                            Utils.showToast(EditChildActivity.this, message);
                        }
                    }
                }, 1, 198, 198);
                CGLog.d("IMG:" + childInfo.getHeadimg());
                uploadFile.upload(childInfo.getHeadimg());
            }
        }
    }

    private void saveInfo(final String imgPath) {
        final HintInfoDialog dialog = new HintInfoDialog(EditChildActivity.this, "信息保存中，请稍后...");
        dialog.show();
        childInfo.setName(nameEt.getText().toString().trim());
        childInfo.setNickname(nickEt.getText().toString().trim());
        childInfo.setSex(sex);
        childInfo.setBirthday(birthEt.getText().toString().trim());
        childInfo.setIdcard(idEt.getText().toString().trim());
        if (!Utils.stringIsNull(imgPath)) {
            childInfo.setHeadimg(imgPath);
        }
        UserRequest.changeChild(mContext, childInfo, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                dialog.dismiss();
                Intent ownIntent = new Intent();
                ownIntent.putExtra("head", childInfo.getHeadimg());
                ownIntent.putExtra("name", nameEt.getText().toString());
                ownIntent.putExtra("nick", nickEt.getText().toString());
                ownIntent.putExtra("sex", sex);
                ownIntent.putExtra("birth", birthEt.getText().toString());
                ownIntent.putExtra("img", imgPath);
                setResult(RESULT_OK, ownIntent);
                finish();
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                dialog.dismiss();
                if (!Utils.stringIsNull(message)) {
                    Utils.showToast(mContext, message);
                }
            }
        });
    }

    private boolean check() {
        if (Utils.stringIsNull(nameEt.getText().toString())) {
            Utils.showToast(EditChildActivity.this, "姓名不能为空");
            return false;
        }

        if (!Utils.stringIsNull(idEt.getText().toString())) {
            IDCardUtil cardUtil = new IDCardUtil();
            if (!cardUtil.verify(idEt.getText().toString())) {
                Utils.showToast(EditChildActivity.this, "身份证不合法");
                return false;
            }
        }
        return true;
    }

}
