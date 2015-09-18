package com.wj.kindergarten.net.request;

import android.content.Context;

import com.loopj.android.http.RequestParams;
import com.wj.kindergarten.bean.BaseModel;
import com.wj.kindergarten.net.RequestHttpUtil;
import com.wj.kindergarten.net.RequestResultI;
import com.wj.kindergarten.net.RequestType;
import com.wj.kindergarten.net.SendRequest;
import com.wj.kindergarten.utils.Utils;

import org.json.JSONObject;

import java.util.List;

/**
 * AddressBookRequest
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-12 20:44
 */
public class AddressBookRequest {

    private static final String TEACHERS = RequestHttpUtil.BASE_URL + "rest/userinfo/getTeacherPhoneBook.json";//获取院长和老师列表
    private static final String LEADER_MESSAGE = RequestHttpUtil.BASE_URL + "rest/message/queryByLeader.json";//获取院长和家长的消息列表
    private static final String TEACHER_MESSAGE = RequestHttpUtil.BASE_URL + "rest/message/queryByTeacher.json";//获取老师和家长的消息列表
    private static final String GET_EMOT = RequestHttpUtil.BASE_URL + "rest/share/getEmot.json";//获取表情符号
    // private static final String SEND_MESSAGE_TO_LEADER = "http://jz.wenjienet.com/px-mobile/" + "rest/message/saveToLeader.json";//给园长写信
    private static final String SEND_MESSAGE_TO_LEADER = RequestHttpUtil.BASE_URL + "rest/message/saveToLeader.json";//给园长写信
    private static final String SEND_MESSAGE_TO_TEACHER = RequestHttpUtil.BASE_URL + "rest/message/saveToTeacher.json";//给老师写信

    /**
     * 通讯录获得园长和老师列表
     *
     * @param resultI
     */
    public static void getTeachers(Context context, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        SendRequest.getInstance().get(context, RequestType.TEACHERS, params, TEACHERS, resultI);
    }

    /**
     * 获得园长和家长的消息
     *
     * @param uuid
     * @param pageNo
     * @param resultI
     */
    public static void getLeaderMessage(Context context, String uuid, int pageNo, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        params.put("uuid", uuid);
        params.put("pageNo", pageNo);
        SendRequest.getInstance().get(context, RequestType.GET_LEADER_MESSAGE, params, LEADER_MESSAGE, resultI);
    }

    /**
     * 获得老师的消息
     *
     * @param uuid
     * @param pageNo
     * @param resultI
     */
    public static void getTeacherMessage(Context context, String uuid, int pageNo, RequestResultI resultI) {
        RequestParams params = new RequestParams();
        params.put("uuid", uuid);
        params.put("pageNo", pageNo);
        SendRequest.getInstance().get(context, RequestType.GET_TEACHER_MESSAGE, params, TEACHER_MESSAGE, resultI);
    }

    /**
     * 获取表情图片列表
     */
    public static void getEmot(final Context context) {
        RequestParams params = new RequestParams();
        SendRequest.getInstance().get(context, RequestType.GET_EMOT, params, GET_EMOT, new RequestResultI() {
            @Override
            public void result(BaseModel domain) {

            }

            @Override
            public void result(List<BaseModel> domains, int total) {

            }

            @Override
            public void failure(String message) {
                Utils.showToast(context, message);
            }
        });
    }

    /**
     * 给园长发消息
     *
     * @param context
     * @param revice_useruuid
     * @param message
     * @param resultI
     */
    public static void sendMessageToLeader(Context context, String revice_useruuid, String message, RequestResultI resultI) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("revice_useruuid", revice_useruuid);
            jsonObject.put("message", message);
            SendRequest.getInstance().post(context, RequestType.SEND_MESSAGE_TO_LEADER, jsonObject.toString(),
                    SEND_MESSAGE_TO_LEADER, resultI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 给园长发消息
     *
     * @param context
     * @param revice_useruuid
     * @param message
     * @param resultI
     */
    public static void sendMessageToTeacher(Context context, String revice_useruuid, String message, RequestResultI resultI) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("revice_useruuid", revice_useruuid);
            jsonObject.put("message", message);
            SendRequest.getInstance().post(context, RequestType.SEND_MESSAGE_TO_TEACHER, jsonObject.toString(),
                    SEND_MESSAGE_TO_TEACHER, resultI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
