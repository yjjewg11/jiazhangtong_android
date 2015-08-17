package com.wj.kindergarten.common;

import android.content.SharedPreferences;

import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.utils.Utils;

/**
 * JJGSharedPrefrence
 *
 * @author weiwu.song
 * @data: 2015/5/20 21:58
 * @version: v1.0
 */
public class CGSharedPreference {
    private static final String CG_INFO = "CG";

    private static SharedPreferences getSharedPreferences() {
        return CGApplication.getInstance().getSharedPreferences(CG_INFO, 0);
    }

    private static SharedPreferences getSharedPreferences(String name) {
        return CGApplication.getInstance().getSharedPreferences(name, 0);
    }

    public static void cleanSp() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static void saveLogin(String loginName, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("loginName", loginName);
        editor.putString("loginPwd", password);
        editor.commit();
    }

    public static String[] getLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        String[] loginInfo = new String[2];
        loginInfo[0] = sharedPreferences.getString("loginName", "");
        loginInfo[1] = sharedPreferences.getString("loginPwd", "");
        return loginInfo;
    }

//    public static void saveLoginInfo(LoginModel loginModel) {
//        SharedPreferences sharedPreferences = getSharedPreferences();
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("loginName", loginName);
//        editor.commit();
//    }
//
//    public static LoginModel getLoginInfo() {
//        SharedPreferences sharedPreferences = getSharedPreferences();
//        String[] loginInfo = new String[2];
//        loginInfo[0] = sharedPreferences.getString("loginName", "");
//        loginInfo[1] = sharedPreferences.getString("loginPwd", "");
//        return loginInfo;
//    }

    public static void saveDeviceId(String id) {
        if(!Utils.stringIsNull(id)) {
            SharedPreferences sharedPreferences = getSharedPreferences();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("deviceId", id);
            editor.commit();
        }
    }

    public static String getDeviceId() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        String deviceId = sharedPreferences.getString("deviceId", "");
        return deviceId;
    }

}
