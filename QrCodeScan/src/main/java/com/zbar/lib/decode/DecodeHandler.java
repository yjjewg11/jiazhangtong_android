package com.zbar.lib.decode;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.zbar.lib.CaptureActivity;
import com.zbar.lib.R;
import com.zbar.lib.ZbarManager;
import com.zbar.lib.bitmap.PlanarYUVLuminanceSource;

/**
 * 作者: 陈涛(1076559197@qq.com)
 * 
 * 时间: 2014年5月9日 下午12:24:13
 * 
 * 版本: V_1.0.0
 * 
 * 描述: 接受消息后解码
 */
public final class DecodeHandler extends Handler {

	CaptureActivity activity = null;
	Context context;
	public static final int DECODE_FROM_PIC = 1030;

	DecodeHandler(CaptureActivity activity) {
		this.activity = activity;
	}

	public DecodeHandler(Context context) {
		this.context = context;
	}

	@Override
	public void handleMessage(Message message) {
		switch (message.what) {
		case CaptureActivityHandler.DECODE:
			decode((byte[]) message.obj, message.arg1, message.arg2);
			break;
		case CaptureActivityHandler.QUIT:
			Looper.myLooper().quit();
			break;
		case DECODE_FROM_PIC:
			decodeFromPic((byte[]) message.obj, message.arg1, message.arg2);
			break;
		}
	}

	//只用于解析二维码
	public void decodeFromPic(byte[] data , int width ,int height){
		//取消横屏换竖屏，因为照相机默认取得横屏的照片，传输来的数据默认为竖屏，不用切换。
		byte[] rotatedData = new byte[data.length];
//		for (int y = 0; y < height; y++) {
//			for (int x = 0; x < width; x++)
//				rotatedData[x * height + height - y - 1] = data[x + y * width];
//		}
//		int tmp = width;// Here we are swapping, that's the difference to #11
//		width = height;
//		height = tmp;

		ZbarManager manager = new ZbarManager();
		String result = manager.decode(rotatedData, width, height, false, width, height, 0,
				0);
		if(result != null){
			Intent intent = new Intent("receiver_two_code_url");
			intent.putExtra("url",result);
			context.sendBroadcast(intent);
		}else{
			Toast.makeText(context,"解析二维码失败!",Toast.LENGTH_SHORT).show();
		}
	}


	private void decode(byte[] data, int width, int height) {
		byte[] rotatedData = new byte[data.length];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++)
				rotatedData[x * height + height - y - 1] = data[x + y * width];
		}
		int tmp = width;// Here we are swapping, that's the difference to #11
		width = height;
		height = tmp;

		ZbarManager manager = new ZbarManager();
		String result = manager.decode(rotatedData, width, height, true, activity.getX(), activity.getY(), activity.getCropWidth(),
				activity.getCropHeight());

		if (result != null) {
			if (activity.isNeedCapture()) {
				// 生成bitmap
				PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(rotatedData, width, height, activity.getX(), activity.getY(),
						activity.getCropWidth(), activity.getCropHeight(), false);
				int[] pixels = source.renderThumbnail();
				int w = source.getThumbnailWidth();
				int h = source.getThumbnailHeight();
				Bitmap bitmap = Bitmap.createBitmap(pixels, 0, w, w, h, Bitmap.Config.ARGB_8888);
				try {
					String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Qrcode/";
					File root = new File(rootPath);
					if (!root.exists()) {
						root.mkdirs();
					}
					File f = new File(rootPath + "Qrcode.jpg");
					if (f.exists()) {
						f.delete();
					}
					f.createNewFile();

					FileOutputStream out = new FileOutputStream(f);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
					out.flush();
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (null != activity.getHandler()) {
				Message msg = new Message();
				msg.obj = result;
				msg.what = CaptureActivityHandler.DECODE_SUCCESS;
				activity.getHandler().sendMessage(msg);
			}
		} else {
			if (null != activity.getHandler()) {
				activity.getHandler().sendEmptyMessage(CaptureActivityHandler.DECODE_FAILED);
			}
		}
	}

}
