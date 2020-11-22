package com.qqlin.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Date getNowDateTime() {

        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);

        Date currentTime_2;
        try {
            currentTime_2 = formatter.parse(dateString);
        } catch (ParseException e) {
            currentTime_2 = currentTime;
        }
        return currentTime_2;
    }

    public static String getNowDateTimeStr() {

        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);

        return dateString;
    }

    public static String getNowDateTimeStrTwo() {

        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);

        return dateString;
    }

    //获取date下一天的日期
    public static Date getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        date = calendar.getTime();
        return date;
    }

    //获取date下一周的日期
    public static Date getNextWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +7);
        date = calendar.getTime();
        return date;
    }

    //获取date下一月的日期
    public static Date getNextMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, +1);
        date = calendar.getTime();
        return date;
    }

    //比较时间
//    public static boolean compareTime(Date date1 ,Date date2){
//        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        return ;
//    }

    public static void main(String args[]) {
        System.out.print(getNowDateTimeStr());
    }
}
