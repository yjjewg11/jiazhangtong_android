package com.wj.kindergarten.common;

import android.content.SharedPreferences;

import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.Group;
import com.wj.kindergarten.bean.VersionInfo;

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

    public static void saveRemerberPassword(boolean r) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("remerberPw", r);
        editor.commit();
    }

    public static boolean getRemerberPassword() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getBoolean("remerberPw", false);
    }

    public static void saveLogin(String loginName, String password, String imgUrl) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("loginName", loginName);
        editor.putString("loginPwd", password);
        editor.putString("imgUrl", imgUrl);
        editor.commit();
    }

    public static String[] getLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        String[] loginInfo = new String[3];
        loginInfo[0] = sharedPreferences.getString("loginName", "");
        loginInfo[1] = sharedPreferences.getString("loginPwd", "");
        loginInfo[2] = sharedPreferences.getString("imgUrl", "");
        return loginInfo;
    }

    public static void saveDeviceId(String id) {
        if (!Utils.stringIsNull(id)) {
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


    public static void saveTitle(Group model) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (null != model) {
            editor.putString("uuid", model.getUuid());
        } else {
            editor.putString("uuid", "");
        }
        editor.commit();
    }

    public static String getTitleUUID() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        String uuid = sharedPreferences.getString("uuid", "");
        return uuid;
    }

    /**
     * 保存推送通知状态
     *
     * @param type
     * @param flag
     */
    public static void saveNoticeState(int type, boolean flag) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (type == 1) {
            editor.putBoolean("newMessage", flag);
        } else if (type == 2) {
            editor.putBoolean("voice", flag);
        } else if (type == 3) {
            editor.putBoolean("zhengdong", flag);
        } else if (type == 4) {
            editor.putBoolean("setting", flag);
        }
        editor.commit();
    }

    public static boolean getNoticeState(int type) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        boolean flag = false;
        if (type == 1) {
            flag = sharedPreferences.getBoolean("newMessage", true);
        } else if (type == 2) {
            flag = sharedPreferences.getBoolean("voice", true);
        } else if (type == 3) {
            flag = sharedPreferences.getBoolean("zhengdong", true);
        } else if (type == 4) {
            flag = sharedPreferences.getBoolean("setting", false);
        }
        return flag;
    }

    public static void setLoginOut(boolean flag) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoginOut", flag);
        editor.commit();
    }

    public static boolean getLoginOut() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getBoolean("isLoginOut", false);
    }

    public static void setMessageState(boolean flag) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("message", flag);
        editor.commit();
    }

    public static boolean getMessageState() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getBoolean("message", false);
    }

    public static VersionInfo getVersionInfoReference(){
        SharedPreferences sharedPreferences = getSharedPreferences();
        String type = sharedPreferences.getString("type", "");
        String mobileVersion = sharedPreferences.getString("mobileVersion","");
        String appVersion = sharedPreferences.getString("appVersion","");
        String city = sharedPreferences.getString("city","");
        VersionInfo versionInfo = new VersionInfo(type,mobileVersion,appVersion,city);
        return versionInfo;
    }

    public static void setVersionInfoReference(VersionInfo versionInfo){
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("type",versionInfo.getType());
        editor.putString("mobileVersion",versionInfo.getMobileVersion());
        editor.putString("appVersion",versionInfo.getAppVersion());
        editor.putString("city",versionInfo.getCity());
        editor.commit();
    }

    public static void setMineCourseIsSendSContent(){
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isSendContent", true);
        editor.commit();
    }

    public static boolean getMineCourseIsSendContent(){
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getBoolean("isSendContent",false);
    }
}
