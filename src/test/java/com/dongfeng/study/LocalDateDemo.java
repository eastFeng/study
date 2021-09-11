package com.dongfeng.study;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Date;

/**
 * @author eastFeng
 * @date 2020/8/26 - 14:05
 */
public class LocalDateDemo {
    public static void main(String[] args) {
        formatter();
    }

    /**
     * LocalDateTime转换为我们熟悉的时间格式
     */
    public static void formatter(){
        final LocalDateTime now = LocalDateTime.now();
        System.out.println("now: "+now);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("formatter: "+formatter.format(now));
    }

    /**
     * LocalDateTime转毫秒时间戳
     */
    public static void timestamp(){
        final LocalDateTime now = LocalDateTime.now();
        System.out.println("now: "+now);
        // 方法一
        long epochMilli = now.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        System.out.println("epochMilli: "+epochMilli);
        // 方法二
        long l = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        System.out.println("epochMilli: "+l);
    }

    /**
     * 时间戳转LocalDateTime
     */
    public static void timestampToLocal(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        long timestamp = 1598400000000L;
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);

        System.out.println(dateFormat.format(new Date(timestamp)));

        System.out.println(localDateTime);
        System.out.println(timeFormatter.format(localDateTime));
    }


    /**
     * LocalDate是日期处理类
     */
    public static void localDate_test(){
        // 获取当前日期
        LocalDate now = LocalDate.now();
        System.out.println("now: "+now);
        // 获取年
        int year = now.getYear();
        System.out.println("year: "+year);
        // 获取月
        Month month = now.getMonth();
        System.out.println("month: "+month);
        // 获取月
        int monthValue = now.getMonthValue();
        System.out.println("monthValue: "+monthValue);
        // 一年中的第几天
        int dayOfYear = now.getDayOfYear();
        System.out.println("dayOfYear: "+dayOfYear);
        // 本月的第几天
        int dayOfMonth = now.getDayOfMonth();
        System.out.println("dayOfMonth: "+dayOfMonth);
        // 星期
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        System.out.println("dayOfWeek: "+dayOfWeek);
        // 星期
        int dayOfWeek1 = now.get(ChronoField.DAY_OF_WEEK);
        System.out.println("dayOfWeek1: "+dayOfWeek1);

        // 设置日期
        LocalDate localDate = LocalDate.of(2020, 7, 17);
        System.out.println("localDate: "+localDate);
    }


    /**
     * LocalTime是时间处理类
     */
    public static void localTime_test(){
        // 获取当前时间
        LocalTime now = LocalTime.now();
        System.out.println("now: "+now);
        // 获取小时
        int hour = now.getHour();
        System.out.println("hour: "+hour);
        // 获取分
        int minute = now.getMinute();
        System.out.println("minute: "+minute);
        // 获取秒
        int second = now.getSecond();
        System.out.println(second);
        // 当前时间的毫秒
        int nano = now.getNano();
        System.out.println("nano: "+nano);

        // 设置时间
        LocalTime localTime = LocalTime.of(13, 51, 10);
        System.out.println("localTime: "+localTime);
    }

    /**
     * LocalDateTime可以设置年月日时分秒，相当于LocalDate + LocalTime
     */
    public static void localDateTime_test(){
        // 获取当前日期时间
        LocalDateTime now = LocalDateTime.now();
        System.out.println("now: "+now);
        // 获取LocalDate
        LocalDate localDate = now.toLocalDate();
        System.out.println("localDate: "+localDate);
        // 获取LocalTime
        LocalTime localTime = now.toLocalTime();
        System.out.println("localTime: "+localTime);

        //
        int nano = now.getNano();
        System.out.println("nano: "+nano);
    }

    /**
     * Instant
     */
    public static void instant_test(){
        // 创建Instant对象
        Instant now = Instant.now();
        // 获取秒
        long epochSecond = now.getEpochSecond();
        System.out.println("epochSecond: "+epochSecond);
        // 获取毫秒
        long epochMilli = now.toEpochMilli();
        System.out.println("epochMilli: "+epochMilli);
    }


}
