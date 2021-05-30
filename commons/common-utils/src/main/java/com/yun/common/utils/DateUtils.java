package com.yun.common.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {
    private static final SimpleDateFormat fmtYMD = new SimpleDateFormat("yyyyMMdd");

    /**
     * @param date
     * @param interval
     * @param number
     * @return 功能:日期加减 获得指定时间前后时间
     * @author wxf
     * @date 2017-7-7 下午5:04:29
     */
    public static Date dateAdd(Date date, String interval, int number) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if ("y".equals(interval)) {
            cal.add(Calendar.YEAR, number);
        } else if ("M".equals(interval)) {
            cal.add(Calendar.MONTH, number);
        } else if ("d".equals(interval)) {
            cal.add(Calendar.DATE, number);
        } else if ("h".equals(interval) || "H".equals(interval)) {
            cal.add(Calendar.HOUR, number);
        } else if ("m".equals(interval)) {
            cal.add(Calendar.MINUTE, number);
        } else if ("s".equals(interval)) {
            cal.add(Calendar.SECOND, number);
        } else {
            return null;
        }
        return cal.getTime();
    }

    public static String dateAdd(String dateStr, String interval, int number, String frmt) {
        Date date = DateUtils.strToDate(dateStr, frmt);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if ("y".equals(interval)) {
            cal.add(Calendar.YEAR, number);
        } else if ("M".equals(interval)) {
            cal.add(Calendar.MONTH, number);
        } else if ("d".equals(interval)) {
            cal.add(Calendar.DATE, number);
        } else if ("h".equals(interval) || "H".equals(interval)) {
            cal.add(Calendar.HOUR, number);
        } else if ("m".equals(interval)) {
            cal.add(Calendar.MINUTE, number);
        } else if ("s".equals(interval)) {
            cal.add(Calendar.SECOND, number);
        } else {
            return null;
        }
        return DateUtils.dateToStr(cal.getTime(), frmt);
    }

    /**
     * @param startDate
     * @param endDate
     * @param interval  时间间隔 d天 H小时 ...
     * @return 功能：获得时间段内的时间集合
     * @author jidengke
     * @date 2017-6-27 下午3:04:29
     */
    public static List<Date> getDateList(Date startDate, Date endDate, String interval) {
        List<Date> resList = new ArrayList<>();
        if (startDate.before(endDate) || startDate.equals(endDate)) {
            while (startDate.before(endDate)) {
                resList.add(startDate);
                startDate = DateUtils.dateAdd(startDate, interval, 1);
            }
            resList.add(endDate);
        }
        return resList;
    }

    /**
     * @param date
     * @return 功能:获得时间是周几
     * @author jidengke
     * @date 2017-6-15 下午5:22:29
     */
    public static String getWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.DAY_OF_WEEK);//1为周日 7为周六
        String res = "";
        if (week == 1) {
            res = "周日";
        } else if (week == 2) {
            res = "周一";
        } else if (week == 3) {
            res = "周二";
        } else if (week == 4) {
            res = "周三";
        } else if (week == 5) {
            res = "周四";
        } else if (week == 6) {
            res = "周五";
        } else if (week == 7) {
            res = "周六";
        }
        return res;
    }

    /**
     * @param date
     * @return 功能:日期转字符串
     * @author wxf
     * @date 2016-5-31 下午5:22:29
     */
    public static String dateToStr(Date date, String formatstr) {
        SimpleDateFormat format = new SimpleDateFormat(formatstr);
        String str = format.format(date);
        return str;
    }

    /**
     * @param formatstr
     * @return 功能：获取当前指定格式时间字符串
     * @author wxf
     * @date 2016-6-3 下午4:08:09
     */
    public static String getNewDate(String formatstr) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(formatstr);
        String str = format.format(date);
        return str;
    }

    /**
     * @param str
     * @return 功能：字符串转日期
     * @author wxf
     * @date 2016-5-31 下午5:22:15
     */
    public static Date strToDate(String str, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int hoursBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * @param specifiedDay 指定日期
     * @param format       格式
     * @param day          数值 负数为前几天，正数为后几天
     * @return 功能：计算指定日期前几天或后几天日期
     * @author wxf
     * @date 2016-6-3 下午4:00:10
     */
    public static String getSpecifiedDay(String specifiedDay, String format, int day) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = simpleDateFormat.parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        c.add(Calendar.DATE, day);
        String dayBefore = simpleDateFormat.format(c.getTime());
        return dayBefore;
    }

    /**
     * @param specifiedHour 日期字符串
     * @param format        格式
     * @param hour          数值 负数为前几小时，正数为后几小时
     * @return 功能：计算指定时间前或后几小时时间
     * @author wxf
     * @date 2016-6-3 下午4:05:58
     */
    public static String getSpecifiedHour(String specifiedHour, String format, int hour) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = simpleDateFormat.parse(specifiedHour);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        c.add(Calendar.HOUR, hour);
        String dayBefore = simpleDateFormat.format(c.getTime());
        return dayBefore;
    }

    /**
     * @param specifiedHour 日期字符串
     * @param format        格式
     * @param month         数值 负数为前几月，正数为后几月
     * @return 功能：计算指定时间前或后几小时时间
     * @author wxf
     * @date 2016-6-3 下午4:05:58
     */
    public static String getSpecifiedMonth(String specifiedHour, String format, int month) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = simpleDateFormat.parse(specifiedHour);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        c.add(Calendar.MONTH, month);
        String dayBefore = simpleDateFormat.format(c.getTime());
        return dayBefore;
    }

    /**
     * @param specifiedHour 日期字符串
     * @param format        格式
     * @param year          数值 负数为前几年，正数为后几年
     * @return 功能：计算指定时间前或后几小时时间
     * @author wxf
     * @date 2016-6-3 下午4:05:58
     */
    public static String getSpecifiedYear(String specifiedHour, String format, int year) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = simpleDateFormat.parse(specifiedHour);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        c.add(Calendar.YEAR, year);
        String dayBefore = simpleDateFormat.format(c.getTime());
        return dayBefore;
    }

    public static Timestamp strToTimestamp(String str, String format) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String tsStr = dateToStr(strToDate(str, format), "yyyy-MM-dd HH:mm:ss");
        try {
            ts = Timestamp.valueOf(tsStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ts;
    }

    //通过日期计算该日期为一年中第几天
    public static String getDateByDayOfYear(String param) throws ParseException {
        String year = "20" + param.substring(0, 2);
        int dayOfYear = Integer.valueOf(param.substring(2, param.length())) - 1;
        Date date1 = new Date();
        String yyyy = DateUtils.dateToStr(date1, "yyyy");
        Date start = new SimpleDateFormat("yyyyMMdd").parse(yyyy + "0101");
        Calendar ca = Calendar.getInstance();
        ca.setTime(start);
        ca.add(Calendar.DATE, dayOfYear);
        String month = "";
        String date = "";
        if (ca.get(Calendar.MONTH) + 1 < 10) {
            month = "0" + String.valueOf(ca.get(Calendar.MONTH) + 1);
        } else {
            month = String.valueOf(ca.get(Calendar.MONTH) + 1);
        }
        if (ca.get(Calendar.DATE) < 10) {
            date = "0" + String.valueOf(ca.get(Calendar.DATE));
        } else {
            date = String.valueOf(ca.get(Calendar.DATE));
        }
        return String.valueOf(ca.get(Calendar.YEAR)) + month + date;
    }

    //通过一年中第几天来计算日期
    public static String getDayOfYearByDate(String date) throws ParseException {
        Date dd = new SimpleDateFormat("yyyyMMdd").parse(date);
        Calendar ca = Calendar.getInstance();
        ca.setTime(dd);
        String year = String.valueOf(ca.get(Calendar.YEAR)).substring(2, 4);
        int i = ca.get(Calendar.DAY_OF_YEAR);
        String index = "";
        if (i < 10) {
            index = "00" + String.valueOf(i);
        } else if (i >= 10 && i < 100) {
            index = "0" + String.valueOf(i);
        } else {
            index = String.valueOf(i);
        }
        return year + index;
    }

    //通过一年中第几天来计算日期
    public static Integer getDayOfYear(String date) throws ParseException {
        Date dd = new SimpleDateFormat("yyyyMMdd").parse(date);
        Calendar ca = Calendar.getInstance();
        ca.setTime(dd);
        return ca.get(Calendar.DAY_OF_YEAR);
    }

    //字符串类型日期转换
    public static String dateStrFormat(String date, String format, String toFormat) throws ParseException {
        SimpleDateFormat sFMT = new SimpleDateFormat(format);
        SimpleDateFormat tFMT = new SimpleDateFormat(toFormat);
        Date parse = sFMT.parse(date);
        String format1 = tFMT.format(parse);
        return format1;
    }

    public static List<Date> getRoundDate(String rule, Short interval, Short round) {
        ArrayList<Date> dates = new ArrayList<>();
        Date date = null;
        String dateToStr = null;
        switch (rule) {
            case "day":
                rule = "d";
                dateToStr = DateUtils.dateToStr(new Date(), "yyyy-MM-dd 00:00:00.000");
                date = DateUtils.strToDate(dateToStr, "yyyy-MM-dd HH:mm:ss.sss");
                break;
            case "hour":
                rule = "h";
                dateToStr = DateUtils.dateToStr(new Date(), "yyyy-MM-dd HH:00:00.000");
                date = DateUtils.strToDate(dateToStr, "yyyy-MM-dd HH:mm:ss.sss");
                break;
            case "minute":
                rule = "m";
                dateToStr = DateUtils.dateToStr(new Date(), "yyyy-MM-dd HH:mm:00.000");
                date = DateUtils.strToDate(dateToStr, "yyyy-MM-dd HH:mm:ss.sss");
                break;
        }
        for (int i = 0; i < round; i++) {
            Date dateTemp = DateUtils.dateAdd(date, rule, -i * interval);
            dates.add(dateTemp);
        }
        return dates;
    }

    /**
     * 获取精确到秒的时间戳
     *
     * @return
     */
    public static int getSecondTimestamp(Date date) {
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime());
        int length = timestamp.length();
        if (length > 3) {
            return Integer.valueOf(timestamp.substring(0, length - 3));
        } else {
            return 0;
        }
    }

    /**
     * 获取指定日期第一天
     *
     * @param dateYm 空则获取当月
     * @return
     */
    public static String getFirstDayOfMonth(String dateYm) {
        //获取前月的第一天
        Calendar cale = Calendar.getInstance();//获取当前日期
        if (dateYm != null) {
            cale.set(Calendar.YEAR, Integer.valueOf(dateYm.substring(0, 4)));
            cale.set(Calendar.MONTH, Integer.valueOf(dateYm.substring(4, 6)) - 1);
        }
        cale.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String firstDay = fmtYMD.format(cale.getTime());
        return firstDay;
    }

    /**
     * 获取指定日期最后一天
     *
     * @param dateYm 为空则默认为当月
     * @return
     */
    public static String getLastDayOfMonth(String dateYm) {
        Calendar cale = Calendar.getInstance();
        if (dateYm != null) {
            cale.set(Calendar.YEAR, Integer.valueOf(dateYm.substring(0, 4)));
            cale.set(Calendar.MONTH, Integer.valueOf(dateYm.substring(4, 6)));
        } else {
            cale.add(Calendar.MONTH, 1);
        }
        cale.set(Calendar.DAY_OF_MONTH, 0);//设置为1号,当前日期既为本月第一天
        String lastDay = fmtYMD.format(cale.getTime());
        return lastDay;
    }

    /**
     * 获取开始日期到结束日期中的所有日期列表
     *
     * @param
     * @return
     */
    public static List<String> dateRange(String startTime, String endTime, String paramFormat, String displayFormat, int
            interval) throws ParseException {
        SimpleDateFormat pmat = new SimpleDateFormat(paramFormat);
        SimpleDateFormat dmat = new SimpleDateFormat(displayFormat);
        Date stime = pmat.parse(startTime);
        Date etime = pmat.parse(endTime);
        List<String> items = new ArrayList<>();
        long itemTime = stime.getTime();
        while (itemTime < etime.getTime()) {
            String currentTimeString = dmat.format(itemTime);
            items.add(currentTimeString);
            itemTime += interval;
        }
        return items;
    }

    /**
     * @param year
     * @param month
     * @return 某月最大天数
     */
    public static int dayCount(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        int countDay = c.getActualMaximum(Calendar.DATE);
        return countDay;
    }

    public static Map<String, String> getChainRelRatio(String obsTime1, String obsTime2) {
        List<String> monthBetween = getMonthBetween(obsTime1.substring(0, 6), obsTime2.substring(0, 6));
        String startTime = dateAdd(obsTime1, "M", -monthBetween.size(), "yyyyMMddHH");
        String endTime = dateAdd(obsTime2, "M", -monthBetween.size(), "yyyyMMddHH");
        Map<String, String> timeMap = new HashMap<>();
        timeMap.put("obsTime1", startTime);
        timeMap.put("obsTime2", endTime);
        return timeMap;

    }

    public static List<String> getMonthBetween(String minDate, String maxDate) {
        ArrayList<String> result = new ArrayList<String>();
        //格式化为年月
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        try {
            min.setTime(sdf.parse(minDate));
            min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

            max.setTime(sdf.parse(maxDate));
            max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }
        min = null;
        max = null;
        curr = null;
        return result;
    }

    /**
     * 获取当前num个小时的时间
     * 分秒都为0
     *
     * @param num
     * @return
     */
    public static Date getBeforeHoursDate(int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, num);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * @param specifiedHour 日期字符串
     * @param format        格式
     * @param hour          数值 负数为前几小时，正数为后几小时
     * @return 功能：计算指定时间前或后几小时时间
     * @author hmy
     * @date 2020-08-31 下午4:05:58
     */
    public static Date getSfHour(String specifiedHour, String format, int hour) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        Date resultDate = null;
        try {
            Date date = simpleDateFormat.parse(specifiedHour);
            c.setTime(date);
            c.add(Calendar.HOUR, hour);
            resultDate = c.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultDate;
    }

    /**
     * 获取传入时间前n天20时的时间
     *
     * @param num
     * @return
     */
    public static Date getBeforeDayDate(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, num);
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取传入时间前n天时间
     *
     * @param num
     * @return
     */
    public static Date getBeforeDay(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, num);
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 将传入时间时分秒置0
     *
     * @return
     */
    public static Date getDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 分割集合
     *
     * @param <T> 泛型
     */
    public static class PartitionList<T> {
        public Map<Integer, List<T>> partition(List<T> originList, int capacity) {
            if (originList.isEmpty() || capacity < 1) {
                return null;
            }
            Map<Integer, List<T>> map = new HashMap<>(10);

            int size = originList.size();
            int count = ((Double) Math.ceil(size * 1.0 / capacity)).intValue();
            for (int i = 0; i < count; i++) {
                int end = capacity * (i + 1);
                if (end > size) {
                    end = size;
                }
                map.put(i, originList.subList(capacity * i, end));
            }
            return map;
        }
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String t, String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(s);
        long lt = new Long(t);
        Date date = new Date(lt);
        return simpleDateFormat.format(date);
    }

    /**
     * 获取传入时间的小时
     *
     * @param date
     * @return
     */
    public static String getHourByDate(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        int hour = ca.get(Calendar.HOUR_OF_DAY);
        if (hour < 10) {
            return "0" + hour;
        }
        return hour + "";
    }

    public static void main(String[] args) {
        System.out.println(getHourByDate(DateUtils.getBeforeHoursDate(-8)));
        System.out.println(getHourByDate(new Date()));
    }
}
