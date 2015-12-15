package com.wj.kindergarten.ui.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.umeng.socialize.utils.Log;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.IOStoreData.StoreDataInSerialize;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.ChildInfo;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.compounets.CircleImage;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.net.upload.Result;
import com.wj.kindergarten.net.upload.UploadFile;
import com.wj.kindergarten.net.upload.UploadImage;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.func.SpecialCourseDetailActivity;
import com.wj.kindergarten.ui.func.adapter.OwnAdapter;
import com.wj.kindergarten.ui.main.MineFragment;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.DateTimePickDialogUtil;
import com.wj.kindergarten.utils.EditTextCleanWatcher;
import com.wj.kindergarten.utils.FileUtil;
import com.wj.kindergarten.utils.GloablUtils;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.UserHeadImageUtil;
import com.wj.kindergarten.utils.Utils;
import com.wj.kindergarten.utils.WindowUtils;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
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
    private ChildInfo childInfo = null;
    private boolean isAdded;


    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// 头像文件名称
    private static final int REQUESTCODE_PICK = 0;        // 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;        // 相机拍照标记
    public static final int REQUESTCODE_CUTTING = 2;    // 图片裁切标记
    private String urlpath = "";//裁剪后图片的路径

    private EditText et_name, et_small_name, et_sex,
            et_birthday, et_ID_card, et_family;
    private String relationShip;
    private String nowName;
    private String oldTel;
    private String newTel;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_edit_child;
    }

    @Override
    protected void setNeedLoading() {

    }

    @Override
    protected void onCreate() {
        setTitleText("详细信息", "完成");
        initView();

        childInfo = (ChildInfo) getIntent().getSerializableExtra("childInfo");
        if (childInfo == null) {
            isAdded = true;
            childInfo = new ChildInfo();
            childInfo.setBa_tel(CGApplication.getInstance().getLogin().getUserinfo().getLoginname());
            headTv.setText("添加宝宝头像");
            circleImage.setImageResource(R.drawable.touxiang);
            return;
        }
        childInfo = ChildActivity.instance.getChildInfo();

        et_name.setText("" + childInfo.getName());
        et_small_name.setText("" + childInfo.getNickname());
        et_sex.setText("" + (childInfo.getSex() == 0 ? "男" : "女"));
        et_birthday.setText("" + childInfo.getBirthday());
        et_ID_card.setText("" + childInfo.getIdcard());
        //获取当前登录号码作比较
        String loginName = CGApplication.getInstance().getLogin().getUserinfo().getLoginname();
        if (!judgeIsNull(childInfo.getBa_tel()) && loginName.equals(childInfo.getBa_tel())) {
            relationShip = "爸爸";
        } else if (!judgeIsNull(childInfo.getMa_tel()) && loginName.equals(childInfo.getMa_tel())) {
            relationShip = "妈妈";
        } else if (!judgeIsNull(childInfo.getYe_tel()) && loginName.equals(childInfo.getYe_tel())) {
            relationShip = "爷爷";
        } else if (!judgeIsNull(childInfo.getNai_tel()) && loginName.equals(childInfo.getNai_tel())) {
            relationShip = "奶奶";
        } else if (!judgeIsNull(childInfo.getWaigong_tel()) && loginName.equals(childInfo.getWaigong_tel())) {
            relationShip = "外公";
        } else if (!judgeIsNull(childInfo.getWaipo_tel()) && loginName.equals(childInfo.getWaigong_tel())) {
            relationShip = "外婆";
        } else if (!judgeIsNull(childInfo.getOther_tel()) && loginName.equals(childInfo.getOther_tel())) {
            relationShip = "其他";
        }
        et_family.setText(relationShip);

        ImageLoaderUtil.displayImage(childInfo.getHeadimg(), circleImage);

    }

    private boolean judgeIsNull(String s) {
        return TextUtils.isEmpty(s);
    }

    private void initView() {
        circleImage = (CircleImage) findViewById(R.id.child_edit_head);
        headTv = (TextView) findViewById(R.id.child_edit_head_tv);

        et_name = (EditText) findViewById(R.id.et_name);
        et_small_name = (EditText) findViewById(R.id.et_small_name);
        et_sex = (EditText) findViewById(R.id.et_sex);
        et_birthday = (EditText) findViewById(R.id.et_birthday);
        et_ID_card = (EditText) findViewById(R.id.et_ID_card);
        et_family = (EditText) findViewById(R.id.et_family);

        et_sex.setFocusableInTouchMode(false);
        et_family.setFocusableInTouchMode(false);
        et_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(getApplicationContext(), R.layout.window_list, null);

                ListView listView_choose = (ListView) view.findViewById(R.id.window_lsit);

                final OwnAdapter ownAdapter = new OwnAdapter(EditChildActivity.this);
                ownAdapter.setList(Arrays.asList(new String[]{"男", "女"}));
                listView_choose.setAdapter(ownAdapter);


                final PopupWindow popupWindowss = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                popupWindowss.setAnimationStyle(R.style.ShareAnimBase);
                popupWindowss.setFocusable(true);
                popupWindowss.setTouchable(true);
                popupWindowss.setOutsideTouchable(true);
                popupWindowss.getContentView().setFocusableInTouchMode(true);
                popupWindowss.getContentView().setFocusable(true);
                popupWindowss.setBackgroundDrawable(new BitmapDrawable());
                popupWindowss.update();
                listView_choose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        et_sex.setText("" + ownAdapter.getItem(position));
                        popupWindowss.dismiss();
                    }
                });
                popupWindowss.showAsDropDown(v, 0, 0);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        popupWindowss.dismiss();
                    }
                });
            }
        });

        et_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = View.inflate(getApplicationContext(), R.layout.window_list, null);

                ListView listView_choose = (ListView) view.findViewById(R.id.window_lsit);

                final OwnAdapter ownAdapter = new OwnAdapter(EditChildActivity.this);
                ownAdapter.setList(Arrays.asList(new String[]{"爸爸", "妈妈", "爷爷", "奶奶", "外公", "外婆"}));
                listView_choose.setAdapter(ownAdapter);


                final PopupWindow popupWindowss = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                popupWindowss.setAnimationStyle(R.style.ShareAnimBase);
                popupWindowss.setFocusable(true);
                popupWindowss.setTouchable(true);
                popupWindowss.setOutsideTouchable(true);
                popupWindowss.getContentView().setFocusableInTouchMode(true);
                popupWindowss.getContentView().setFocusable(true);
                popupWindowss.setBackgroundDrawable(new BitmapDrawable());
                popupWindowss.update();

                listView_choose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        et_family.setText("" + ownAdapter.getItem(position));
                        popupWindowss.dismiss();
                    }
                });

                int[] location = new int[2];
                v.getLocationInWindow(location);

                View view1 = ownAdapter.getView(0, null, listView_choose);

                int w = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                view1.measure(w, h);
                int heignt1 = view1.getMeasuredHeight();
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) listView_choose.getLayoutParams();
                params.bottomMargin = WindowUtils.dm.heightPixels - location[1];
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                listView_choose.setLayoutParams(params);
                popupWindowss.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        popupWindowss.dismiss();
                    }
                });
            }

        });

        circleImage.setOnClickListener(this);
        headTv.setOnClickListener(this);
        et_birthday.setFocusableInTouchMode(false);
        et_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickDialogUtil dialogUtil = new DateTimePickDialogUtil(EditChildActivity.this,
                        et_birthday.getText().toString(), new DateTimePickDialogUtil.ChooseTime() {
                    @Override
                    public void choose(String time) {
                        et_birthday.setText(time);
                    }
                });
                dialogUtil.dateTimePicKDialog();
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
        startActivityForResult(intent, EditChildActivity.REQUESTCODE_CUTTING);
    }


    /**
     * 取得裁剪图片后调用
     * <p>
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {

        if (picdata != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = picdata.getParcelableExtra("data");
            Log.i("TAG", "拿到bitmap图片对象的引用" + photo);
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
        childInfo.setName(et_name.getText().toString().trim());
        childInfo.setNickname(et_small_name.getText().toString().trim());
        childInfo.setSex(et_sex.getText().toString().equals("男") == true ? 0 : 1);
        childInfo.setBirthday(et_birthday.getText().toString().trim());
        childInfo.setIdcard(et_ID_card.getText().toString().trim());
        //更改用户绑定关系
        nowName = et_family.getText().toString();
        oldTel = null;
        newTel = null;

        if (!TextUtils.isEmpty(relationShip) && !nowName.equals(relationShip)) {
            if (relationShip.equals("爸爸")) {
                childInfo.setBa_tel("");
                oldTel = "ba_tel";
            } else if (relationShip.equals("妈妈")) {
                childInfo.setMa_tel("");
                oldTel = "ma_tel";
            } else if (relationShip.equals("爷爷")) {
                childInfo.setYe_tel("");
                oldTel = "ye_tel";
            } else if (relationShip.equals("奶奶")) {
                childInfo.setNai_tel("");
                oldTel = "nai_tel";
            } else if (relationShip.equals("外公")) {
                childInfo.setWaigong_tel("");
                oldTel = "waigong_tel";
            } else if (relationShip.equals("外婆")) {
                childInfo.setWaipo_tel("");
                oldTel = "waipo_tel";
            }
        }


        String loginName = CGApplication.getInstance().getLogin().getUserinfo().getLoginname();
        if (nowName.equals("爸爸")) {
            childInfo.setBa_tel(loginName);
            newTel = "ba_tel";
        } else if (nowName.equals("妈妈")) {
            childInfo.setMa_tel(loginName);
            newTel = "ma_tel";
        } else if (nowName.equals("爷爷")) {
            childInfo.setYe_tel(loginName);
            newTel = "ye_tel";
        } else if (nowName.equals("奶奶")) {
            childInfo.setNai_tel(loginName);
            newTel = "nai_tel";
        } else if (nowName.equals("外公")) {
            childInfo.setWaigong_tel(loginName);
            newTel = "waigong_tel";
        } else if (nowName.equals("外婆")) {
            childInfo.setWaipo_tel(loginName);
            newTel = "waipo_tel";
        }
        if (!Utils.stringIsNull(imgPath)) {
            childInfo.setHeadimg(imgPath);
        }
        String addressName = null;
        if (isAdded) {
            addressName = "add";
        } else {
            addressName = "save";
        }
        UserRequest.changeChild(mContext, childInfo, addressName, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                dialog.dismiss();
                if (isAdded) {
                    //如果是添加新的小孩，直接返回主页面
                    CGApplication.getInstance().getLogin().getList().add(childInfo);
                    storeData();
                    finish();
                    return;
                }

                Intent ownIntent = new Intent();
                ownIntent.putExtra("head", childInfo.getHeadimg());
                ownIntent.putExtra("name", et_name.getText().toString());
                ownIntent.putExtra("nick", et_small_name.getText().toString());
                ownIntent.putExtra("sex", (et_sex.getText().toString().equals("男") == true ? 0 : 1));
                ownIntent.putExtra("birth", et_birthday.getText().toString());
                ownIntent.putExtra("idCard", et_ID_card.getText().toString());
                ownIntent.putExtra("oldTel", oldTel);
                ownIntent.putExtra("newTel", newTel);
                setResult(RESULT_OK, ownIntent);
                storeData();
                //将内存中的修改的childInfo替换掉
                replaceLogin(childInfo.getUuid());
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

    private void replaceLogin(String uuid) {

        if (CGApplication.getInstance().getLogin() != null && CGApplication.getInstance().getLogin().getList() != null) {
            for (int i = 0; i< CGApplication.getInstance().getLogin().getList().size() ; i++ ) {
                if (null != childInfo && CGApplication.getInstance().getLogin().getList().get(i).getUuid().equals(uuid)) {
                    CGApplication.getInstance().getLogin().getList().remove(i);
                    CGApplication.getInstance().getLogin().getList().add(i, childInfo);
                }
            }
        }


    }

    private void storeData() {

        StoreDataInSerialize.storeUserInfo(CGApplication.getInstance().getLogin());
//        Intent intent = new Intent(GloablUtils.MINE_ADD_CHILD_FINISH);
//        sendBroadcast(intent);
        MineFragment.instance.addChildren();


    }

    private boolean check() {
        if (Utils.stringIsNull(et_name.getText().toString())) {
            Utils.showToast(EditChildActivity.this, "姓名不能为空");
            return false;
        }

//        if (!Utils.stringIsNull(idEt.getText().toString())) {
//            IDCardUtil cardUtil = new IDCardUtil();
//            if (!cardUtil.verify(idEt.getText().toString())) {
//                Utils.showToast(EditChildActivity.this, "身份证不合法");
//                return false;
//            }
//        }
        return true;
    }


}
