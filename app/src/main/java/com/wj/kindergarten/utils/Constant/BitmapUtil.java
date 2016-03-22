package com.wj.kindergarten.utils.Constant;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

/**
 * Created by tangt on 2016/2/25.
 */
public abstract class BitmapUtil {

    //此类对图片做模糊处理
    public static synchronized BitmapDrawable blur(Resources resources,float radius,Bitmap bkg) {
        if(radius < 0){
            radius = 20;
        }
        long startMs = System.currentTimeMillis();
        float scaleFactor = 9;//图片缩放比例；//模糊程度

        Bitmap overlay = Bitmap.createBitmap(
                (int) (bkg.getWidth() / scaleFactor),
                (int) (bkg.getHeight() / scaleFactor),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);


        overlay = FastBlur.doBlur(overlay, (int) radius, true);
        return new BitmapDrawable(resources,overlay);
        /**
         * 打印高斯模糊处理时间，如果时间大约16ms，用户就能感到到卡顿，时间越长卡顿越明显，如果对模糊完图片要求不高，可是将scaleFactor设置大一些。
         */
    }
}
