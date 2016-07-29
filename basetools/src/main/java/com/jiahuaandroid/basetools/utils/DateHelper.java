package com.jiahuaandroid.basetools.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jhhuang on 2016/7/21.
 * QQ:781913268
 * Description：Date日期的工具类
 */
public class DateHelper {
    public static final String DEFUALT_DATE_FORMAT = "yyyy-MM-dd HH:mm";

    /**
     * @param date Date时间对象
     * @return yyyy-MM-dd HH:mm的日期字符串
     */
    public static String date2str(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DEFUALT_DATE_FORMAT);
        return format.format(date);
    }

    /**
     * 获取当前年
     * @return 2016
     */
    public static int getCurrYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前月
     * @return 7
     */
    public static int getCurrMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(Calendar.MONTH)+1;
    }

    /**
     * @param date yyyy-MM-dd HH:mm 字符串类型的日期
     * @return date日期对象
     */
    public static Date str2date(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date parse = null;
        try {
            parse = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            parse = new Date(0);
        }
        return parse;
    }

    /**
     * 获取日期的小时和分钟
     *
     * @param date yyyy-MM-dd HH:mm 格式的字符串
     * @return
     */
    public static String getHourAndMin(String date) {
        return date.substring(date.length() - 5, date.length());
    }

    public static String getHourAndMin(Date date) {
        String s = date2str(date);
        return s.substring(s.length() - 5, s.length());
    }

    /**
     * 调整日期时间
     * @param date 指定日期
     * @param field 字段 : Calendar.YEAR...
     * @param value 数字
     * @return 修改之后的日期
     */
    public static Date addTime(Date date,int field, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar.add(field, value);
        return calendar.getTime();
    }

//    public static void main(String[] args) {
//        System.out.print(date2str(addTime(new Date(0),Calendar.DAY_OF_YEAR,-15)));
//    }

}
