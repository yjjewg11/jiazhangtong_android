package com.wj.kindergarten.net;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceActivity;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.socialize.utils.Log;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.IOStoreData.StoreDataInSerialize;
import com.wj.kindergarten.bean.BoutiqueDianzanList;
import com.wj.kindergarten.bean.BoutiqueDianzanListFather;
import com.wj.kindergarten.bean.NeedBoundPhoneResult;
import com.wj.kindergarten.bean.TeacherDetailInfo;
import com.wj.kindergarten.bean.AddChild;
import com.wj.kindergarten.bean.AddressBook;
import com.wj.kindergarten.bean.AddressBookEmot;
import com.wj.kindergarten.bean.AddressBookMessage;
import com.wj.kindergarten.bean.AllPfAlbum;
import com.wj.kindergarten.bean.AllTeacherList;
import com.wj.kindergarten.bean.AppraiseTeacherList;
import com.wj.kindergarten.bean.ArticleDetail;
import com.wj.kindergarten.bean.ArticleList;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.BaseResponse;
import com.wj.kindergarten.bean.BoutiqueAlbum;
import com.wj.kindergarten.bean.BoutiqueAllpic;
import com.wj.kindergarten.bean.BoutiqueReviewAddress;
import com.wj.kindergarten.bean.BoutiqueSingleInfo;
import com.wj.kindergarten.bean.ConfigObject;
import com.wj.kindergarten.bean.CourseList;
import com.wj.kindergarten.bean.FoodList;
import com.wj.kindergarten.bean.FoundHotSelectionFather;
import com.wj.kindergarten.bean.FoundTypeCount;
import com.wj.kindergarten.bean.GetAssessStateList;
import com.wj.kindergarten.bean.HtmlTitle;
import com.wj.kindergarten.bean.InteractionList;
import com.wj.kindergarten.bean.Ka;
import com.wj.kindergarten.bean.Login;
import com.wj.kindergarten.bean.MainTopic;
import com.wj.kindergarten.bean.MineAllCourseList;
import com.wj.kindergarten.bean.MoreData;
import com.wj.kindergarten.bean.MoreDiscussList;
import com.wj.kindergarten.bean.Msg;
import com.wj.kindergarten.bean.NoticeDetail;
import com.wj.kindergarten.bean.NoticeList;
import com.wj.kindergarten.bean.OnceSpecialCourseList;
import com.wj.kindergarten.bean.PfAlbumInfo;
import com.wj.kindergarten.bean.PfAlbumList;
import com.wj.kindergarten.bean.PfChangeData;
import com.wj.kindergarten.bean.PfModeName;
import com.wj.kindergarten.bean.PfMusic;
import com.wj.kindergarten.bean.PfSingleAssess;
import com.wj.kindergarten.bean.PrivilegeActiveList;
import com.wj.kindergarten.bean.ReplyList;
import com.wj.kindergarten.bean.SchoolDetailList;
import com.wj.kindergarten.bean.SchoolList;
import com.wj.kindergarten.bean.SignList;
import com.wj.kindergarten.bean.SinlePfExtraInfo;
import com.wj.kindergarten.bean.SpecialCourseInfoList;
import com.wj.kindergarten.bean.SpecialCourseTypeList;
import com.wj.kindergarten.bean.StoreList;

import com.wj.kindergarten.bean.StudyStateObjectList;
import com.wj.kindergarten.bean.SyncUploadPic;
import com.wj.kindergarten.bean.TeacherCountList;

import com.wj.kindergarten.bean.TeacherInfo;
import com.wj.kindergarten.bean.TrainChildInfoList;
import com.wj.kindergarten.bean.TrainCourse;
import com.wj.kindergarten.bean.TrainSchoolInfoListFather;

import com.wj.kindergarten.bean.UUIDList;
import com.wj.kindergarten.bean.ZanItem;
import com.wj.kindergarten.common.CGSharedPreference;
import com.wj.kindergarten.ui.addressbook.EmotManager;
import com.wj.kindergarten.ui.mine.LoginActivity;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.bean.GsonKdUtil;
import com.wj.kindergarten.utils.Utils;


import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



/**
 * SendRequest
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/5/21 17:00
 */
public class SendRequest {
    private static SendRequest request = null;
    private static final String HTTP_SUCCESS = "success";

    public synchronized static SendRequest getInstance() {
        if (request == null) {
            synchronized (SendRequest.class) {
                if (request == null) {
                    request = new SendRequest();
                }
            }
        }
        return request;
    }

    /**
     * @param requestType 请求说明
     * @param params
     * @param url
     * @param resultI     数据处理接口
     */
    public void post(Context context, final int requestType, RequestParams params, String url,
                     final RequestResultI resultI) {
        if (Utils.isNetworkAvailable(CGApplication.getInstance())) {
            CGLog.d("SendRequest：" + requestType + "->" + url + "?" + params);
            RequestHttpUtil.post(context, url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        CGLog.i("SendRequest：" + requestType + "->" + new String(response.toString().getBytes(), "utf-8"));

                        BaseResponse baseResponse = new BaseResponse(response);
                        if (HTTP_SUCCESS.equals(baseResponse.getResMsg().getStatus())) {
                            result(requestType, response.toString(), resultI);
                        } else if ("failed".equals(baseResponse.getResMsg().getStatus())) {
                            resultI.failure(baseResponse.getResMsg().getMessage());
                        }
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    resultI.failure("获取数据失败");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      String responseBody, Throwable e) {
                    super.onFailure(statusCode,headers,responseBody,e);
                    CGLog.d("SendRequest：" + requestType + "->" + responseBody);
                    resultI.failure("请求超时,请检查您的网络是否有问题。");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    CGLog.d("SendRequest：" + requestType + "->" + errorResponse);
                    resultI.failure("请求超时,请检查您的网络是否有问题。");
                }

                @Override
                public void onFailure(int statusCode,Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    CGLog.d("SendRequest：" + requestType + "->" + errorResponse);
                    resultI.failure("请求超时,请检查您的网络是否有问题。");
                }
            });
        } else {
            resultI.failure("网络连接已断开或不可用");
        }
    }

    public void post(final Context context, final int requestType, String json, String url,
                     final RequestResultI resultI) {
        if (Utils.isNetworkAvailable(CGApplication.getInstance())) {
            CGLog.d("SendRequest：" + requestType + "->" + url + "?" + json);
            StringEntity httpEntity = null;
            try {
                httpEntity = new StringEntity(json, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            RequestHttpUtil.post(context, url, httpEntity, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] header, JSONObject response) {
                    super.onSuccess(statusCode, header, response);
                    try {
                        CGLog.i("SendRequest：" + requestType + "->" + new String(response.toString().getBytes(), "utf-8"));

                        BaseResponse baseResponse = new BaseResponse(response);
                        if (HTTP_SUCCESS.equals(baseResponse.getResMsg().getStatus())) {
                            result(requestType, response.toString(), resultI);
                        } else if ("failed".equals(baseResponse.getResMsg().getStatus())) {
                            resultI.failure(baseResponse.getResMsg().getMessage());
                        } else if ("sessionTimeout".equals(baseResponse.getResMsg().getStatus())) {
                            Utils.showToast(context, baseResponse.getResMsg().getMessage());
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    resultI.failure("获取数据失败");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      String responseBody, Throwable e) {
                    super.onFailure(statusCode, headers, responseBody, e);
                    CGLog.d("SendRequest：" + requestType + "->" + responseBody);
                    resultI.failure("请求超时,请检查您的网络是否有问题。");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    CGLog.d("SendRequest：" + requestType + "->" + errorResponse);
                    resultI.failure("请求超时,请检查您的网络是否有问题。");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    CGLog.d("SendRequest：" + requestType + "->" + errorResponse);
                    resultI.failure("请求超时,请检查您的网络是否有问题。");
                }
            });
        } else {
            resultI.failure("网络连接已断开或不可用");
        }
    }

    public void get(final Context context, final int requestType, RequestParams params, String url,
                    final RequestResultI resultI) {
        if (Utils.isNetworkAvailable(CGApplication.getInstance())) {
            CGLog.d("SendRequest：" + requestType + "->" + url + "?" + params);
            RequestHttpUtil.get(context, url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        Log.i("TAG","打印  - - >"+requestType+ " 返回的字符串 - - >"+ response.toString());
                        CGLog.d("SendRequest：" + requestType + "->" + response.toString());
                        BaseResponse baseResponse = new BaseResponse(response);
                        if (HTTP_SUCCESS.equals(baseResponse.getResMsg().getStatus())) {

                            if (requestType != RequestType.GET_EMOT) {
                                result(requestType, response.toString(), resultI);
                            } else {
                                AddressBookEmot emot = (AddressBookEmot) getDomain(response.toString(), AddressBookEmot.class);
                                if (null != emot) {
                                    EmotManager.addEmots(emot.getList());
                                }
                            }
                        } else if ("failed".equals(baseResponse.getResMsg().getStatus())) {
                            resultI.failure(baseResponse.getResMsg().getMessage());
                        } else if ("sessionTimeout".equals(baseResponse.getResMsg().getStatus())) {
                            Utils.showToast(context, baseResponse.getResMsg().getMessage());
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }else if("unchange".equals(baseResponse.getResMsg().getStatus())){
                            //如果状态是未改变，则从磁盘获取数据用户信息
                            if(requestType == RequestType.GET_USER_INFO){
                                Log.i("TAG", "打印用户信息" + StoreDataInSerialize.getUserInfo());
                                Login login = StoreDataInSerialize.getUserInfo();
                                CGApplication.getInstance().setLogin(login);
                                CGSharedPreference.setStoreJESSIONID(login.getJSESSIONID());
                                return;
                            }

                            if(requestType == RequestType.GET_TOPIC_CONFIG){
                                return;
                            }

                        }
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    resultI.failure("获取数据失败");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      String responseBody, Throwable e) {
                    super.onFailure(statusCode, headers, responseBody, e);
                    CGLog.d("SendRequest：" + requestType + "->" + responseBody);
                    resultI.failure("请求超时,请检查您的网络是否有问题。");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    CGLog.d("SendRequest：" + requestType + "->" + throwable);
                    resultI.failure("请求超时,请检查您的网络是否有问题。");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    CGLog.d("SendRequest：" + requestType + "->" + throwable);
                    resultI.failure("请求超时,请检查您的网络是否有问题。");
                }
            });
        } else {
            resultI.failure("网络连接已断开或不可用");
        }
    }

    /**
     * 返回结果处理json字符串
     *
     * @param requestType
     * @param domain
     * @param resultI
     */
    private void result(int requestType, String domain, RequestResultI resultI) {
        switch (requestType) {
            case RequestType.REGISTER:
            case RequestType.FORGET_PWD:
            case RequestType.ZAN:
            case RequestType.ZAN_CANCEL:
            case RequestType.STORE:
            case RequestType.CANCEL_STORE:
            case RequestType.REPLY:
            case RequestType.APPRAISE_TEACHER:
            case RequestType.INTERACTION_SEND:
            case RequestType.LOGIN_OUT:
            case RequestType.READ_MESSAGE:
            case RequestType.ADD_FAMILY_MEMBER:
                resultI.result(getDomain(domain, BaseModel.class));
                break;
            case RequestType.CHANGE_CHILD:
                 resultI.result(getDomain(domain, AddChild.class));
                break;
            case RequestType.ZAN_LIST:
                resultI.result(getDomain(domain, ZanItem.class));
                break;
            case RequestType.REPLY_LIST:
                resultI.result(getDomain(domain, ReplyList.class));
                break;
            case RequestType.LOGIN:
                resultI.result(getDomain(domain, Login.class));
                break;
            case RequestType.INTERACTION_LIST:
                resultI.result(getDomain(domain, InteractionList.class));
                break;
            case RequestType.NOTICE_LIST:
                resultI.result(getDomain(domain, NoticeList.class));
                break;
            case RequestType.NOTICE:
                resultI.result(getDomain(domain, NoticeDetail.class));
                break;
            case RequestType.SIGN:
                resultI.result(getDomain(domain, SignList.class));
                break;
            case RequestType.COURSE_LIST:
                resultI.result(getDomain(domain, CourseList.class));
                break;
            case RequestType.FOOD_LIST:
                resultI.result(getDomain(domain, FoodList.class));
                break;
            case RequestType.ARTICLE_LIST:
                resultI.result(getDomain(domain, ArticleList.class));
                break;
            case RequestType.ARTICLE:
                ArticleDetail detail = (ArticleDetail) getDomain(domain, ArticleDetail.class);
                CGLog.d("isFavour: " + detail.isFavor());
                resultI.result(getDomain(domain, ArticleDetail.class));
                break;
            case RequestType.APPRAISE_TEACHER_LIST:
                resultI.result(getDomain(domain, AppraiseTeacherList.class));
                break;
            case RequestType.TEACHERS:
                resultI.result(getDomain(domain, AddressBook.class));
                break;
            case RequestType.GET_LEADER_MESSAGE:
            case RequestType.GET_TEACHER_MESSAGE:
                resultI.result(getDomain(domain, AddressBookMessage.class));
                break;
            case RequestType.SEND_MESSAGE_TO_LEADER:
            case RequestType.SEND_MESSAGE_TO_TEACHER:
            case RequestType.UPDATE_PASSWORD:
            case RequestType.DEVICE_SAVE:
                resultI.result(new BaseModel());
                break;
            case RequestType.QUERY_MESSAGE:
                resultI.result(getDomain(domain, Msg.class));
                break;
            case RequestType.QUERY_MORE:
                resultI.result(getDomain(domain, MoreData.class));
                break;
            case RequestType.QUERY_SCHOOL:
                resultI.result(getDomain(domain, SchoolList.class));
                break;
            case RequestType.QUERY_STORE:
                resultI.result(getDomain(domain, StoreList.class));
                break;
            case RequestType.QUERY_TEACHER_INFO:
                resultI.result(getDomain(domain, TeacherInfo.class));
                break;
            case RequestType.KA_INFO:
                resultI.result(getDomain(domain, Ka.class));
                break;

            case RequestType.TRAIN_COURSE_LIST:
                resultI.result(getDomain(domain, TrainCourse.class));
                break;

            case RequestType.TRAIN_CHLID_INFO:
                resultI.result(getDomain(domain, TrainChildInfoList.class));
                break;
            case RequestType.TRAIN_CLASS:
//                resultI.result(getDomain(domain, TrainClassList.class));
                break;
            case RequestType.SPECIAL_COURSE_TYPE:
                resultI.result(getDomain(domain, SpecialCourseTypeList.class));
                break;

            case RequestType.SPECIAL_COURSE_INFO:
                resultI.result(getDomain(domain,SpecialCourseInfoList.class));
                break;
            case RequestType.ONCE_COURSE_CLICK:
                resultI.result(getDomain(domain, OnceSpecialCourseList.class));
                break;
            case RequestType.ALL_TRAINC_SCHOOL:
                resultI.result(getDomain(domain, TrainSchoolInfoListFather.class));
                break;

            case RequestType.MORE_DISCUSS_FROM_UUID:
                resultI.result(getDomain(domain, MoreDiscussList.class));
                break;
            case RequestType.NEXT_CLASS_INFO:
                resultI.result(getDomain(domain,TrainCourse.class));
                break;
            case RequestType.TEACHER_COUNT:
                resultI.result(getDomain(domain, TeacherCountList.class));
                break;
            case RequestType.STUDY_STATE:
                resultI.result(getDomain(domain, StudyStateObjectList.class));
                break;
            case RequestType.MINE_ALL_COURSE:
                resultI.result(getDomain(domain, MineAllCourseList.class));
                break;
            case RequestType.TRAIN_SCHOOL_DETAIL:
                resultI.result(getDomain(domain, SchoolDetailList.class));
                break;
            case RequestType.ALL_TEACHER:
                resultI.result(getDomain(domain, AllTeacherList.class));
                break;
            case RequestType.GET_ASSESS_STATE:
                resultI.result(getDomain(domain, GetAssessStateList.class));
                break;
            case RequestType.TEACHER_DETAIL_INFO:
                resultI.result(getDomain(domain,TeacherDetailInfo.class));
                break;
            case RequestType.GET_PRIVELEGE_ACTIVE:
                resultI.result(getDomain(domain, PrivilegeActiveList.class));
                break;
            case RequestType.GET_TOPIC_CONFIG:
                resultI.result(getDomain(domain, ConfigObject.class));
                break;
            case RequestType.GET_MAIN_TOPIC:
                resultI.result(getDomain(domain, MainTopic.class));
                break;
            case RequestType.GET_USER_INFO:
                resultI.result(getDomain(domain,Login.class));
                break;
            case RequestType.FOUND_TYPE_COUNT:
                resultI.result(getDomain(domain,FoundTypeCount.class));
                break;
            case RequestType.FOUND_HOT_SELECTION:
                resultI.result(getDomain(domain,FoundHotSelectionFather.class));
                break;
            case RequestType.GET_INTERACTION_LINK:
                resultI.result(getDomain(domain, HtmlTitle.class));
                break;
            case RequestType.GET_PF_ALBUM_LIST :
                resultI.result(getDomain(domain, PfAlbumList.class));
                break;
            case RequestType.LOOK_FOR_ALL_PF :
                resultI.result(getDomain(domain, AllPfAlbum.class));
                break;
            case RequestType.PF_PIC_BY_UUID :
                resultI.result(getDomain(domain, AllPfAlbum.class));
                break;
            case RequestType.QUERY_INCREMENT_NEW_DATA:
                resultI.result(getDomain(domain, PfChangeData.class));
                break;
            case RequestType.PF_OBJ_BY_UPDATE :
                resultI.result(getDomain(domain, UUIDList.class));
                break;
            case RequestType.GET_SINGLE_PF_INFO :

                break;
            case RequestType.GET_BOUTIQUE_MODE :
                resultI.result(getDomain(domain, PfModeName.class));
                break;
            case RequestType.GET_MODE_MUSIC :
                resultI.result(getDomain(domain, PfMusic.class));
                break;
            case RequestType.GET_SINGLE_PF_EXTRA_INFO :
                resultI.result(getDomain(domain, SinlePfExtraInfo.class));
                break;
            case RequestType.GET_SINGLE_PF_ASSESS :
                resultI.result(getDomain(domain, PfSingleAssess.class));
                break;
            case RequestType.GET_BOUTIQUE_SINGLE_INFO:
                resultI.result(getDomain(domain, BoutiqueSingleInfo.class));
                break;
            case RequestType.GET_BOUTIQUE_REVIEW_URL:
                resultI.result(getDomain(domain, BoutiqueReviewAddress.class));
                break;
            case RequestType.GET_BOUTIQUE_ALBUM :
                resultI.result(getDomain(domain, BoutiqueAlbum.class));
                break;
            case RequestType.GET_PF_ALBUM_INFO :
                resultI.result(getDomain(domain, PfAlbumInfo.class));
                break;
            case RequestType.INIT_SYNC_UPLOAD:
                resultI.result(getDomain(domain, SyncUploadPic.class));
                break;
            case RequestType.GET_ALL_PIC_FROM_BOUTIQUE:
                resultI.result(getDomain(domain, BoutiqueAllpic.class));
                break;
            case RequestType.GET_BOUTIQUE_DIAN_ZAN_LIST:
                resultI.result(getDomain(domain, BoutiqueDianzanListFather.class));
                break;
            case RequestType.VALIDATE_BAN_PHONE:
                resultI.result(getDomain(domain, NeedBoundPhoneResult.class));
                break;
            case RequestType.GET_THREE_USER_INFO:
                resultI.result(getDomain(domain, Login.class));
                break;

            default:
                break;
        }
    }

    /**
     * 把json转换为列表
     *
     * @param responseBaseDomain
     * @param className
     * @return
     */
    private List<BaseModel> getDomains(String responseBaseDomain, Class<? extends BaseModel> className) {

        List<?> list = GsonKdUtil.getGson().fromJson(responseBaseDomain, new TypeToken<List<?>>() {
        }.getType());
        String jsonStr = null;
        List<BaseModel> baseDomains = new ArrayList<BaseModel>();
        BaseModel baseDomain = null;
        for (int i = 0; i < list.size(); i++) {
            jsonStr = GsonKdUtil.toJson(list.get(i));
            baseDomain = GsonKdUtil.getGson().fromJson(jsonStr, className);
            baseDomains.add(baseDomain);
        }
        return baseDomains;
    }

    private BaseModel getDomain(String domain, Class<? extends BaseModel> className) {
        BaseModel baseDomain = GsonKdUtil.getGson().fromJson(domain, className);
        return baseDomain;
    }
}
