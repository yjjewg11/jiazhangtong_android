package com.wj.kindergarten.utils;

import android.content.Context;
import android.text.BoringLayout;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.ui.main.MainActivity;

/**
 * Created by Administrator on 2015/10/30.
 */
public abstract class WindowUtils{
    private static  DisplayMetrics dm = new DisplayMetrics();
    static {
        WindowManager windowManager = (WindowManager) CGApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
    }
    public static float getDesnity(){
        return dm.density;
    }
}
