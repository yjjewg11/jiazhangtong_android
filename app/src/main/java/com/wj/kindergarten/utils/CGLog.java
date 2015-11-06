package com.wj.kindergarten.utils;

import android.util.Log;

/**
 * JJGLog for print log message
 *
 * @author weiwu.song
 * @data: 2015/5/21
 * @version: v1.0
 */
public final class CGLog {
    public static boolean isDebug = true;//is print log
    public static final String TAG = "CGLog";//log tag

    /**
     * util class avoid to instantiate
     */
    private CGLog() {
    }

    public static void v(String tag, String log) {
        if (isDebug) {
            if (Utils.stringIsNull(tag)) {
                Log.v(TAG, log);
            } else {
                Log.v(tag, log);
            }
        }
    }

    public static void v(String log) {
        v("", log);
    }

    public static void i(String tag, String log) {
        if (isDebug) {
            if (Utils.stringIsNull(tag)) {
                Log.i(TAG, log);
            } else {
                Log.i(tag, log);
            }
        }
    }

    public static void i(String log) {
        i("", log);
    }

    public static void d(String tag, String log) {
        if (isDebug) {
            if (Utils.stringIsNull(tag)) {
                Log.d(TAG, log);
            } else {
                Log.d(tag, log);
            }
        }
    }

    public static void d(String log) {
        d("", log);
    }

    public static void w(String tag, String log) {
        if (isDebug) {
            if (Utils.stringIsNull(tag)) {
                Log.w(TAG, log);
            } else {
                Log.w(tag, log);
            }
        }
    }

    public static void w(String log) {
        w("", log);
    }

    public static void e(String tag, String log) {
        if (isDebug) {
            if (Utils.stringIsNull(tag)) {
                Log.e(TAG, log);
            } else {
                Log.e(tag, log);
            }
        }
    }

    public static void e(String log) {
        e("", log);
    }
}
