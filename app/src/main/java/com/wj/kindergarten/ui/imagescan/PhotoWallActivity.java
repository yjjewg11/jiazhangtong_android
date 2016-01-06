package com.wj.kindergarten.ui.imagescan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.FileUtil;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.Utils;
import com.zbar.lib.decode.DecodeHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * PhotoWallActivity
 *
 * @author weiwu.song
 * @data: 2014/12/30 14:14
 * @version: v1.0
 */
public class PhotoWallActivity extends BaseActivity {
    public static final String RESULT_LIST = "ResultList";
    public static final String KEY_POSITION = "position";
    public static final String KEY_LIST = "list";
    public static final String KEY_TYPE = "type";
    private String titleRightT = "";
    private HackyViewPager mPager = null;
    private int preSize = 0;
    private PhotoWallAdapter photoWallAdapter = null;
    private ArrayList<String> list = null;

    @Override
    protected void setContentLayout() {
        layoutId = R.layout.activity_photo_wall;
    }

    @Override
    protected void setNeedLoading() {
    }

    @Override
    protected void onCreate() {
        titleRightButton.setVisibility(View.VISIBLE);

        int position = getIntent().getIntExtra(KEY_POSITION, 0);
        list = getIntent().getStringArrayListExtra(KEY_LIST);
        titleRightT = getIntent().getStringExtra(KEY_TYPE);
        if (list != null && list.size() > 0) {
            if (Utils.stringIsNull(titleRightT)) {
                setTitleText((position + 1) + "/" + list.size());
            } else {
                setTitleText((position + 1) + "/" + list.size(), titleRightT);
            }
        } else {
            setTitleText("");
        }
        preSize = list.size();
        mPager = (HackyViewPager) findViewById(R.id.photo_wall_pager);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                refreshPhotoWallTitle();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        photoWallAdapter = new PhotoWallAdapter(list,this);
        mPager.setAdapter(photoWallAdapter);
        mPager.setCurrentItem(position, false);
    }

    @Override
    protected void titleRightButtonListener() {
        if ("删除".equals(titleRightT)) {
            int now = mPager.getCurrentItem();
            list.remove(now);
            photoWallAdapter = new PhotoWallAdapter(list,this);
            mPager.setAdapter(photoWallAdapter);
            if (list.size() <= 0) {
                clickBack();
                return;
            }
            if (now >= list.size()) {
                mPager.setCurrentItem(now - 1, false);
            } else {
                mPager.setCurrentItem(now, false);
            }
            refreshPhotoWallTitle();
        } else if ("保存".equals(titleRightT)) {
            int now = mPager.getCurrentItem();
            String url = list.get(now);
            if (!(Utils.stringIsNull(url)) && url.contains("@")) {
                url = url.substring(0, url.indexOf("@"));
            }
            CGLog.d("photo wall title right->" + url);
            //弹出选择框
            savePhoto2Gallery(url);
        }
    }

    private void savePhoto2Gallery(String url) {
        if (Utils.stringIsNull(url)) {
            return;
        }
        showProgressDialog("保存图片中，请稍候...");
        ImageLoaderUtil.downLoadImageLoader(url, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                hideProgressDialog();
                Utils.showToast(mContext, "图片保存失败");
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                String fileName = System.currentTimeMillis() + ".jpg";
                saveImageToGallery(fileName, PhotoWallActivity.this, loadedImage);
                CGLog.d("P:" + Environment.getExternalStorageDirectory() + "/CGImage");
                hideProgressDialog();
                Utils.showToast(mContext, "图片保存成功");
                File appDir = new File(Environment.getExternalStorageDirectory() + "/CGImage");
                FileUtil.deleteFolder(appDir);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                hideProgressDialog();
            }
        });
    }


    public void saveImageToGallery(String fileName, Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "问界互动家园");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Utils.showToast(context, "图片保存失败");
            e.printStackTrace();
        } catch (IOException e) {
            Utils.showToast(context, "图片保存失败");
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            Utils.showToast(context, "图片保存失败");
            e.printStackTrace();
        }
        // 最后通知图库更新
        CGLog.d(Uri.fromFile(new File(file.getPath())) + "");
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(file.getPath()))));
//        new SingleMediaScanner(mContext, new File(file.getPath()), null);
    }

    private void refreshPhotoWallTitle() {
        int now = mPager.getCurrentItem();
        setTitleText((now + 1) + "/" + list.size(), titleRightT);
    }

    private void clickBack() {
        if (list.size() < preSize) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra(RESULT_LIST, list);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    @Override
    protected void titleLeftButtonListener() {
        clickBack();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            clickBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
