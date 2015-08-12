package com.wj.kindergarten.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.common.Constants;
import com.wj.kindergarten.ui.mine.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utils
 *
 * @author weiwu.song
 * @data: 2014/12/17 15:36
 * @version: v1.0
 */
public class Utils {
    private static final int ANIMATION_DURATION = 300;//动画执行时间
    public static final SimpleDateFormat timestampFormatter = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.sss");

    /**
     * util class, avoid to instantiate
     */
    private Utils() {
    }

    /**
     * judge str is null or not
     *
     * @param str
     * @return true:str is not null ,flase:str is null
     */
    public static boolean stringIsNull(String str) {
        if (str == null || "".equals(str) || "null".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * 显示时排除显示null的情况
     *
     * @param str
     * @return
     */
    public static String getText(String str) {
        if (str == null || "".equals(str) || "null".equals(str)) {
            return "";
        } else {
            return str;
        }
    }

    /**
     * show toast
     *
     * @param mContext Context of activity
     * @param tips     toast to show tips
     */
    public static void showToast(Context mContext, String tips) {
        Toast.makeText(mContext, tips, Toast.LENGTH_SHORT).show();
    }

    /**
     * get main item width
     *
     * @return width of main item
     */
    public static int getMainItemWidth() {
        return getScreenHW()[1] / Constants.MAIN_ITEM_MAX;
    }

    /**
     * get screen height and width
     *
     * @return hw[0]:height ,hw[1]:width
     */
    public static int[] getScreenHW() {
        int[] hw = {0, 0};
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager manager = (WindowManager) CGApplication.getInstance()
                .getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(dm);
        hw[1] = dm.widthPixels;//宽度
        hw[0] = dm.heightPixels;//高度
        return hw;
    }

    public static int getHeightByScreenWeight(int weight) {
        if (weight == 0) {
            weight = 1;
        }
        int hight = getScreenHW()[0];
        if (hight == 0) {
            hight = 1080;
        }
        return hight / weight;
    }

    public static int getWidthByScreenWeight(int weight) {
        if (weight == 0) {
            weight = 1;
        }
        int width = getScreenHW()[1];
        if (width == 0) {
            width = 1080;
        }
        return width / weight;
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidht = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidht, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
                true);
        return newbmp;
    }

    /**
     * dip to px
     *
     * @param dpValue the dp
     * @return the result px
     */
    public static int dip2px(float dpValue) {
        final float scale = CGApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取应用版本名称
     */
    public static String getVersionName() {
        return getVersionName(CGApplication.getInstance());
    }

    /**
     * 获取应用版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageInfo pi = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return context.getString(R.string.version_unknown);
        }
    }

    /**
     * 拨打电话
     *
     * @param context Context
     * @param tel     电话号码
     */
    public static void callPhone(Context context, String tel) throws Exception {
        if (stringIsNull(tel)) {
            return;
        }
        tel = tel.replace("转", ",,");
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.fromParts("tel", tel, null));//拼一个电话的Uri，拨打分机号 关键代码
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 文件转换为流
     *
     * @param filePath 要转换的文件
     * @return 转换后的流
     */
    public static InputStream file2Stream(String filePath) {
        try {
            FileInputStream fin = new FileInputStream(filePath);
            InputStream in = new BufferedInputStream(fin);
            return in;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将long转为string日期
     *
     * @param time
     * @return
     */
    public static String getDate(long time) {
        if (time > 0) {
            Date d = new Date(time);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.format(d);
        } else {
            return "";
        }
    }

    /**
     * 将long转为string日期
     *
     * @param time
     * @return
     */
    public static String getDate2(long time) {
        if (time > 0) {
            Date d = new Date(time);
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
            return sdf.format(d);
        } else {
            return "";
        }
    }

    /**
     * 翻转动画
     *
     * @param moveLayout 要旋转的view
     * @param start      开始角度
     * @param end        结束角度
     */
//    public static void rotationAnimations(View moveLayout, float start, float end) {
//        if (Build.VERSION.SDK_INT < 11) {
//            com.nineoldandroids.animation.ObjectAnimator animY
//                    = com.nineoldandroids.animation.ObjectAnimator.ofFloat(moveLayout,
// "rotation", start, end);
//            animY.setDuration(ANIMATION_DURATION);
//            animY.start();
//        } else {
//            ObjectAnimator animY = ObjectAnimator.ofFloat(moveLayout, "rotation", start, end);
//            animY.setDuration(ANIMATION_DURATION);
//            animY.start();
//        }
//    }

    /**
     * 执行Y轴平移动画，并保持动画结束时状态
     *
     * @param moveLayout 要执行动画的view
     * @param deltaY     Y轴偏移量
     */
//    public static void doBottomSlidAnimations(View moveLayout, float deltaY) {
//        if (Build.VERSION.SDK_INT < 11) {
//            com.nineoldandroids.animation.ObjectAnimator animY
//                    = com.nineoldandroids.animation.ObjectAnimator.ofFloat(moveLayout,
// "translationY", deltaY);
//            animY.setDuration(300);
//            animY.start();
//        } else {
//            ObjectAnimator animY = ObjectAnimator.ofFloat(moveLayout, "translationY", deltaY);
//            animY.setDuration(300);
//            animY.start();
//        }
//    }


    /**
     * 是否显示输入法
     *
     * @param context
     * @param isShow
     */
    public static void inputMethod(Context context, boolean isShow, View view) {
        // 隐藏输入法
        InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        // 显示或者隐藏输入法
        if (isShow) {
            imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
        } else {
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     * 随机生成RSA密钥对
     *
     * @param keyLength 密钥长度，范围：512～2048 一般1024
     * @return
     */
    public static KeyPair generateRSAKeyPair(int keyLength) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(keyLength);
            return kpg.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 随机生成RSA密钥对 密钥长度1024
     *
     * @return
     */
    public static KeyPair generateRSAKeyPair() {
        return generateRSAKeyPair(1024);
    }

    /**
     * 转换数据为指定对象
     *
     * @param targetClass
     * @param responseBody
     * @return
     */
    public static <T> T response2Obj(Class<T> targetClass, byte[] responseBody) {
        if (responseBody == null || responseBody.length == 0) {
            return null;
        }
        String json = "";
        try {
            json = new String(responseBody, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        return gson.fromJson(json, targetClass);
    }

    /**
     * 判断网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 从assert文件夹中读取省市区的json文件，然后转化为json对象
     */
    public static JSONObject initCityJsonData(Context context, String file) {
        try {
            StringBuffer sb = new StringBuffer();
            InputStream is = context.getAssets().open(file);
            int len = -1;
            byte[] buf = new byte[is.available()];
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "utf-8"));
            }
            is.close();

            return new JSONObject(sb.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取时间戳
     */
    public static String getTimestamp() {
        return timestampFormatter.format(System.currentTimeMillis());
    }

    /**
     * 获取md5
     *
     * @param string 要获取md5的字串
     */
    public static String getMd5Old(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString().toLowerCase();
    }

    public static String getMd5(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }


    /**
     * 获取手机imei
     */
    public static String getIMEI() {
        try {
            TelephonyManager tm = (TelephonyManager) CGApplication.getInstance()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (stringIsNull(imei)) {
                return getIMEI2();
            } else {
                return imei;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "520";
        }
    }

    /**
     * 如果imei没获取成功 则获取android id
     */
    private static String getIMEI2() {
        try {
            return Settings.Secure.getString(CGApplication.getInstance().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
            return "520";
        }
    }

    public static boolean toSmallPic(Bitmap bitmap) {
        if (bitmap == null || bitmap.getHeight() <= 0 || bitmap.getWidth() <= 0) {
            return false;
        }
        int preW = bitmap.getWidth();
        int preH = bitmap.getHeight();

        float scale = CGApplication.getInstance().getResources().getDimension(R.dimen.head_width_big)
                / Math.min(preW, preH);

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);

        return saveBitmap(resizeBmp);
    }

    public static boolean toMiddlePic(Bitmap bitmap) {
        if (bitmap == null || bitmap.getHeight() <= 0 || bitmap.getWidth() <= 0) {
            return false;
        }
        int preW = bitmap.getWidth();
        int preH = bitmap.getHeight();

        float scale = CGApplication.getInstance().getResources().getDimension(R.dimen.pic_width_big)
                / Math.min(preW, preH);

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);

        return saveBitmap(resizeBmp);
    }

    private static boolean saveBitmap(Bitmap bitmap) {
        try {
            File f = new File(Constants.cameraPicPath);
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream fOut = null;
            fOut = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean isLoginIn() {
        return !(stringIsNull(CGSharedPreference.getLogin()[0])
                || stringIsNull(CGSharedPreference.getLogin()[1]));
    }

    public static void loginOut(Context mContext) {
        Utils.cleanDb();
        CGSharedPreference.cleanSp();//删除本地用户名密码
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public static void cleanDb() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                MessageDao.getInstance().deleteAll();
            }
        }).start();
    }
}
