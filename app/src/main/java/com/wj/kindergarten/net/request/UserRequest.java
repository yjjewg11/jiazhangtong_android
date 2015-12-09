package com.wj.kindergarten.net.request;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;


import com.loopj.android.http.RequestParams;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.IOStoreData.StoreDataInSerialize;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.ChildInfo;
import com.wj.kindergarten.bean.Login;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.handler.GlobalHandler;
import com.wj.kindergarten.net.RequestHttpUtil;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.RequestType;
import com.wj.kindergarten.net.SendRequest;
import com.wj.kindergarten.ui.SplashActivity;
import com.wj.kindergarten.ui.func.CourseInteractionListActivity;
import com.wj.kindergarten.ui.func.NormalReplyListActivity;
import com.wj.kindergarten.ui.func.SchoolHtmlActivity;
import com.wj.kindergarten.ui.func.TeacherDetailInfoActivity;
import com.wj.kindergarten.ui.main.MainActivity;
import com.wj.kindergarten.ui.mine.PrivilegeActiveActivity;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.GsonUtil;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

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
    private static final String FORGET_PWD = "rest/userinfo/updatePasswordBySms.json";
    private static final String CHANGE_CHILD = "rest/student/save.json";
    private static final String SMS_CODE = "rest/sms/sendCode.json";
    private static final String INTERACTION_LIST = "rest/classnews/getClassNewsByMy.json";
    private static final String ZAN = "rest/dianzan/save.json";
    private static final String ZAN_CANCEL = "rest/dianzan/delete.json";
    private static final String INTERACTION_SEND = "rest/classnews/save.json";
    private static final String ZAN_LIST = "rest/dianzan/getByNewsuuid.json";//get newsuuid
    private static final String REPLY_LIST = "rest/reply/getReplyByNewsuuid.json";//get newsuuid pageNo
    private static final String REPLY = "rest/reply/save.json";//post content newsuuid uuid type
    private static final String NOTICE_LIST = "rest/announcements/queryMy.json";
    private static final String NOTICE = "rest/announcements/";
    private static final String SIGN = "rest/studentSignRecord/queryMy.json";
    private static final String COURSE_LIST = "rest/teachingplan/list.json";
    private static final String FOOD_LIST = "rest/cookbookplan/list.json";
    private static final String ARTICLE_LIST = "rest/share/articleList.json";
    private static final String ARTICLE = "rest/share/getArticleUrlJSON.json";
    private static final String APPRAISE_TEACHER_LIST = "rest/teachingjudge/getTeachersAndJudges.json";
    private static final String APPRAISE = "rest/teachingjudge/save.json";
    private static final String UPDATE_PASSWORD = "rest/userinfo/updatepassword.json";
    private static final String DEVICE_SAVE = "rest/pushMsgDevice/save.json";//消息设备注册
    private static final String QUERY_MESSAGE = "rest/pushMessage/queryMy.json";//请求我的消息列表
    private static final String QUERY_MORE = "rest/userinfo/getDynamicMenu.json";//请求首页more
    private static final String QUERY_SCHOOL = "rest/group/list.json";//请求学校列表
    private static final String QUERY_STORE = "rest/favorites/query.json";//请求收藏列表
    private static final String STORE = "rest/favorites/save.json";//收藏
    private static final String CANCEL_STORE = "rest/favorites/delete.json";//取消收藏
    private static final String QUERY_TEACHER_INFO = "rest/userinfo/getTeacherInfo.json";//获取老师的信息
    private static final String LOGINOUT = "rest/userinfo/logout.json";//退出登录
    private static final String KAURL = "rest/studentbind/query.json";//学生卡号信息
    private static final String READ_MESSAGE = "rest/pushMessage/read.json";//阅读消息
    private static final String SPECIAL_COURSE_TYPE = "rest/share/getCourseType.json";

    //培训相关的接口
    private static final String TRAING_CHILD = "rest/pxstudent/listByMy.json";
    //获取所有孩子的班级
    private static final String TRAING_CLASSES = "rest/pxclass/listByStudent.json";

    private static final String SPECIAL_COURSE_INFO = "rest/pxteachingplan/list.json";

    //获取培训课程信息

    private static final String TRAING_COURSE_OF_CLASS =  "rest/pxCourse/queryByPage.json";

    //获取热门课程

    private static final String TRAIN_HOT_CLASS =  "rest/pxCourse/hotByPage.json";
    private static String groupUuid;
    private static String ONCE_COURSE_CLICK = "rest/pxCourse/get2.json";
    private static final String ALL_TRAINC_SCHOOL = "rest/group/pxlistByPage.json";
    private static final String MORE_DISCUSS_FROM_UUID = "rest/appraise/queryByPage.json";
    //查询下一次的课程的接口
    private static final String NEXT_CLASS_INFO = "rest/pxteachingplan/nextList.json";
    private static final String SAVE_ASSESS = "rest/appraise/save.json";

    private static final String TEACHER_COUNT = "rest/pxteacher/queryByPage.json";
    private static final String STUDY_STATE = "rest/pxclass/listMyChildClassByPage.json";
    private static final String MINE_ALL_COURSE = "rest/pxteachingplan/listAllByclassuuid.json";
    private static final String ALL_TEACHER = "rest/pxclass/listclassTeacher.json";
    private static final String GET_ASSESS_STATE = "rest/appraise/queryMyByPage.json";
    private static final String SEND_VERSION = "rest/userinfo/saveParentData.json";
    private static final String COURSE_INTERACTION_LIST = "rest/classnews/queryPxClassNewsBy.json";
    private static final String GET_PRIVELEGE_ACTIVE = "rest/share/pxbenefitList.json";
    private static final String PRIVELIGE_ACTIVE = "rest/share/getPxbenefitJSON.json";
    private static final String CALL_MESSAGE_SATTE = "rest/pxTelConsultation/save.json";
    private static final String TRAIN_SCHOOL_DETAIL = "rest/group/get2.json";
    private static final String OTHER_ALL_TRAINC_SCHOOL = "rest/group/kdlistByPage.json";
    private static final String  TRAIN_SCHOOL_DETAIL_FROM_RECRUIT = "rest/group/getKD.json";
    private static final String GET_USER_INFO = "rest/userinfo/getUserinfo.json";
    private static final String GET_TOPIC_CONFIG = "rest/share/getConfig.json";
    private static final String  GET_MAIN_TOPIC = "rest/userinfo/getMainTopic.json";
    private static final String CLICK_AND_REFRESH_TOPIC = "rest/userinfo/getMainTopic_cb.json";
    private static final String GET_SCHOOL_ASSESS_STATE = "rest/appraise/queryMyKDByPage.json";
    private static final String FOUND_TYPE_COUNT = "rest/userinfo/getNewMsgNumber.json";
    private static final String FOUND_HOT_SELECTION = "rest/snsTopic/hotByPage.json";

    private UserRequest() {
    }

    public static void getTrainingCourseOfChildren(Context context,RequestResultI resultI){
        RequestParams requestParams = new RequestParams();
        SendRequest.getInstance().get(context,RequestType.NEXT_CLASS_INFO,requestParams,
                RequestHttpUtil.BASE_URL+NEXT_CLASS_INFO,resultI);
    }

    public static void getChildrenClassInfo(Context context,RequestResultI resultI){
        RequestParams requestParams = new RequestParams();
//        requestParams.put("JSESSIONID",CGApplication.getInstance().getLogin().getJSESSIONID());
        SendRequest.getInstance().get(context, RequestType.TRAIN_CLASS, requestParams,
                RequestHttpUtil.BASE_URL + TRAING_CHILD, resultI);
    }

    public static void getTrainChild(Context context,RequestResultI resultI){
        RequestParams requestParams = new RequestParams();
//        requestParams.put("JSESSIONID",CGApplication.getInstance().getLogin().getJSESSIONID());
        SendRequest.getInstance().get(context, RequestType.TRAIN_CHLID_INFO, requestParams,
                RequestHttpUtil.BASE_URL + TRAING_CHILD, resultI);
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
        requestParams.put("ver", Utils.getVersion(context));

        SendRequest.getInstance().post(context, RequestType.LOGIN, requestParams,
                RequestHttpUtil.BASE_URL + LOGIN, requestResultI);
    }

    public static void login2(final Context context, String acc, String pwd) {
        String md5 = Utils.getMd5(pwd);

        RequestParams requestParams = new RequestParams();
        requestParams.put("loginname", acc);
        requestParams.put("password", md5);
        requestParams.put("ver", Utils.getVersion(context));

        SendRequest.getInstance().post(context, RequestType.LOGIN, requestParams,
                RequestHttpUtil.BASE_URL + LOGIN, new RequestResultI() {
                    @Override
                    public void result(BaseModel domain) {
                        CGApplication.getInstance().setLogin((Login) domain);
                        if (CGSharedPreference.getNoticeState(1)) {
                            UserRequest.deviceSave(context, 0);//注册设备
                        } else {
                            UserRequest.deviceSave(context, 2);//注册设备
                        }
                        GlobalHandler.getHandler().sendEmptyMessage(1011);
                    }

                    @Override
                    public void result(List<BaseModel> domains, int total) {

                    }

                    @Override
                    public void failure(String message) {
                        GlobalHandler.getHandler().sendEmptyMessage(1010);
                    }
                });
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

    public static void getSmsCode(Context context, String tel, int type, RequestResultI requestResultI) {
        RequestParams params = new RequestParams();
        params.put("tel", tel);
        params.put("type", type);

        SendRequest.getInstance().get(context, RequestType.SMS_CODE, params,
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
    public static void getCourseInteractionList(Activity activity, String newsuuid, int page, RequestResultI requestResultI) {
        String groupuuid111 = null;
        String courseuuid222 = null;
        if(activity instanceof CourseInteractionListActivity){
            CourseInteractionListActivity cis = (CourseInteractionListActivity) activity;
            int type = cis.getType();
            if(type == NormalReplyListActivity.TRAIN_SCHOOL){
                groupuuid111 = newsuuid;
            }else{
                courseuuid222 = newsuuid;
            }
        }


        RequestParams requestParams = new RequestParams();
        requestParams.put("groupuuid", groupuuid111);
        requestParams.put("courseuuid", courseuuid222);
        requestParams.put("pageNo", page);
        SendRequest.getInstance().get(activity, RequestType.INTERACTION_LIST, requestParams,
                RequestHttpUtil.BASE_URL + COURSE_INTERACTION_LIST, requestResultI);
    }
    public static void sendInteraction(Context context, String title, String classuuid, String uuid,
                                       String content, String imgs, RequestResultI requestResultI) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", title);
            jsonObject.put("classuuid", classuuid);
            jsonObject.put("uuid", uuid);
            jsonObject.put("content", content);
            jsonObject.put("imgs", imgs);
            SendRequest.getInstance().post(context, RequestType.INTERACTION_SEND, jsonObject.toString(),
                    RequestHttpUtil.BASE_URL + INTERACTION_SEND, requestResultI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 点赞
     *
     * @param context
     * @param newsUuid
     * @param type
     * @param requestResultI
     */
    public static void zan(Context context, String newsUuid, int type, RequestResultI requestResultI) {
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

    //取消点赞
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

    public static void getSignList(Context context, String uuid,int pageNo, RequestResultI requestResultI) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("pageNo",pageNo);

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

    private static String testURL = "http://192.168.0.115:8080/px-mobile/rest/pxteachingplan/list.json";
    public static void getTrainCourseList(Context context, String beginDay, String endDay, String classUUID, RequestResultI requestResultI){
        RequestParams requestParams = new RequestParams();
        requestParams.put("begDateStr", beginDay);
        requestParams.put("endDateStr", endDay);
        requestParams.put("classuuid", classUUID);
        SendRequest.getInstance().get(context, RequestType.TRAIN_COURSE_LIST, requestParams,
                testURL, requestResultI);
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

    public static void getPrivilegeActive(Context context, String uuid, RequestResultI requestResultI) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("uuid", uuid);
        SendRequest.getInstance().get(context, RequestType.ARTICLE, requestParams,
                RequestHttpUtil.BASE_URL + PRIVELIGE_ACTIVE, requestResultI);
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
    public static void deviceSave(final Context context, int type) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_id", CGSharedPreference.getDeviceId());
            jsonObject.put("device_type", "android");
            jsonObject.put("status", type);
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

    /**
     * 请求消息列表
     *
     * @param context
     * @param pageNo
     * @param resultI
     */
    public static void queryMessage(Context context, int pageNo, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        params.put("pageNo", pageNo);
        SendRequest.getInstance().get(context, RequestType.QUERY_MESSAGE, params, RequestHttpUtil.BASE_URL + QUERY_MESSAGE, resultI);
    }

    public static void querySchool(Context context, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        SendRequest.getInstance().get(context, RequestType.QUERY_SCHOOL, params, RequestHttpUtil.BASE_URL + QUERY_SCHOOL, resultI);
    }

    public static void queryMore(Context context, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        SendRequest.getInstance().get(context, RequestType.QUERY_MORE, params, RequestHttpUtil.BASE_URL + QUERY_MORE, resultI);
    }

    /**
     * 收藏列表
     */
    public static void queryStore(Context context, int pageNo, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        params.put("pageNo", pageNo);
        SendRequest.getInstance().get(context, RequestType.QUERY_STORE, params, RequestHttpUtil.BASE_URL + QUERY_STORE, resultI);
    }

    //收藏
    public static void store(Context context, String title, int type, String reluuid, String url, RequestResultI resultI) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", title);
            jsonObject.put("type", type);
            jsonObject.put("uuid", reluuid);
            jsonObject.put("reluuid", reluuid);
            jsonObject.put("createtime", TimeUtil.getNowDate());
            SendRequest.getInstance().post(context, RequestType.STORE, jsonObject.toString(), RequestHttpUtil.BASE_URL + STORE, resultI);
        } catch (Exception e) {

        }
    }

    //取消收藏
    public static void cancelStore(boolean fromStore, Context context, String uuid, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        try {
            params.put("reluuid", uuid);
            SendRequest.getInstance().post(context, RequestType.CANCEL_STORE, params, RequestHttpUtil.BASE_URL + CANCEL_STORE, resultI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void queryTeacherInfo(Context context, String uuid, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        params.put("uuid", uuid);
        SendRequest.getInstance().get(context, RequestType.QUERY_TEACHER_INFO, params, RequestHttpUtil.BASE_URL + QUERY_TEACHER_INFO, resultI);
    }

    public static void loginOut(Context context, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        SendRequest.getInstance().post(context, RequestType.LOGIN_OUT, params, RequestHttpUtil.BASE_URL + LOGINOUT, resultI);
    }

    public static void queryKaInfo(Context context, String uuid, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        params.put("studentuuid", uuid);
        SendRequest.getInstance().get(context, RequestType.KA_INFO, params, RequestHttpUtil.BASE_URL + KAURL, resultI);
    }

    public static void readMessage(Context context, String uuid, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        params.put("uuid", uuid);
        SendRequest.getInstance().post(context, RequestType.READ_MESSAGE, params, RequestHttpUtil.BASE_URL + READ_MESSAGE, resultI);
    }

    public static void getSpecialCourseType(Context context,RequestResultI resultI) {
        //获取特长课程的分类信息
        RequestParams params = new RequestParams();
        SendRequest.getInstance().get(context, RequestType.SPECIAL_COURSE_TYPE, params, RequestHttpUtil.BASE_URL + SPECIAL_COURSE_TYPE, resultI);
    }

    //根据不同类型或者不同机构获取列表课程信息
    public static void getSpecialCourseInfoFormType(Context context,String groupuuid,int pageNo,int type,String sort,String teacheruuid, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        params.put("groupuuid",groupuuid);
        params.put("pageNo",pageNo);
        params.put("teacheruuid",teacheruuid);
        if(type != -1){params.put("type",type);}
        if(CGApplication.latitude > 0){
            params.put("map_point",CGApplication.longitude+","+CGApplication.latitude);
        }
        params.put("sort",sort);
        SendRequest.getInstance().get(context,RequestType.SPECIAL_COURSE_INFO,params,RequestHttpUtil.BASE_URL+TRAING_COURSE_OF_CLASS,resultI);
    }

    //获取热门课程
    public static void getHotCourseInfo(Context context,String groupuuid,int pageNo,int type,String sort,String teacheruuid, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        params.put("groupuuid",groupuuid);
        params.put("pageNo",pageNo);
        params.put("teacheruuid",teacheruuid);
        if(type != -1){params.put("type",type);}
        if(CGApplication.latitude > 0){
            params.put("map_point",CGApplication.longitude+","+CGApplication.latitude);
        }
        params.put("sort",sort);
        SendRequest.getInstance().get(context,RequestType.SPECIAL_COURSE_INFO,params,RequestHttpUtil.BASE_URL+TRAIN_HOT_CLASS,resultI);
    }

    //点击课程之后获取该课程的详细信息
    public static void getSpecialCourseINfoFromClickItem(Context context, String uuid, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        params.put("uuid",uuid);
        SendRequest.getInstance().get(context,RequestType.ONCE_COURSE_CLICK,params,RequestHttpUtil.BASE_URL+ONCE_COURSE_CLICK,resultI);
    }

    public static void getAllSchool(Context context, int pageNo,String sort, int type,RequestResultI resultI) {
        RequestParams params = new RequestParams();
        params.put("pageNo",pageNo);
        if(CGApplication.latitude > 0){
            params.put("map_point",CGApplication.longitude+","+CGApplication.latitude);
        }
        params.put("sort",sort);
        if(type != -1){
            params.put("type",type);
        }

        SendRequest.getInstance().get(context,RequestType.ALL_TRAINC_SCHOOL,params,RequestHttpUtil.BASE_URL+ALL_TRAINC_SCHOOL,resultI);
    }

    public static void getMoreDiscuss(Context context, String ext_uuid, int pageNo, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        params.put("ext_uuid",ext_uuid);
        params.put("pageNo",pageNo);
        SendRequest.getInstance().get(context,RequestType.MORE_DISCUSS_FROM_UUID,params,RequestHttpUtil.BASE_URL+MORE_DISCUSS_FROM_UUID,resultI);
    }

    public static void sendSpecialCourseAssess(Context context,String uuid,String ext_uuid,String class_uuid, int type, int score, String content, int niming,RequestResultI resultI) {
    JSONObject object = new JSONObject();
        try{
            object.put("uuid",uuid);
            object.put("ext_uuid",ext_uuid);
            object.put("class_uuid",class_uuid);
            object.put("type",type);
            object.put("score",score*10);
            object.put("content",content);
            object.put("anonymous",niming);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        SendRequest.getInstance().post(context, RequestType.INTERACTION_SEND, object.toString(), RequestHttpUtil.BASE_URL + SAVE_ASSESS, resultI);
    }

    public static void getTeacherFromUuid(Context context, String groupuuid,int pageNo, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        params.put("groupuuid",groupuuid);
        params.put("pageNo",pageNo);
        SendRequest.getInstance().get(context, RequestType.TEACHER_COUNT, params, RequestHttpUtil.BASE_URL + TEACHER_COUNT, resultI);
    }

    public static void getStudyStatus(Context context, int pageNo, int isStudying, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        params.put("pageNo",pageNo);
        params.put("isdisable", isStudying);
                SendRequest.getInstance().get(context,RequestType.STUDY_STATE,params,RequestHttpUtil.BASE_URL+STUDY_STATE,resultI);
    }

    public static void getMineAllCourse(Context context, int pageNo, String classuuid, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        params.put("pageNo",pageNo);
        params.put("classuuid",classuuid);
                SendRequest.getInstance().get(context,RequestType.MINE_ALL_COURSE,params,RequestHttpUtil.BASE_URL+MINE_ALL_COURSE,resultI);

    }

    public static void getTrainSchoolDetail(Context context,String uuid,RequestResultI resultI){
        RequestParams params = new RequestParams();
        params.put("uuid", uuid);
        SendRequest.getInstance().get(context,RequestType.TRAIN_SCHOOL_DETAIL,params,RequestHttpUtil.BASE_URL+TRAIN_SCHOOL_DETAIL,resultI);
    }

    public static void getAllAssessTeacher(Context context, String courseuuid, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        params.put("classuuid",courseuuid);
        SendRequest.getInstance().get(context,RequestType.ALL_TEACHER,params,RequestHttpUtil.BASE_URL+ALL_TEACHER,resultI);
    }

    public static void getAssessState(Context context, String courseuuid, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        params.put("class_uuid",courseuuid);
        params.put("pageNo",1);
        SendRequest.getInstance().get(context,RequestType.GET_ASSESS_STATE,params,RequestHttpUtil.BASE_URL+GET_ASSESS_STATE,resultI);
    }

    public static void getSchoolAssessState(Context context, String groupuuid, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        params.put("groupuuid",groupuuid);
        params.put("pageNo",1);
        SendRequest.getInstance().get(context,RequestType.GET_ASSESS_STATE,params,RequestHttpUtil.BASE_URL+GET_SCHOOL_ASSESS_STATE,resultI);
    }

    public static void getTeacherDetailInfo(Context context, String uuid, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        params.put("uuid", uuid);
        String url = RequestHttpUtil.BASE_URL+"rest/pxteacher/"+uuid+".json";
        SendRequest.getInstance().get(context,RequestType.TEACHER_DETAIL_INFO,params,url,resultI);
    }

    public static void sendVersion(Context context, String phone_type, String phone_version, String app_verion, String city, RequestResultI resultI) {
        JSONObject jSONObject=new JSONObject();
        try {
            jSONObject.put("phone_type", phone_type);
            jSONObject.put("phone_version",phone_version);
            jSONObject.put("app_verion",app_verion);
            jSONObject.put("city",city);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SendRequest.getInstance().post(context, RequestType.ZAN, jSONObject.toString(), RequestHttpUtil.BASE_URL + SEND_VERSION, resultI);

    }

    public static void getPrivilegeByPage(Context context, int pageNo, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        params.put("pageNo",pageNo);
        if(CGApplication.latitude > 0){
            params.put("map_point",CGApplication.longitude+","+CGApplication.latitude);
        }
        SendRequest.getInstance().get(context, RequestType.GET_PRIVELEGE_ACTIVE, params, RequestHttpUtil.BASE_URL + GET_PRIVELEGE_ACTIVE, resultI);
    }

    public static void sendCallMessage(Context context, String ext_uuid, int type, RequestResultI resultI) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ext_uuid",ext_uuid);
            jsonObject.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SendRequest.getInstance().post(context, RequestType.ZAN, jsonObject.toString(), RequestHttpUtil.BASE_URL + CALL_MESSAGE_SATTE, resultI);
    }

    public static void getSchoolAbout(Context context,String s, int pageNo, String sort,RequestResultI resultI) {
        RequestParams params = new RequestParams();
        params.put("pageNo",pageNo);
        if(CGApplication.latitude > 0){
            params.put("map_point",CGApplication.longitude+","+CGApplication.latitude);
        }
        params.put("sort",sort);
        //学校相关获取的学校与培训机构获取的学校内容完全相同,只是地址不一样
        SendRequest.getInstance().get(context, RequestType.ALL_TRAINC_SCHOOL, params, RequestHttpUtil.BASE_URL + OTHER_ALL_TRAINC_SCHOOL, resultI);


    }

    public static void getTrainSchoolDetailFromRecruit(Context context, String uuid, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        params.put("uuid", uuid);
        SendRequest.getInstance().get(context,RequestType.TRAIN_SCHOOL_DETAIL,params,RequestHttpUtil.BASE_URL+TRAIN_SCHOOL_DETAIL_FROM_RECRUIT,resultI);
    }

    public static void getUserInfo(Context context, String storeJESSIONID, String jessionid_md5) {
        RequestParams params = new RequestParams();
        params.put("JSESSIONID",storeJESSIONID);
        params.put("md5",jessionid_md5);
        SendRequest.getInstance().get(context, RequestType.GET_USER_INFO, params, RequestHttpUtil.BASE_URL + GET_USER_INFO, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {
                Login login = (Login) domain;
                if(login != null){
                    //把获取到的用户信息存入到磁盘中
                    CGSharedPreference.setJESSIONID_MD5(login.getMd5());
                    StoreDataInSerialize.storeUserInfo(login);
                    CGApplication.getInstance().setLogin((Login) domain);
                }
            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });


    }

    public static void getTopicConfig(Context context, String configMD5, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        params.put("md5",configMD5);
        SendRequest.getInstance().get(context,RequestType.GET_TOPIC_CONFIG,params,RequestHttpUtil.BASE_URL+GET_TOPIC_CONFIG,resultI);
    }

    public static void getMainTopic(Context context, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        SendRequest.getInstance().get(context,RequestType.GET_MAIN_TOPIC,params,RequestHttpUtil.BASE_URL+GET_MAIN_TOPIC,resultI);
    }

    public static void clickAndRefreshTopic(Context context){
        RequestParams params = new RequestParams();
        SendRequest.getInstance().get(context, RequestType.ZAN, params, RequestHttpUtil.BASE_URL + CLICK_AND_REFRESH_TOPIC, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {

            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {

            }
        });
    }

    public static void getTypeCount(Context context, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        SendRequest.getInstance().get(context,RequestType.FOUND_TYPE_COUNT,params,RequestHttpUtil.BASE_URL+FOUND_TYPE_COUNT,resultI);
    }

    public static void getFoundHotSeclection(Context context, int pageNo, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        params.put("pageNo",pageNo);
        SendRequest.getInstance().get(context,RequestType.FOUND_HOT_SELECTION,params,RequestHttpUtil.BASE_URL+FOUND_HOT_SELECTION,resultI);
    }
}
