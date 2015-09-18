package com.wj.kindergarten.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.wenjie.jiazhangtong.R;


/**
 * PopWindowUtil
 *
 * @author weiwu.song
 * @data: 2015/5/21
 * @version: v1.0
 */
public class PopWindowUtil {
    public static int popWindowPadding = 32;

    public static PopupWindow showPopWindow(boolean clickOutside, final Context context,
                                            final View parentView, final int layoutId,
                                            int topBottomMargin, final PopWindowInterface popWindowInterface) {
        popWindowPadding = context.getResources().getDimensionPixelSize(R.dimen.popwindow_padding_lr);
        LinearLayout popupView = null;
        popupView = (LinearLayout) View.inflate(context, R.layout.popwindow_base_layout, null);
        final PopupWindow mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        View popupContentV = null;
        popupContentV = View.inflate(context, layoutId, null);
        LinearLayout.LayoutParams layoutParams = null;
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = popWindowPadding;
        layoutParams.rightMargin = popWindowPadding;
        layoutParams.gravity = Gravity.CENTER;
        if (topBottomMargin > 0) {
            layoutParams.bottomMargin = topBottomMargin;
            layoutParams.topMargin = topBottomMargin;
        }
        popupView.addView(popupContentV, layoutParams);

        popupContentV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        popWindowInterface.setViewActions(mPopupWindow, popupView);
        mPopupWindow.setAnimationStyle(R.style.PopWindowAnim);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);//outside hide
        mPopupWindow.getContentView().setFocusableInTouchMode(true);
        mPopupWindow.getContentView().setFocusable(true);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (clickOutside) {
            mPopupWindow.getContentView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPopupWindow.dismiss();
                }
            });
        }
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.update();
        mPopupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });

        return mPopupWindow;
    }

    public static PopupWindow showPopWindow(boolean clickOutside, final Context context,
                                            final View parentView, final int layoutId,
                                            final PopWindowInterface popWindowInterface) {
        return showPopWindow(clickOutside, context, parentView, layoutId, -1, popWindowInterface);
    }

    public interface PopWindowInterface {
        public void setViewActions(PopupWindow popupWindow, View popupView);
    }
}
