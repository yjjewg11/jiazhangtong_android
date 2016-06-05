package com.wj.kindergarten.ui.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.map.MapActivity;
import com.wj.kindergarten.utils.FileUtil;
import com.wj.kindergarten.utils.ToastUtils;
import com.wj.kindergarten.utils.Utils;
import com.zbar.lib.CaptureActivity;
import com.zbar.lib.decode.DecodeHandler;
import com.zbar.lib.encode.EncodingHandler;

/**
 * Created by tangt on 2016/1/6.
 */
public class ChooseTwoCode {
    private final View view;
    private Context context;
    private String imgUrl;
    public TextView tv_save;
    private TextView tv_parse;

    public ChooseTwoCode(Context context,String imgUrl) {
        this.context = context;
        this.imgUrl = imgUrl;
        view = View.inflate(context, R.layout.save_or_pasre, null);
        tv_save = (TextView) view.findViewById(R.id.save_pic_to_dicretory);
    }

    public void choose(View v){
        if(TextUtils.isEmpty(imgUrl) || TextUtils.isEmpty(Utils.isNull(imgUrl))){
            return;
        }

        tv_parse = (TextView) view.findViewById(R.id.pasre_two_code);
        if(parse){
            tv_parse.setVisibility(View.VISIBLE);
        }else {
            tv_parse.setVisibility(View.GONE);
        }
        TextView tv_scan_two_code = (TextView)view.findViewById(R.id.scan_two_code);
        TextView create_two_code = (TextView) view.findViewById(R.id.create_two_code);
        final ImageView image_create_two_code = (ImageView)view.findViewById(R.id.image_create_two_code);
        TextView look_for_map = (TextView)view.findViewById(R.id.look_for_map);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Utils.setPopWindow(popupWindow);
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        //保存图片
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        tv_save.setOnClickListener(new DownListeners(imgUrl) {
            @Override
            public void doOwnThing(String imageUri, Bitmap loadedImage) {
                popupWindow.dismiss();
                String fileName = System.currentTimeMillis() + ".jpg";
                FileUtil.saveImageToGallery(fileName, context, loadedImage);
            }
        });
        //将图片转换成数组，解析二维码
        tv_parse.setOnClickListener(new DownListeners(imgUrl) {
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
    }


    boolean parse;
    public void setParse(boolean parse) {
        this.parse = parse;
    }
}
