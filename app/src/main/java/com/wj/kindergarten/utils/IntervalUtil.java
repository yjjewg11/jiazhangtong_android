package com.wj.kindergarten.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * IntervalUtil
 *
 * @Description:
 * @Author: Wave
 * @CreateDate: 2015/8/12 23:49
 */
public final class IntervalUtil {

    private IntervalUtil() {
    }

    public static String getIntervalOld(String createtime) { //传入的时间格式必须类似于2012-8-21 17:53:20这样的格式

        if (Utils.stringIsNull(createtime)) {
            return "";
        }

        String interval = null;

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date d1 = (Date) sd.parse(createtime, pos);

        //用现在距离1970年的时间间隔new Date().getTime()减去以前的时间距离1970年的时间间隔d1.getTime()得出的就是以前的时间与现在时间的时间间隔
        long time = new Date().getTime() - d1.getTime();// 得出的时间间隔是毫秒

        if (time / 1000 < 10 && time / 1000 >= 0) {
            //如果时间间隔小于10秒则显示“刚刚”time/10得出的时间间隔的单位是秒
            interval = "刚刚";

        } else if (time / 3600000 < 24 && time / 3600000 > 0) {
            //如果时间间隔小于24小时则显示多少小时前
            int h = (int) (time / 3600000);//得出的时间间隔的单位是小时
            interval = h + "小时前";

        } else if (time / 60000 < 60 && time / 60000 > 0) {
            //如果时间间隔小于60分钟则显示多少分钟前
            int m = (int) ((time % 3600000) / 60000);//得出的时间间隔的单位是分钟
            interval = m + "分钟前";

        } else if (time / 1000 < 60 && time / 1000 > 0) {
            //如果时间间隔小于60秒则显示多少秒前
            int se = (int) ((time % 60000) / 1000);
            interval = se + "秒前";

        } else {
            //大于24小时，则显示正常的时间，但是不显示秒
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            ParsePosition pos2 = new ParsePosition(0);
            Date d2 = (Date) sdf.parse(createtime, pos2);

            interval = sdf.format(d2);
        }
        return interval;
    }

    private static final long ONE_SECOND = 1000L;
    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";

    public static String getInterval(String time) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date date = sd.parse(time, pos);

        long delta = new Date().getTime() - date.getTime();
        if (delta < 10L * ONE_SECOND) {
            return "刚刚";
        } else if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (delta < /*12*/3L * 4L * ONE_WEEK) {//三个月前
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {//三个月前处理
//            long years = toYears(delta);
//            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            ParsePosition pos2 = new ParsePosition(0);
            Date d2 = sdf.parse(time, pos2);

            return sdf.format(d2);
        }
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }
}
