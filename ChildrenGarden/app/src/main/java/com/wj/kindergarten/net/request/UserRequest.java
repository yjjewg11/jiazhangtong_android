package com.wj.kindergarten.net.request;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.loopj.android.http.RequestParams;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.ChildInfo;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.net.RequestHttpUtil;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.RequestType;
import com.wj.kindergarten.net.SendRequest;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.GsonUtil;
import com.wj.kindergarten.utils.Utils;

import org.json.JSONObject;

import java.util.List;

/**
 * RsaResponse
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/5/22 11:20
 */
public final class UserRequest {
    private static final String LOGIN = "rest/userinfo/login.json";
    private static final String REGISTER = "rest/userinfo/reg.json";
    private static final String FORGET_PWD = "rest/userinfo/updatepasswordBySms.json";
    private static final String CHANGE_CHILD = "rest/student/save.json";
    private static final String SMS_CODE = "rest/sms/sendCode.json";
    private static final String INTERACTION_LIST = "rest/classnews/getClassNewsByMy.json";
    private static final String ZAN = "rest/dianzan/save.json";
    private static final String ZAN_CANCEL = "rest/dianzan/delete.json";
    private static final String INTERACTION_SEND = "rest/classnews/save.json";
    private static final String ZAN_LIST = "rest/dianzan/getByNewsuuid.json";//get newsuuid
    private static final String REPLY_LIST = "rest/reply/getReplyByNewsuuid.json";//get newsuuid pageNo
    private static final String REPLY = "rest/reply/save.json";//post content newsuuid uuid type
    private static final String uploadPic = "rest/uploadFile/upload.json";//post JSESSIONID file type 1学生 2老师
    private static final String NOTICE_LIST = "rest/announcements/queryMy.json";
    private static final String NOTICE = "rest/announcements/";
    private static final String SIGN = "rest/studentSignRecord/queryMy.json";
    private static final String COURSE_LIST = "rest/teachingplan/list.json";
    private static final String FOOD_LIST = "rest/cookbookplan/list.json";
    private static final String ARTICLE_LIST = "rest/share/articleList.json";
    private static final String ARTICLE = "rest/share/getArticleJSON.json";
    private static final String APPRAISE_TEACHER_LIST = "rest/teachingjudge/getTeachersAndJudges.json";
    private static final String APPRAISE = "rest/teachingjudge/save.json";
    private static final String UPDATE_PASSWORD = "rest/userinfo/updatepassword.json";
    private static final String DEVICE_SAVE = "rest/pushMsgDevice/save.json";//消息设备注册

    private UserRequest() {
    }

    public static void getAdDetail(Context context, String adId, RequestResultI requestResultI) {
        SendRequest.getInstance().get(context, RequestType.LOGIN, null, RequestHttpUtil.BASE_URL
                + LOGIN + "/" + adId, requestResultI);
    }

    public static void login(Context context, String acc, String pwd, RequestResultI requestResultI) {
        String md5 = Utils.getMd5(pwd);

        RequestParams requestParams = new RequestParams();
        requestParams.put("loginname", acc);
        requestParams.put("password", md5);

        SendRequest.getInstance().post(context, RequestType.LOGIN, requestParams,
                RequestHttpUtil.BASE_URL + LOGIN, requestResultI);
    }

    public static void register(Context context, String acc, String pwd, String fixCode,
                                RequestResultI requestResultI) {
        String md5 = Utils.getMd5(pwd);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tel", acc);
            jsonObject.put("password", md5);
            jsonObject.put("smscode", fixCode);
            jsonObject.put("type", "2");
        } catch (Exception e) {
            e.printStackTrace();
        }


        SendRequest.getInstance().post(context, RequestType.REGISTER, jsonObject.toString(),
                RequestHttpUtil.BASE_URL + REGISTER, requestResultI);
    }

    public static void forgetPwd(Context context, String tel, String pwd, String smsCode, RequestResultI requestResultI) {
        String md5 = Utils.getMd5(pwd);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tel", tel);
            jsonObject.put("password", md5);
            jsonObject.put("smscode", smsCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SendRequest.getInstance().post(context, RequestType.FORGET_PWD, jsonObject.toString(),
                RequestHttpUtil.BASE_URL + FORGET_PWD, requestResultI);
    }

    public static void getSmsCode(Context context, String tel, RequestResultI requestResultI) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tel", tel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SendRequest.getInstance().post(context, RequestType.SMS_CODE, jsonObject.toString(),
                RequestHttpUtil.BASE_URL + SMS_CODE, requestResultI);
    }

    public static void changeChild(Context context, ChildInfo childInfo, RequestResultI requestResultI) {
        String json = GsonUtil.getGson().toJson(childInfo);

        SendRequest.getInstance().post(context, RequestType.CHANGE_CHILD, json,
                RequestHttpUtil.BASE_URL + CHANGE_CHILD, requestResultI);
    }

    public static void getInteractionList(Context context, String newsuuid, int page, RequestResultI requestResultI) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("newsuuid", newsuuid);
        requestParams.put("pageNo", page);
        SendRequest.getInstance().get(context, RequestType.INTERACTION_LIST, requestParams,
                RequestHttpUtil.BASE_URL + INTERACTION_LIST, requestResultI);
    }

    public static void sendInteraction(Context context, String title, String classuuid, String uuid, String content
            , RequestResultI requestResultI) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", title);
            jsonObject.put("classuuid", classuuid);
            jsonObject.put("uuid", uuid);
            jsonObject.put("content", content);
            SendRequest.getInstance().post(context, RequestType.INTERACTION_SEND, jsonObject.toString(),
                    RequestHttpUtil.BASE_URL + INTERACTION_SEND, requestResultI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void zan(Context context, String newsUuid, String type, RequestResultI requestResultI) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("newsuuid", newsUuid);
            jsonObject.put("type", type);
            SendRequest.getInstance().post(context, RequestType.ZAN, jsonObject.toString(),
                    RequestHttpUtil.BASE_URL + ZAN, requestResultI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void zanCancel(Context context, String newsUuid, RequestResultI requestResultI) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("newsuuid", newsUuid);
            SendRequest.getInstance().post(context, RequestType.ZAN_CANCEL, jsonObject.toString(),
                    RequestHttpUtil.BASE_URL + ZAN_CANCEL, requestResultI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getZanList(Context context, String newsuuid, RequestResultI requestResultI) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("newsuuid", newsuuid);
        SendRequest.getInstance().get(context, RequestType.ZAN_LIST, requestParams,
                RequestHttpUtil.BASE_URL + ZAN_LIST, requestResultI);
    }

    public static void getReplyList(Context context, String newsuuid, int pageNo, RequestResultI requestResultI) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("newsuuid", newsuuid);
        requestParams.put("pageNo", pageNo);
        SendRequest.getInstance().get(context, RequestType.REPLY_LIST, requestParams,
                RequestHttpUtil.BASE_URL + REPLY_LIST, requestResultI);
    }

    public static void reply(Context context, String newsuuid, String content, String uuid, int type, RequestResultI requestResultI) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("newsuuid", newsuuid);
            jsonObject.put("content", content);
            jsonObject.put("uuid", uuid);
            jsonObject.put("type", type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SendRequest.getInstance().post(context, RequestType.REPLY, jsonObject.toString(),
                RequestHttpUtil.BASE_URL + REPLY, requestResultI);
    }

    public static void getNoticeList(Context context, String groupuuid, int pageNo, RequestResultI requestResultI) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("groupuuid", groupuuid);
        requestParams.put("pageNo", pageNo);
        SendRequest.getInstance().get(context, RequestType.NOTICE_LIST, requestParams,
                RequestHttpUtil.BASE_URL + NOTICE_LIST, requestResultI);
    }

    public static void getNotice(Context context, String uuid, RequestResultI requestResultI) {
        RequestParams requestParams = new RequestParams();
        SendRequest.getInstance().get(context, RequestType.NOTICE, requestParams,
                RequestHttpUtil.BASE_URL + NOTICE + uuid + ".json", requestResultI);
    }

    public static void getSignList(Context context, String uuid, RequestResultI requestResultI) {
        RequestParams requestParams = new RequestParams();
        SendRequest.getInstance().get(context, RequestType.SIGN, requestParams,
                RequestHttpUtil.BASE_URL + SIGN, requestResultI);
    }

    public static void getCourseList(Context context, String beginDay, String endDay, String classUUID, RequestResultI requestResultI) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("begDateStr", beginDay);
        requestParams.put("endDateStr", endDay);
        requestParams.put("classuuid", classUUID);
        SendRequest.getInstance().get(context, RequestType.COURSE_LIST, requestParams,
                RequestHttpUtil.BASE_URL + COURSE_LIST, requestResultI);
    }

    public static void getFoodList(Context context, String beginDay, String endDay, String groupUUID, RequestResultI requestResultI) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("begDateStr", beginDay);
        requestParams.put("endDateStr", endDay);
        requestParams.put("groupuuid", groupUUID);
        SendRequest.getInstance().get(context, RequestType.FOOD_LIST, requestParams,
                RequestHttpUtil.BASE_URL + FOOD_LIST, requestResultI);
    }

    public static void getArticleList(Context context, int page, RequestResultI requestResultI) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("pageNo", page);
        SendRequest.getInstance().get(context, RequestType.ARTICLE_LIST, requestParams,
                RequestHttpUtil.BASE_URL + ARTICLE_LIST, requestResultI);
    }

    public static void getArticle(Context context, String uuid, RequestResultI requestResultI) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("uuid", uuid);
        SendRequest.getInstance().get(context, RequestType.ARTICLE, requestParams,
                RequestHttpUtil.BASE_URL + ARTICLE, requestResultI);
    }

    public static void getAppraiseTeacherList(Context context, RequestResultI requestResultI) {
        RequestParams requestParams = new RequestParams();
        SendRequest.getInstance().get(context, RequestType.APPRAISE_TEACHER_LIST, requestParams,
                RequestHttpUtil.BASE_URL + APPRAISE_TEACHER_LIST, requestResultI);
    }

    public static void appraiseTeacher(Context context, String content, String teacheruuid, int type, RequestResultI requestResultI) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("teacheruuid", teacheruuid);
            jsonObject.put("content", content);
            jsonObject.put("type", type);
            SendRequest.getInstance().post(context, RequestType.APPRAISE_TEACHER, jsonObject.toString(),
                    RequestHttpUtil.BASE_URL + APPRAISE, requestResultI);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 修改密码
     *
     * @param context
     * @param oldpassowrd
     * @param password
     */
    public static void updatePassword(Context context, String oldpassowrd, String password, RequestResultI resultI) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("oldpassword", Utils.getMd5(oldpassowrd));
            jsonObject.put("password", Utils.getMd5(password));
            SendRequest.getInstance().post(context, RequestType.UPDATE_PASSWORD, jsonObject.toString(), RequestHttpUtil.BASE_URL + UPDATE_PASSWORD, resultI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设备注册
     */
    public static void deviceSave(final Context context) {
        JSONObject jsonObject = new JSONObject();
        try {
            String device_id = CGSharedPreference.getDeviceId();
//            if (Utils.stringIsNull(device_id)) {
//                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//                device_id = tm.getDeviceId();
//            }
            jsonObject.put("device_id", device_id);
            jsonObject.put("device_type", "android");
            jsonObject.put("status", 0);
            SendRequest.getInstance().post(context, RequestType.DEVICE_SAVE, jsonObject.toString(), RequestHttpUtil.BASE_URL + DEVICE_SAVE, new RequestResultI() {
                @Override
                public void result(BaseModel domain) {
                    CGLog.d("device register success");
                }

                @Override
                public void result(List<BaseModel> domains, int total) {

                }

                @Override
                public void failure(String message) {
                    if (!Utils.stringIsNull(message)) {
                        Utils.showToast(context, message);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
