package com.semioe.healthcareup.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/6.
 */

public class TimeUtils {

    public static  String getBefore3mDate(){
        Date dBefore = new Date();
        Date dNow = new Date();   //当前时间
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(calendar.MONTH, -3);  //设置为前3月
        dBefore = calendar.getTime();   //得到前3月的时间

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
        String defaultStartDate = sdf.format(dBefore);    //格式化前3月的时间
        String defaultEndDate = sdf.format(dNow); //格式化当前时间
        return defaultStartDate;
    }

    public static  String getBefore1mDate(){
        Date dBefore = new Date();
        Date dNow = new Date();   //当前时间
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(calendar.MONTH, -1);  //设置为前1月
        dBefore = calendar.getTime();   //得到前3月的时间

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
        String defaultStartDate = sdf.format(dBefore);    //格式化前3月的时间
        String defaultEndDate = sdf.format(dNow); //格式化当前时间
        return defaultStartDate;
    }

    public static  String getBefore6mDate(){
        Date dBefore = new Date();
        Date dNow = new Date();   //当前时间
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(calendar.MONTH, -6);  //设置为前6月
        dBefore = calendar.getTime();   //得到前3月的时间

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
        String defaultStartDate = sdf.format(dBefore);    //格式化前3月的时间
        String defaultEndDate = sdf.format(dNow); //格式化当前时间
        return defaultStartDate;
    }

    public static String getCurrentData(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        return df.format(new Date());
    }

    public static String getFormatDate(String date){
        SimpleDateFormat format = new SimpleDateFormat("mm-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-mm-dd");
        Date d = null;
        try {
            d = format2.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String time = format.format(d);
        return time;
    }
    private static SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Date getDate(String date){

        Date d = null;
        try {
            d = format2.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static String getCurrentFirstDate (){
        // 获取当前月的第一天
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        String firstday = format2.format(cale.getTime());
        return  firstday;
    }

    public static String getNextFirstDate (Date d,int m){
        // 获取当前月的第一天
        Calendar cale = Calendar.getInstance();
        cale.setTime(d);
        cale.add(Calendar.MONTH, m);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        String firstday = format2.format(cale.getTime());
        return  firstday;
    }

    public static String getNextFirstDate (String dat,int m){
        Date d = null;
        try {
            d = format2.parse(dat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 获取当前月的第一天
        Calendar cale = Calendar.getInstance();
        cale.setTime(d);
        cale.add(Calendar.MONTH, m);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        String firstday = format2.format(cale.getTime());
        return  firstday;
    }

    public static String getNextDate(String dat){
        Date d = null;
        try {
            d = format2.parse(dat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 下一天
        Calendar cale = Calendar.getInstance();
        cale.setTime(d);
        cale.add(Calendar.DATE, 1);
        String firstday = format2.format(cale.getTime());
        return  firstday;
    }
}
