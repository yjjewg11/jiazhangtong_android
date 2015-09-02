package com.wj.kindergarten.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
     * 获取当前时间
     *
     * @return
     */
    public static String getNowDate() {
        Date date = new Date();
        return format.format(date);
    }

    public static String getWeekOfDay(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            c.setTime(format.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String mWay = c.get(Calendar.DAY_OF_WEEK) + "";
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return "星期" + mWay;
    }

    public static int getWeekOfDayNum(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            c.setTime(format.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.get(Calendar.DAY_OF_WEEK) - 1;
    }
}
