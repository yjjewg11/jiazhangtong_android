package com.wj.kindergarten.ui.func;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Selection;
import android.text.Spannable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.Class;
import com.wj.kindergarten.bean.Emot;
import com.wj.kindergarten.bean.HtmlTitle;
import com.wj.kindergarten.bean.PfMusic;
import com.wj.kindergarten.common.Constants;
import com.wj.kindergarten.handler.GlobalHandler;
import com.wj.kindergarten.handler.MessageHandlerListener;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.request.UserRequest;
import com.wj.kindergarten.net.upload.Result;
import com.wj.kindergarten.net.upload.UploadFile;
import com.wj.kindergarten.net.upload.UploadImage;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.addressbook.EmotManager;
import com.wj.kindergarten.ui.emot.ChooseFace;
import com.wj.kindergarten.ui.emot.EmotUtil;
import com.wj.kindergarten.ui.emot.FaceAdapter;
import com.wj.kindergarten.ui.imagescan.GalleryImagesActivity;
import com.wj.kindergarten.ui.imagescan.PhotoWallActivity;
import com.wj.kindergarten.ui.viewpager.CirclePageIndicator;
import com.wj.kindergarten.ui.viewpager.ViewPagerAdapter;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.HintInfoDialog;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;

import net.tsz.afinal.annotation.view.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * InteractionSentActivity
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/7/26 21:28
 */
public class EditPfActivity extends BaseActivity {


    private static final int REQUEST_MUSIC_CODE  = 3008;
    private LinearLayout photoContent = null;
    private ArrayList<String> images = new ArrayList<>();
    @ViewInject(id=R.id.edit_pf_choose_music,click="onClick")
    private EditText edit_pf_choose_music;
    private ImageView edit_pf_iamge;
    @ViewInject(id=R.id.edit_pf_right_pic,click="onClick")
    private TextView edit_pf_right_pic;
    private PfMusic pfMusic;


    @Override
    protected void setContentLayout() {
        layoutId = R.layout.edit_pf_activity;
    }

    @Override
    protected void setNeedLoading() {

    }

    boolean isResult = true;
    AlertDialog alertDialog;
    @Override
    protected void onCreate() {
        setTitleText("编辑相册", "完成");

        initViews();



        GlobalHandler.getHandler().addMessageHandlerListener(new MessageHandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                CGLog.d("AN: " + getRunningActivityName());
                if (msg.what == 1024) {
                    //这个消息是网络状态改变，不是用来上图片的
//                    if (!isCon && null != images && images.size() > 0) {
//                        isCon = true;
//                        uploadImage(count);
//                    }
                }
            }
        });
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.edit_pf_choose_music :
                 Intent intent = new Intent(this,FindMusicOfPfActivity.class);
                 startActivityForResult(intent,REQUEST_MUSIC_CODE,null);
                break;
            case R.id.edit_pf_right_pic :

                break;
        }
    }


    private void initViews() {
        edit_pf_choose_music = (EditText) findViewById(R.id.edit_pf_choose_music);
        photoContent = (LinearLayout) findViewById(R.id.repairs_photo_content);
        edit_pf_iamge = (ImageView) findViewById(R.id.edit_pf_iamge);
    }


    private String getRunningActivityName() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return runningActivity;
    }

    /**
     * 获取分页数据
     *
     * @param page
     * @return
     */

    /**
     * 刷新图片
     */
    private void refreshPhoto() {
        int photoW = (int) getResources().getDimension(R.dimen.fix_detail_photo_height);
        int photoMargin = (int) getResources().getDimension(R.dimen.small_padding);

        photoContent.removeAllViews();
        // photoContent.setBackgroundColor(getResources().getColor(android.R.color.black));
        for (int i = 0; i < images.size(); i++) {
            final int position = i;
            ImageView photoView = new ImageView(mContext);
            LinearLayout.LayoutParams photoLayoutParams = new LinearLayout.LayoutParams(photoW, photoW);
            if (i > 0) {
                photoLayoutParams.leftMargin = photoMargin;
            }
            photoView.setLayoutParams(photoLayoutParams);
            photoView.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageLoaderUtil.displayImage("file://" + images.get(i), photoView);
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickPhoto(position);
                }
            });
            photoContent.addView(photoView);
        }

        if (images.size() < GalleryImagesActivity.IMAGE_MAX) {
            ImageView photoView = new ImageView(mContext);
            LinearLayout.LayoutParams photoLayoutParams = new LinearLayout.LayoutParams(photoW, photoW);
            if (images.size() > 0) {
                photoLayoutParams.leftMargin = photoMargin;
            }
            photoLayoutParams.gravity = Gravity.CENTER;
            photoView.setLayoutParams(photoLayoutParams);
            photoView.setScaleType(ImageView.ScaleType.FIT_XY);
            photoView.setImageResource(R.drawable.interaction_add_image);
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    File appDir = new File(Environment.getExternalStorageDirectory()+"/CGImage");
//                    FileUtil.deleteFolder(appDir);
                    takePhoto();
                }
            });
            photoContent.addView(photoView);
        }
    }



    /**
     * 添加已有图片
     */
    private void takePhoto() {
        Intent intent = new Intent(this, GalleryImagesActivity.class);
        intent.putExtra(Constants.ALREADY_SELECT_KEY, images);
//        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    /**
     * 点击图片
     *
     * @param position 点击的图片位置
     */
    private void clickPhoto(int position) {
        Intent intent = new Intent(this, PhotoWallActivity.class);
        intent.putExtra(PhotoWallActivity.KEY_POSITION, position);
        intent.putStringArrayListExtra(PhotoWallActivity.KEY_LIST, images);
        intent.putExtra(PhotoWallActivity.KEY_TYPE, "删除");
//        startActivityForResult(intent, REQUEST_SHOW_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if(data == null) return;
        if(requestCode == REQUEST_MUSIC_CODE){
            pfMusic = (PfMusic) getIntent().getSerializableExtra("pfMusic");
            showText();
        }
//            if (requestCode == REQUEST_TAKE_PHOTO) {
//                images.clear();
//                ArrayList<String> result = data.getStringArrayListExtra(GalleryImagesActivity.RESULT_LIST);
//                images.addAll(result);
//                refreshPhoto();
//            } else if (requestCode == REQUEST_SHOW_GALLERY) {
//                images.clear();
//                ArrayList<String> result = data.getStringArrayListExtra(PhotoWallActivity.RESULT_LIST);
//                images.addAll(result);
//                refreshPhoto();
//            }


    }

    private void showText() {
        if(pfMusic == null) return;
        edit_pf_choose_music.setText(""+pfMusic.getTitle());
    }


    @Override
    protected void titleRightButtonListener() {

    }


//    private class UploadImageImpl implements UploadImage {
//
//        @Override
//        public void success(Result result) {
//
//        }
//
//        @Override
//        public void failure(String message) {
//            if (time < 3) {
//                time++;
//                uploadImage(count);
//            } else {
//                if (null != dialog) {
//                    dialog.dismiss();
//                }
//                Utils.showToast(EditPfActivity.this, /*"抱歉，图片上传失败。"*/message);
//            }
//        }
//    }



}
