package com.wj.kindergarten.ui.more;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.More;
import com.wj.kindergarten.ui.webview.WebviewActivity;
import com.wj.kindergarten.utils.CGLog;

import java.util.ArrayList;


/**
 * Created by WW on 2014/6/3.
 */
public class MoreUtil {
    private static Context context;
    private static boolean flag = false;//防止弹窗多次
    private static boolean isShow = false;//分享图标是否显示

    private MoreUtil() {

    }

    private static MoreUtil shareUtils = new MoreUtil();

    public static MoreUtil getInstance() {
        return shareUtils;
    }

    /**
     * 显示分享对话框
     *
     * @param con
     * @param
     */
    public static void more(final Context con, final ArrayList<More> list, final View view) {
        if (!isShow) {
            isShow = true;
            context = con;
            flag = false;

            View popupView = View.inflate(con, R.layout.view_more_layout, null);
            GridView gridView = (GridView) popupView.findViewById(R.id.gv_more);
            MoreAdapter adapter = new MoreAdapter(con, list);
            gridView.setAdapter(adapter);
            final PopupWindow mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    More more = list.get(position);
                    if (null != more) {
                        Intent intent = new Intent(context, WebviewActivity.class);
                        intent.putExtra("title", more.getName());
                        intent.putExtra("url", more.getUrl());
                        context.startActivity(intent);
                        mPopupWindow.dismiss();
                        isShow = false;
                    }
                }
            });
            popupView.findViewById(R.id.share_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindow.dismiss();
                    isShow = false;
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
