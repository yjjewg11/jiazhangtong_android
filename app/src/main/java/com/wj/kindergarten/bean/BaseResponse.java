package com.wj.kindergarten.bean;

import com.google.gson.annotations.Expose;
import com.wj.kindergarten.utils.GsonUtil;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * service返回数据基类
 * Created by LQ on 14-3-11.
 */
public class BaseResponse implements Serializable {
    @Expose
    private BaseResMsg ResMsg;

    public BaseResponse() {
    }

    /**
     * 根据service返回的JSON数据构建
     *
     * @param response
     * @throws Exception
     */
    public BaseResponse(JSONObject response) throws Exception {
        if (response.has("ResMsg")) {
            JSONObject temp = response.getJSONObject("ResMsg");
            if (temp != null) {
                ResMsg = GsonUtil.getGson().fromJson(temp.toString(), BaseResMsg.class);
            }
        }
    }

    public BaseResMsg getResMsg() {
        return ResMsg;
    }

    public void setResMsg(BaseResMsg resMsg) {
        ResMsg = resMsg;
    }
}
