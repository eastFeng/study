package com.dongfeng.study.util;

import cn.hutool.core.date.DateTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 建议使用hutool工具类 : cn.hutool.core.date.DateUtil
 *
 * @author eastFeng
 * @date 2020/8/15 - 14:42
 */
@Slf4j
public class DateUtil {
    public static void main(String[] args) {
        test_format();
    }

    public static void test_format(){
        // yyyyMMdd
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        // yyyy-MM-dd
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        // HH: 24小时制
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // yyyyMMdd HH:mm:ss
        SimpleDateFormat sdf4 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        // hh: 12小时制
        SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        // yyyyMMdd hh:mm:ss
        SimpleDateFormat sdf6 = new SimpleDateFormat("yyyyMMdd hh:mm:ss");
        // yyyy年MM月dd日HH时mm分ss秒
        SimpleDateFormat sdf7 = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        // SSS: 毫秒
        SimpleDateFormat sdf8 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        // yyyy/MM/dd
        SimpleDateFormat sdf9 = new SimpleDateFormat("yyyy/MM/dd");
        // M/d
        SimpleDateFormat sdf10 = new SimpleDateFormat("M/d");
        // dd : 4日会展示为04
        SimpleDateFormat sdf11 = new SimpleDateFormat("dd");

        // d : 4日会展示为4
        SimpleDateFormat sdf12 = new SimpleDateFormat("d");

        System.out.println(sdf1.format(System.currentTimeMillis()));
        System.out.println(sdf2.format(System.currentTimeMillis()));
        System.out.println(sdf3.format(System.currentTimeMillis()));
        System.out.println(sdf4.format(System.currentTimeMillis()));
        System.out.println(sdf5.format(System.currentTimeMillis()));
        System.out.println(sdf6.format(System.currentTimeMillis()));
        System.out.println(sdf7.format(System.currentTimeMillis()));
        System.out.println(sdf8.format(System.currentTimeMillis()));
        System.out.println(sdf9.format(System.currentTimeMillis()));
        System.out.println(sdf10.format(System.currentTimeMillis()));
        System.out.println(sdf11.format(System.currentTimeMillis()));
        System.out.println(sdf12.format(System.currentTimeMillis()));
    }

    public static void test(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        //hutool的日期工具类很好用
        DateTime beginOfDay = cn.hutool.core.date.DateUtil.beginOfDay(date);
        DateTime endOfDay = cn.hutool.core.date.DateUtil.endOfDay(date);

        //apache lang3包下的日期工具类也好用
        Date truncate = DateUtils.truncate(date, Calendar.DATE);

        System.out.println("现在时间  "+dateFormat.format(date));
        System.out.println("开始时间  "+dateFormat.format(beginOfDay));
        System.out.println("结束时间  "+dateFormat.format(endOfDay));
        System.out.println("truncate "+dateFormat.format(truncate));


        Date date1 = new Date(1602904807000L);
        int compare = cn.hutool.core.date.DateUtil.compare(date, date1);
        System.out.println("hutool compare: "+compare);

        System.out.println("my compare:");
        if (date1.getTime()>date.getTime()){
            System.out.println(1);
        }else if (date1.getTime()<date.getTime()){
            System.out.println(-1);
        }else {
            System.out.println(0);
        }
    }

    /**
     * 指定日期是否是周末（周六或者周日）
     */
    public static boolean isWeekend(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
    }


    /**
     * 字符串转Date对象
     */
    public static Date parseDate(String dateStr, String format){
        Date date = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            date = dateFormat.parse(dateStr);
        } catch (Exception e) {
            log.info("parseTime error:{}", e.getMessage(), e);
        }
        return date;
    }

    /**
     * @return 获取该Date的开始处 00:00:00
     */
    public static Date getDateStart(Date date){
        if (date==null){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 时
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        // 分
        calendar.set(Calendar.MINUTE, 0);
        // 秒
        calendar.set(Calendar.SECOND, 0);
        // 毫秒
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * @return 获取该Date的结束处 23:59:59
     */
    public static Date getDateEnd(Date date){
        if (date == null){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 时
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        // 分
        calendar.set(Calendar.MINUTE, 59);
        // 秒
        calendar.set(Calendar.SECOND, 59);
        // 毫秒
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 获取前X天的日期，X=0时就是当前的日期
     */
    public static Date previousXDay(int x){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, -24*x);
        return calendar.getTime();
    }

    /**
     * 获取后X天的日期，X=0时就是当前的日期
     */
    public static Date afterXDay(int x){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 24*x);
        return calendar.getTime();
    }

    /**
     * 计算两个Date直接相差的天数
     * @param start 开始时间
     * @param end 结束时间
     * @return 两个时间相差的天数
     */
    public static long differenceTwoDate(Date start, Date end){
        final long difference = Math.abs(end.getTime() - start.getTime());
        //两个时间相差的毫秒值除以一天的毫秒值，就是两个时间相差的天数
        return difference / (1000 * 60 * 60 * 24);
    }

    /**
     * 字符串转换到时间格式
     *
     * @param dateStr 需要转换的字符串
     * @param formatStr 需要格式的目标字符串 举例 yyyy-MM-dd
     * @return Date 返回转换后的时间
     * @throws ParseException 转换异常
     */
    public static Date stringToDate(String dateStr, String formatStr) {
        DateFormat sdf = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            log.error("error:{}", e.getMessage(), e);
        }
        return date;
    }


    /**
     * 判断时间是否在两个时间的区间内(闭包)
     * @param compareTime 要判断的时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return true在  false不在
     */
    public static boolean isInTime(Date compareTime,Date startTime, Date endTime) {
        return compareTime.getTime() >= startTime.getTime() && compareTime.getTime()<= endTime.getTime();
    }
}
