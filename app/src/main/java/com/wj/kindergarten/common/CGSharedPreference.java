package com.wj.kindergarten.common;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.Group;
import com.wj.kindergarten.bean.MainTopic;
import com.wj.kindergarten.bean.MainTopicSun;
import com.wj.kindergarten.bean.VersionInfo;

import com.wj.kindergarten.utils.Utils;

import java.util.Date;

/**
 * JJGSharedPrefrence
 *
 * @author weiwu.song
 * @data: 2015/5/20 21:58
 * @version: v1.0
 */
public class CGSharedPreference {
    private static final String CG_INFO = "CG";
    private static final String IS_NEED_SHOW = "is_need_show";

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

    public static boolean getIsNeedShow(){
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getBoolean(IS_NEED_SHOW,true);
    }

    public static void setIsNeedShow(){
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_NEED_SHOW,false);
        editor.commit();

    }

    public static String  getStoreJESSIONID() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getString("JESSIONID", "");
    }

    //在获取用户信息时保存的md5
    public static String getJESSIONID_MD5() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getString("JESSIONID_MD5", "");
    }

    public static void setStoreJESSIONID(String JESSIONID) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("JESSIONID",JESSIONID);
        editor.commit();
    }

    //在获取用户信息时保存的md5
    public static void setJESSIONID_MD5(String JESSIONID_MD5) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("JESSIONID_MD5",JESSIONID_MD5);
        editor.commit();
    }

    public static String getConfigMD5() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        String text =  sharedPreferences.getString("config_md5", "");
        return text;
    }

    public static void setConfigMD5(String md5,String mainTopicUrl) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mainTopicUrl",mainTopicUrl);
        editor.putString("config_md5",md5);
        editor.commit();
    }
    public static String getMainTopicUrl(){
        SharedPreferences sharedPreferences = getSharedPreferences();
        String text =  sharedPreferences.getString("mainTopicUrl", "");
        return text;
    }

    public static boolean getEnoughOneDay() {
        Date date = new Date();
        SharedPreferences sharedPreferences = getSharedPreferences();
        long priousTime =  sharedPreferences.getLong("isEnoughOneDay", -1);
        if(date.getTime() - priousTime > 1000 * 60 * 60  * 24 ){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("isEnoughOneDay",date.getTime());
            editor.commit();
            return true;
        }
        return false;
    }

    public static String getMineSchoolAssessState() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getString("mine_school_assess_uuid","");
    }

    public static void setMineSchoolAssessState(String uuid) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mine_school_assess_uuid",uuid);
        editor.commit();
    }

    public static void setMainTopicSun(MainTopicSun sun) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("topic_title",sun.getTitle());
        editor.putString("topic_link_url",sun.getUrl());
        editor.commit();
    }

    public static MainTopicSun getMainTopicSun(){
        MainTopicSun topic = new MainTopicSun();
        SharedPreferences sharedPreferences = getSharedPreferences();
        topic.setTitle(sharedPreferences.getString("topic_title", ""));
        topic.setUrl(sharedPreferences.getString("topic_link_url", ""));
        return topic;
    }

    public static String [] getPfMaxAndMinTime() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        String max =  sharedPreferences.getString("pf_maxtime", null);
        String min =  sharedPreferences.getString("pf_mintime",null);
        return new String[]{max,min};
    }

    public static boolean getUploadSyncStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        boolean upload_sync_status =  sharedPreferences.getBoolean("upload_sync_status", false);
        return upload_sync_status;
    }

    public static void setUploadSyncStatus(boolean uploadSyncStatus){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean("upload_sync_status", uploadSyncStatus);
        editor.commit();
    }

    public  void setPfMaxAndMinTime(String [] times){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString("pf_maxtime",times[0]);
        editor.putString("pf_mintime", times[1]);
        editor.commit();
    }

    public static void setCanReadConstact(boolean open){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean("openreadconstanctpermession", open);
        editor.commit();
    }
    public static boolean getCanReadConstact(){
        SharedPreferences preferences= getSharedPreferences();
        return preferences.getBoolean("openreadconstanctpermession",false);
    }

    public static boolean initReadConstact() {
        SharedPreferences preferences= getSharedPreferences();
        return preferences.getBoolean("initReadConstact",false);
    }
    public static void alreadyInitReadConstact(){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean("initReadConstact", true);
        editor.commit();
    }

    public static void storeAccess_Token(String access_token) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString("login_access_token", access_token);
        editor.commit();
    }
    public static String getAccess_Token() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getString("login_access_token", "");
    }

    public static void storelogin_type(String type) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString("login_type", type);
        editor.commit();
    }


    public static String getlogin_type() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getString("login_type", "");
    }

    public static boolean getLoginOnce() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getBoolean("login_once", false);
    }

    public static void setLoginOnce() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean("login_once", true);
        editor.commit();
    }

    public static void setVideoAccessToken(String accessToken) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString("video_access_token", accessToken);
        editor.commit();
    }

    public static void setVideoAppkey(String appKey) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString("video_app_key", appKey);
        editor.commit();
    }

    public static String getVideoAccessToken() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getString("video_access_token", "");
    }

    public static String getVideoAppkey() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getString("video_app_key", "");
    }
}
