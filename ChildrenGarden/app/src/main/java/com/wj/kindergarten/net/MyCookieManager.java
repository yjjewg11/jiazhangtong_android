package com.wj.kindergarten.net;

import org.apache.http.cookie.Cookie;

import java.util.ArrayList;
import java.util.List;

/**
 * MyCookieManager
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-09-01 15:25
 */
public class MyCookieManager {

    public static List<Cookie> cookies = new ArrayList<>();

    public static void add(List<Cookie> list) {
        cookies.clear();
        cookies.addAll(list);
    }

    public static List<Cookie> getCookies() {
        return cookies;
    }
}
