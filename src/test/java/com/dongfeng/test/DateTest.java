package com.dongfeng.test;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author eastFeng
 * @date 2020-10-29 17:40
 */
public class DateTest {
    public static void main(String[] args) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
//        Calendar calendar = Calendar.getInstance();
//        System.out.println(dateFormat.format(calendar.getTime()));

//        System.out.println(1900%4);

//        isLeapYear(1900);

//        System.out.println(DateUtil.isWeekend(new Date(1609569373000L)));
//        String isHo = "Y";
//        if (DateUtil.isWeekend(new Date())){
//            isHo = "N";
//        }

        Date start = new Date(1642934870000L);
        Date end = new Date(1651488470000L);
        long between = DateUtil.between(DateUtil.beginOfDay(start), DateUtil.beginOfDay(end), DateUnit.DAY);
        System.out.println(between);
    }

    @Test
    public void dateRangeTest(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date today = new Date();
        DateTime beginDate = cn.hutool.core.date.DateUtil.offsetDay(today, -6);

        List<DateTime> dateList = cn.hutool.core.date.DateUtil.rangeToList(beginDate, today, DateField.DAY_OF_YEAR);

        for (DateTime time : dateList) {
            System.out.println(dateFormat.format(time));
        }

    }

    @Test
    public void month(){
        System.out.println(cn.hutool.core.date.DateUtil.weekOfYear(new Date(1609482973000L)));
        System.out.println(cn.hutool.core.date.DateUtil.month(new Date(1609482973000L)));


//        System.out.println(cn.hutool.core.date.DateUtil.dayOfWeek(new Date()));
    }

    //calendar.add
    @Test
    public void test(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 36500);
        System.out.println(dateFormat.format(calendar.getTime()));
    }

    //CST时间格式: Sun Sep 27 23:00:00 CST 2020
    @Test
    public void cstFormat(){
        try {
            String dateString = "Tue Nov 24 23:00:00 CST 2020";

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            Date date = simpleDateFormat.parse(dateString);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(df.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void isLeapYear(int year){
        if ((year%4==0 && year%100!=0) || (year%400==0)){
            System.out.println(year + "是闰年");
        }else {
            System.out.println(year + "不是闰年");
        }
    }


    @Test
    public void splitTest(){
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String today = dateFormat.format(new Date());
//        System.out.println("today: -- "+today);
//
//        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String begin = timeFormat.format(new Date(1603417839000L));
//        //根据空格" "把begin(2020-10-23 09:50:39)分成2020-10-23和09:50:39两个部分
//        String[] s = begin.split(" ");
//        //beginStr: 第二部分09:50:39  时分秒
//        String beginStr = s[1];
//        System.out.println("beginStr: -- "+beginStr);
//
//        Date date = DateUtil.parseDate(today + " " + beginStr, "yyyy-MM-dd HH:mm:ss");
//        System.out.println(date.getTime());
//        System.out.println(timeFormat.format(date));
    }
}

