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
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.map.MapActivity;
import com.wj.kindergarten.utils.FileUtil;
import com.wj.kindergarten.utils.ImageLoaderUtil;
import com.wj.kindergarten.utils.ShareUtils;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.WindowUtils;
import com.zbar.lib.CaptureActivity;
import com.zbar.lib.decode.DecodeHandler;
import com.zbar.lib.encode.EncodingHandler;

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
        TextView create_two_code = (TextView) view.findViewById(R.id.create_two_code);
        final ImageView image_create_two_code = (ImageView)view.findViewById(R.id.image_create_two_code);
        TextView look_for_map = (TextView)view.findViewById(R.id.look_for_map);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ShareUtils.setPopWindow(popupWindow);
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

        //保存图片
        tv_save.setOnClickListener(new DownListeners() {
            @Override
            public void doOwnThing(String imageUri, Bitmap loadedImage) {
                popupWindow.dismiss();
                String fileName = System.currentTimeMillis() + ".jpg";
                FileUtil.saveImageToGallery(fileName, context, loadedImage);
            }
        });
        //将图片转换成数组，解析二维码
        tv_parse.setOnClickListener(new DownListeners() {
            @Override
            public void doOwnThing(String imageUri, Bitmap loadedImage) {
                if (loadedImage == null) {
                    ToastUtils.showMessage("图片下载失败!");
                    return;
                }
                popupWindow.dismiss();
                DecodeHandler decodeHandler = new DecodeHandler(context);
                Message message = new Message();
                message.what = DecodeHandler.DECODE_FROM_PIC_URL;
                message.obj = loadedImage;
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

        //生成二维码
        create_two_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //默认生成百度网址的二维码
                try {
                    String path = System.currentTimeMillis() +".jpg";
                    Bitmap bitmap =  EncodingHandler.createQRCode("大家好，这是问界家园二维码扫描中心，" +
                            "恭喜你获得1000万大奖，请在节前领取，过期不候！", 200);
                    FileUtil.saveImageToGallery(path, context,bitmap);
                    image_create_two_code.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        //查看地图
        look_for_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        return false;
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