package com.chad.baserecyclerviewadapterhelper.util;


import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;



public class TimeUtil {

    private static final String TAG = "TimeUtil";

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm");

    private TimeUtil() {
        throw new AssertionError();
    }

    /**
     * long time to string
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * get current time in milliseconds
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }


    /**
     * @param timeStr 传入系统System.currentTimeMillis()格式的long字符串,不然就减的有问题了
     * @return 弹框显示的时间样式.
     */
    @Deprecated
    private String getStandardDate(long timeStr) {

        StringBuffer sb = new StringBuffer();

//        long t = Long.parseLong(timeStr);
        long time = System.currentTimeMillis() - timeStr;

        //向上取整
        long mill = (long) Math.ceil(time / 1000);//秒前

        long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前

        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时

        long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前

        if (day - 1 > 0) {
            sb.append(day + "天");
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append(hour + "小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute + "分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill >= 59 && mill <= 60) {
                sb.append("1分钟");
            } else {
                sb.append(mill + "秒");
            }
        } else {
            sb.append("现在");
        }
        if (!sb.toString().equals("现在")) {
            sb.append("前");
        }
        return sb.toString();
    }

    public static String getBeforeTime(long currentTimeMills) {
        CharSequence timeSpanString = DateUtils.getRelativeTimeSpanString(
                currentTimeMills,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_SHOW_DATE);
        return timeSpanString.toString().replace(" ", "");
    }



}
