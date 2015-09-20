package com.wj.kindergarten.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.ui.mine.ChooseImage;

public class UserHeadImageUtil {
    private static Context context;
    private static boolean isShow = false;//分享图标是否显示

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_IMAGE_FROM_PICTURES = 2;

    private UserHeadImageUtil() {

    }

    private static UserHeadImageUtil imageUtil = new UserHeadImageUtil();

    public static UserHeadImageUtil getInstance() {
        return imageUtil;
    }

    /**
     * 显示分享对话框
     *
     * @param con
     */
    public static void showChooseImageDialog(Context con, View view, final ChooseImage chooseImage) {
        if (!isShow) {
            isShow = true;
            context = con;
            View popupView = View.inflate(con, R.layout.user_head_image_layout, null);
            final PopupWindow mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            popupView.findViewById(R.id.choose_image_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindow.dismiss();
                    isShow = false;
                }
            });

            //相册
            popupView.findViewById(R.id.choose_form_images).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isShow = false;
                    mPopupWindow.dismiss();
                    chooseImage.chooseImage(CHOOSE_IMAGE_FROM_PICTURES);
                }
            });

            //拍照
            popupView.findViewById(R.id.take_photo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isShow = false;
                    mPopupWindow.dismiss();
                    chooseImage.chooseImage(TAKE_PHOTO);
                }
            });

            mPopupWindow.getContentView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isShow = false;
                    mPopupWindow.dismiss();
                }
            });
            mPopupWindow.setAnimationStyle(R.style.ShareAnimBase);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setTouchable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.getContentView().setFocusableInTouchMode(true);
            mPopupWindow.getContentView().setFocusable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.update();
            mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        }
    }
}
