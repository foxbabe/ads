package com.sztouyun.advertisingsystem.utils;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.common.UnitEnum;
import com.sztouyun.advertisingsystem.utils.excel.TimeFormatEnum;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.springframework.util.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by wenfeng on 2017/8/2.
 */
public class DateUtils {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
    private static SimpleDateFormat ymdSdf = new SimpleDateFormat(Constant.DATA_YMD_CN);
    private static SimpleDateFormat defaultSdf = new SimpleDateFormat(Constant.DEATULT_DATETIME,Locale.UK);

    public static Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTime(date);
        return calendar;
    }

    public static String getCurrentFormat() {
        return sdf.format(new Date());
    }

    public static String getDateFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String dateFormat(Date date,String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 字符串转换成字符串
     * @param str
     * @param pattern
     * @return date
     */
    public static String strTostr(String str, String pattern) {
        Date date = strToDate(str, Constant.DATETIME);

        return dateFormat(date, pattern);
    }

    /**
     * 获取时间段间隔天数
     *
     * @param sDate
     * @param eDate
     * @return
     */
    public static int getIntervalDays(Date sDate, Date eDate) {
        try {
            long timeSubtraction = eDate.getTime() - sDate.getTime();
            long dayMilliSecond = 24 * 60 * 60 * 1000;
            long day = timeSubtraction/ dayMilliSecond;
            long mod = timeSubtraction % dayMilliSecond;
            if( mod != 0) ++day;
            return (int) day;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @param format
     * @return date
     */
    public static Date strToDate(String str, String format) {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getMaxDate() {
        return strToDate("9999-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
    }

    public static Date getMinDate() {
        return strToDate("1970-01-01 00:00:000", "yyyy-MM-dd HH:mm:ss");
    }

    public static  String getDiffMonth(Date start,Date end){
        int days=getIntervalDays(start,end);
        return  new BigDecimal(days/30).setScale(0, BigDecimal.ROUND_HALF_UP)+"个月";
    }

    public static Date getDateAccurateToMinute(Date date){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static  Date addDays(Date date, int days){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH,days);
        return calendar.getTime();
    }

    public static  Date addMonths(Date date, int months){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,months);
        return calendar.getTime();
    }

    /**
     * 格式化XX月XX天
     * @param days
     * @return
     */
    public static String formate(Integer days){
        StringBuffer result=new StringBuffer();
        int month=days/30;
        int leftDay=days%30;
        if(month>0){
            result.append(month).append("个月");
        }
        if(leftDay>0){
            if(leftDay<10 && month>0){
                result.append("0");
            }
            result.append(leftDay).append("天");
        }
        return result.toString();
    }
    /**
     * 格式化yy年XX月XX天
     * @param days
     * @return
     */
    public static String formateYmd(Integer days){
        StringBuffer result=new StringBuffer();
        int year=days/360;
        int daysForMonth=days%360;
        int month=daysForMonth/30;
        int leftDay=daysForMonth%30;
        if(year>0){
            result.append(year).append("年");
        }
        if(month>0){
            result.append(month).append("个月");
        }
        if(leftDay>0){
            result.append(leftDay).append("天");
        }
        return result.toString();
    }

    /**
     * 格式化yy年XX月XX天XX小时XX分钟
     */
    public static String formateYmdhm(Date sDate,Date eDate){
        long timeSubtraction = eDate.getTime() - sDate.getTime();
        long dayMilliSecond = 24 * 60 * 60 * 1000;
        long day = timeSubtraction/ dayMilliSecond;
        long year=day/360;
        long daysForMonth=day%360;
        long month=daysForMonth/30;
        long leftDay=daysForMonth%30;
        long hours = timeSubtraction % dayMilliSecond/(60*60*1000);
        long minute = timeSubtraction % dayMilliSecond%(60*60*1000)/(60*1000);
        if( timeSubtraction % dayMilliSecond%(60*60*1000)%(60*1000) != 0) ++minute;
        StringBuffer result=new StringBuffer();
        if(year>0){
            result.append(year).append("年");
        }
        if(month>0){
            result.append(month).append("个月");
        }
        if(leftDay>0){
            result.append(leftDay).append("天");
        }
        if(hours>0){
            result.append(hours).append("小时");
        }
        if(minute > 0) {
            result.append(minute).append("分钟");
        }
        return result.toString();
    }

    /**
     * 判断同年同月同日
     * @param sDate
     * @param eDate
     * @return
     */
    public static boolean isEqualsDateByYearMonthDay(Date sDate, Date eDate) {
        Calendar sCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        sCalendar.setTime(sDate);
        Calendar eCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        eCalendar.setTime(eDate);
        return sCalendar.get(Calendar.YEAR) == eCalendar.get(Calendar.YEAR) && sCalendar.get(Calendar.MONTH) == eCalendar.get(Calendar.MONTH) && sCalendar.get(Calendar.DAY_OF_MONTH) == eCalendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 判断同年同月
     * @param sDate yyyy-MM
     * @param eDate yyyy-MM
     * @return
     */
    public static boolean isEqualsDateByYearMonth(Date sDate, Date eDate) {
        LocalDate sLocalDate = new LocalDate(sDate);
        LocalDate eLocalDate = new LocalDate(eDate);
        return sLocalDate.getYear() == eLocalDate.getYear() && sLocalDate.getMonthOfYear() == eLocalDate.getMonthOfYear();
    }

    public static boolean isEqualsDateByYearMonth(Date sDate, Integer year,Integer month) {
        LocalDate localDate = new LocalDate(sDate);
        return localDate.getYear() == year && localDate.getMonthOfYear() == month;
    }

    public static Date getDateStartByZero(Date date) {
        date.setHours(0);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 24);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
        return calendar.getTime();
    }

    public static Date getPureDate(Date date){
        return new DateTime(date).toLocalDate().toDate();
    }

    /**
     * joda不支持EEE MMM dd HH:mm:ss Z yyyy的格式
     * @param date
     * @return
     */
    public static String dateFormat(String date, TimeFormatEnum timeFormatEnum){
        try {
            return timeFormatEnum.getSimpleDateFormat().format( defaultSdf.parse(date));
        } catch (ParseException e) {
            throw new BusinessException("时间格式转换失败");
        }
    }

    /**
     *
     * @param dBegin
     * @param dEnd
     * @return
     */
    public static List<Date> getDateList(Date dBegin, Date dEnd,int type) {
        List<Date> dateList = new ArrayList();
        dateList.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(dEnd);
        while (dEnd.after(calBegin.getTime())) {
            calBegin.add(type, 1);
            dateList.add(calBegin.getTime());
        }
        return dateList;
    }

    public static LocalDate getFixedDayOfCurrentMonth(Integer dayOfMonth) {
        return LocalDate.now().toDateTimeAtStartOfDay().withDayOfMonth(dayOfMonth).toLocalDate();
    }

    public static Date addTime(Date src,int type,int value){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTime(src);
        calendar.add(type,value);
        return calendar.getTime();
    }


    public static String unitFormat(long times) {
        String retStr = null;
        if (times >= 0 && times < 10)
            retStr = "0" + Long.toString(times);
        else
            retStr = "" + times;
        return retStr;
    }

    public static String formatOneDayTimes(long seconds) {
        long day = TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - TimeUnit.DAYS.toHours(day);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.DAYS.toMinutes(day) - TimeUnit.HOURS.toMinutes(hours);
//        long second = TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.DAYS.toSeconds(day) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minute);

        StringBuffer sb = new StringBuffer();
        if(day > 0) {
            sb.append(day).append("天");
        }
        sb.append(hours).append("小时");
        if(minute > 0) {
            sb.append(unitFormat(minute)).append("分钟");
        }
//        if(second > 0) {
//            sb.append(unitFormat(second)).append("秒");
//        }
        return sb.toString();
    }

    /**
     * 时间转毫秒数
     * @return
     */
    public static Long timeToMillisecond(Integer value, UnitEnum unitEnum){

        switch (unitEnum){
            case Day:
                return value*24*60*60*1000L;
            case HOUR:
                return value*60*60*1000L;
            case DATE:
                return value*24*60*60*1000L;
            case MINUTE:
                return value*60*1000L;
        }
        return null;
    }

    public static boolean loe(Date date1,Date date2){
        return date1.getTime()<=date2.getTime();
    }

    public static boolean goe(Date date1,Date date2){
        return date1.getTime()>=date2.getTime();
    }

    public static boolean lt(Date date1,Date date2){
        return date1.getTime()<date2.getTime();
    }

    public static boolean gt(Date date1,Date date2){
        return date1.getTime()>date2.getTime();
    }

}