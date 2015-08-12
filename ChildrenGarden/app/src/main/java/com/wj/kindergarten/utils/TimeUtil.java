package com.wj.kindergarten.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TimeUtil 时间工具类
 *
 * @author weiwu.song
 * @data: 2015/6/9 10:19
 * @version: v1.0
 */
public final class TimeUtil {
    private final static int DELETE_DAY = 15;
    //时间格式
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private TimeUtil() {
    }

    /**
     * 是否删除旧数据
     *
     * @param datetime
     * @return
     */
    public static boolean hasDeleteOldData(String datetime) {
        try {
            Date d1 = new Date();
            Date d2 = format.parse(datetime);
            int day = (int) ((d1.getTime() - d2.getTime()) / 86400000);
            if (day > DELETE_DAY) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getNowDate() {
        Date date = new Date();
        return format.format(date);
    }
}
