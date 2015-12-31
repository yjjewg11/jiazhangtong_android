package com.wj.kindergarten.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

import com.wj.kindergarten.ui.BaseActivity;
import com.wj.kindergarten.ui.mine.EditChildActivity;

public class FileUtil {


    /**
     * 将Bitmap 图片保存到本地路径，并返回路径
     *
     * @param c
     * @param fileName 文件名称
     * @param bitmap   图片
     * @return
     */
    public static String saveFile(Context c, String fileName, Bitmap bitmap) {
        return saveFile(c, "", fileName, bitmap);
    }

    public static String saveFile(Context c, String filePath, String fileName, Bitmap bitmap) {
        byte[] bytes = bitmapToBytes(bitmap);
        return saveFile(c, filePath, fileName, bytes);
    }

    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static String saveFile(Context c, String filePath, String fileName, byte[] bytes) {
        String fileFullName = "";
        FileOutputStream fos = null;
        String dateFolder = new SimpleDateFormat("yyyyMMdd", Locale.CHINA)
                .format(new Date());
        try {
            String suffix = "";
            if (filePath == null || filePath.trim().length() == 0) {
                filePath = Environment.getExternalStorageDirectory() + "/JiaXT/" + dateFolder + "/";
            }
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File fullFile = new File(filePath, fileName + suffix);
            fileFullName = fullFile.getPath();
            fos = new FileOutputStream(new File(filePath, fileName + suffix));
            fos.write(bytes);
        } catch (Exception e) {
            fileFullName = "";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    fileFullName = "";
                }
            }
        }
        return fileFullName;
    }


    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param file 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public static void deleteFolder(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteFolder(f);
            }
            file.delete();
        }
    }


//    此方法得到一个bitmap对象，返回一个base64字符串
    public static String getBase64FromBitmap( Bitmap thePic){
        String  pictureBytes = null;
        ByteArrayOutputStream out = null;
        byte[] bytes = null;
        try {
            int quality = 80;

            out = new ByteArrayOutputStream();
            do {
                thePic.compress(Bitmap.CompressFormat.JPEG, quality, out);
                bytes = out.toByteArray();
                quality-=10;
                if(quality==0) break;
                out.reset();
            }while (bytes.length / 1024 > 100);
            try {
                if(out!=null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            pictureBytes = Base64.encodeToString(bytes, Base64.NO_WRAP);


        }catch (Exception e){
            e.printStackTrace();
        }
        return pictureBytes;
    }

    //把图片保存到图库中，并且通知更新
    public static void saveImageToGallery(String fileName, Context context, Bitmap bmp) {
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

}
