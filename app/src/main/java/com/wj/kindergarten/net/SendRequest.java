package com.wj.kindergarten.net;

import android.content.Context;
import android.content.Intent;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wj.kindergarten.CGApplication;
import com.wj.kindergarten.bean.AddressBook;
import com.wj.kindergarten.bean.AddressBookEmot;
import com.wj.kindergarten.bean.AddressBookMessage;
import com.wj.kindergarten.bean.AppraiseTeacherList;
import com.wj.kindergarten.bean.ArticleDetail;
import com.wj.kindergarten.bean.ArticleList;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.bean.BaseResponse;
import com.wj.kindergarten.bean.CourseList;
import com.wj.kindergarten.bean.FoodList;
import com.wj.kindergarten.bean.InteractionList;
import com.wj.kindergarten.bean.Ka;
import com.wj.kindergarten.bean.Login;
import com.wj.kindergarten.bean.MoreData;
import com.wj.kindergarten.bean.Msg;
import com.wj.kindergarten.bean.NoticeDetail;
import com.wj.kindergarten.bean.NoticeList;
import com.wj.kindergarten.bean.ReplyList;
import com.wj.kindergarten.bean.SchoolList;
import com.wj.kindergarten.bean.SignList;
import com.wj.kindergarten.bean.StoreList;
import com.wj.kindergarten.bean.TeacherInfo;
import com.wj.kindergarten.bean.UserInfo;
import com.wj.kindergarten.bean.ZanItem;
import com.wj.kindergarten.ui.addressbook.EmotManager;
import com.wj.kindergarten.ui.mine.LoginActivity;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.GsonUtil;
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
                public void onSuccess(int statusCode, Header[] header, JSONObject response) {
                    super.onSuccess(statusCode, header, response);
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
     * 返回结果处理
     *
     * @param requestType
     * @param domain
     * @param resultI
     */
    private void result(int requestType, String domain, RequestResultI resultI) {
        switch (requestType) {
            case RequestType.REGISTER:
            case RequestType.FORGET_PWD:
            case RequestType.CHANGE_CHILD:
            case RequestType.ZAN:
            case RequestType.ZAN_CANCEL:
            case RequestType.STORE:
            case RequestType.CANCEL_STORE:
            case RequestType.REPLY:
            case RequestType.APPRAISE_TEACHER:
            case RequestType.INTERACTION_SEND:
            case RequestType.LOGIN_OUT:
            case RequestType.READ_MESSAGE:
                resultI.result(getDomain(domain, BaseModel.class));
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

        List<?> list = GsonUtil.getGson().fromJson(responseBaseDomain, new TypeToken<List<?>>() {
        }.getType());
        String jsonStr = null;
        List<BaseModel> baseDomains = new ArrayList<BaseModel>();
        BaseModel baseDomain = null;
        for (int i = 0; i < list.size(); i++) {
            jsonStr = GsonUtil.toJson(list.get(i));
            baseDomain = GsonUtil.getGson().fromJson(jsonStr, className);
            baseDomains.add(baseDomain);
        }
        return baseDomains;
    }

    private BaseModel getDomain(String domain, Class<? extends BaseModel> className) {
        BaseModel baseDomain = GsonUtil.getGson().fromJson(domain, className);
        return baseDomain;
    }
}