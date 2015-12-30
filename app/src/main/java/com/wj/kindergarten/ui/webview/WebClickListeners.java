package com.wj.kindergarten.ui.webview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.ShareUtils;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.WindowUtils;
import com.zbar.lib.CaptureActivity;
import com.zbar.lib.decode.DecodeHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by tangt on 2015/12/29.
 */
public class WebClickListeners implements View.OnLongClickListener {
    private Context context;
    private String imgurl;

    public WebClickListeners(Context context) {
        this.context = context;
    }

    @Override
    public boolean onLongClick(View v) {
        WebView.HitTestResult result = ((WebView) v).getHitTestResult();
        if (result != null) {
            int type = result.getType();
            if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                imgurl = result.getExtra();
            } else {
                return false;
            }
        }

        View view = View.inflate(context, R.layout.save_or_pasre, null);
        TextView tv_save = (TextView) view.findViewById(R.id.save_pic_to_dicretory);
        TextView tv_parse = (TextView) view.findViewById(R.id.pasre_two_code);
        TextView tv_scan_two_code = (TextView)view.findViewById(R.id.scan_two_code);

        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ShareUtils.setPopWindow(popupWindow);
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

        //保存图片
        tv_save.setOnClickListener(new DownListeners() {
            @Override
            public void doOwnThing(String imageUri, Bitmap loadedImage) {
                popupWindow.dismiss();
                String fileName = System.currentTimeMillis() + ".jpg";
                saveImageToGallery(fileName, context, loadedImage);
            }
        });
        //将图片转换成数组，解析二维码
        tv_parse.setOnClickListener(new DownListeners() {
            ByteArrayOutputStream outputStream;

            @Override
            public void doOwnThing(String imageUri, Bitmap loadedImage) {
                popupWindow.dismiss();
                outputStream = new ByteArrayOutputStream();
                loadedImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                byte[] bytes = outputStream.toByteArray();
                DecodeHandler decodeHandler = new DecodeHandler(context);
                Message message = new Message();
                message.what = DecodeHandler.DECODE_FROM_PIC;
                message.obj = bytes;
                message.arg1 = WindowUtils.dm.heightPixels;
                message.arg2 = WindowUtils.dm.widthPixels;
                decodeHandler.sendMessage(message);
            }
        });
        //启动扫描二维码
        tv_scan_two_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                context.startActivity(new Intent(context, CaptureActivity.class));
            }
        });

        return false;
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
            ToastUtils.showMessage("图片保存失败");
            e.printStackTrace();
        } catch (IOException e) {
            ToastUtils.showMessage("图片保存失败");
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            ToastUtils.showMessage("图片保存失败");
            e.printStackTrace();
        }
        // 最后通知图库更新
//                                                CGLog.d(Uri.fromFile(new File(file.getPath())) + "");
        Intent intent = new Intent();
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(file.getPath()))));
//        new SingleMediaScanner(mContext, new File(file.getPath()), null);
        ToastUtils.showMessage("图片保存成功");
    }

    abstract class DownListeners implements View.OnClickListener {

        public abstract void doOwnThing(String imageUri, Bitmap loadedImage);

        @Override
        public void onClick(View v) {
            downPic();
        }

        private void downPic() {
            ImageLoaderUtil.downLoadImageLoader(imgurl,
                    new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//
                            doOwnThing(imageUri, loadedImage);

//
//                                                ToastUtils.showMessage("图片保存成功");
//                                                MainActivity.instance.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File("/sdcard/Boohee/image.png"))));


                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }

                    });
        }
    }
}