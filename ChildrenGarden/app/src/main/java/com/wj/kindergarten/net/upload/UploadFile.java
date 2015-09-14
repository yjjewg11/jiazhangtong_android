package com.wj.kindergarten.net.upload;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import android.os.Environment;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.BaseResponse;
import com.wj.kindergarten.net.RequestHttpUtil;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.GsonUtil;
import com.wj.kindergarten.utils.Utils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by zoupengqiang on 14-5-26.
 * 图片上传
 */
public class UploadFile {
    public static final String URL = RequestHttpUtil.BASE_URL + "rest/uploadFile/upload.json";
    private UploadImage uploadImage;
    private Context context;
    private int type;
    private static final String HTTP_SUCCESS = "success";
    private int width = 0;
    private int height = 0;

    public UploadFile(Context context, UploadImage uploadImage, int type, int width, int height) {
        this.uploadImage = uploadImage;
        this.context = context;
        this.type = type;
        this.width = width;
        this.height = height;
    }

    /**
     * 上传
     */
    public void upload(String path) {
        if (Utils.isNetworkAvailable(context)) {
            try {
                File file = new File(path);
                if (isompress(path)) {
                    Bitmap bitmap = compressBySize(path, width, height);
                    file = saveFile(bitmap, 70);
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                }
                uploadImg(file, path);
            } catch (Exception e) {
                e.printStackTrace();
                uploadImage.failure("处理图片失败");
            }
        } else {
            uploadImage.failure("网络连接已断开或不可用");
        }
    }

    private void uploadImg(final File file, final String path) {
        if (file != null && file.exists() && file.length() > 0) {
            try {
                RequestParams params = new RequestParams();
                params.put("file", file);
                params.put("JSESSIONID", CGApplication.getInstance().getLogin().getJSESSIONID());
                params.put("type", type);
                CGLog.d(URL + "?" + params.toString());
                RequestHttpUtil.post(context, URL, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] header, JSONObject response) {
                        super.onSuccess(statusCode, header, response);
                        CGLog.d("back1:" + response.toString());
                        try {
                            BaseResponse baseResponse = new BaseResponse(response);
                            if (HTTP_SUCCESS.equals(baseResponse.getResMsg().getStatus())) {
                                uploadImage.success(GsonUtil.getGson().fromJson(response.toString(), Result.class));
                            } else {
                                uploadImage.failure("上传图片失败");
                            }
                        } catch (Exception e) {
                            uploadImage.failure("上传图片不成功");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String response) {
                        super.onSuccess(statusCode, headers, response);
                        CGLog.d("back2:" + response.toString());
                        uploadImage.failure("上传图片失败");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        CGLog.d("back3:" + response.toString());
                        uploadImage.failure("上传图片失败");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          String responseBody, Throwable e) {
                        super.onFailure(statusCode, headers, responseBody, e);
                        uploadImage.failure("上传图片失败");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                uploadImage.failure("上传图片失败");
            }
        } else {
            uploadImage.failure("上传的文件不存在");
        }
    }

    /**
     * 是否需要压缩图片
     *
     * @param path
     * @return
     * @throws Exception
     */

    private boolean isompress(String path) throws Exception {
        if (null != path && !path.equals("")) {
            //   Bitmap bmp = BitmapFactory.decodeFile(path);
            File file = new File(path);
            FileInputStream fs = new FileInputStream(file);
            int len = fs.available() / 1024;
//            if (null != bmp && Build.VERSION.SDK_INT > 12) {
//                CGLog.d("压缩前：" + "大小" + len + "K，" + "宽：" + bmp.getWidth() + "，高：" + bmp.getHeight());
//            } else {
//                if (null != file) {
//                    CGLog.d("压缩前：" + "大小" + len + "K，" + "宽：" + bmp.getWidth() + "，高：" + bmp.getHeight());
//                }
//            }

            if (len < 100) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    //压缩图片质量
    private Bitmap compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 150) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap2 = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap2;
    }


    //压缩图片尺寸
    public Bitmap compressBySize(String pathName, int targetWidth, int targetHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
        // 得到图片的宽度、高度；
        float imgWidth = opts.outWidth;
        float imgHeight = opts.outHeight;

        CGLog.d("W:" + opts.outWidth);
        CGLog.d("H:" + opts.outHeight);

        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
        if (targetWidth > 0 || targetHeight > 0) {
            int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
            int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
            opts.inSampleSize = 1;
            if (widthRatio > 1 || heightRatio > 1) {
                if (widthRatio > heightRatio) {
                    opts.inSampleSize = widthRatio;
                } else {
                    opts.inSampleSize = heightRatio;
                }
            }
        }
        //设置好缩放比例后，加载图片进内容；
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(pathName, opts);
        return bitmap;
    }


    //存储进SD卡
    public File saveFile(Bitmap bm, int size) throws Exception {
        String tempPath = Environment.getExternalStorageDirectory() + File.separator + "jiazhangtong_temp.jpg";
        File myCaptureFile = new File(tempPath);

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        //size 100表示不进行压缩，70表示压缩率为30%
        bm.compress(Bitmap.CompressFormat.JPEG, size, bos);
        FileInputStream fs = new FileInputStream(myCaptureFile);
        int len = fs.available() / 1024;
        if (null != bm && Build.VERSION.SDK_INT >= 12) {
            CGLog.d("压缩后：" + "大小" + len + "K，" + "宽：" + bm.getWidth() + "，高：" + bm.getHeight());
        } else {
            CGLog.d("压缩后：" + "大小" + len + "K，" + "宽：" + bm.getWidth() + "，高：" + bm.getHeight());
        }
        bos.flush();
        bos.close();
        return myCaptureFile;
    }
}
