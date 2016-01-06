package com.wj.kindergarten.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * JsonUtil
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/5/21 14:32
 */
public class GsonKdUtil {

    static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static Gson getGson() {
        return gson;
    }
}
