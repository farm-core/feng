package com.yun.gateway.utils;

import java.util.Date;

public class GetDateTime {
    public static long getDatePoor(Date endDate, Date nowDate, String type) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        if ("day".equals(type)) {
            return day;
        } else if ("hour".equals(type)) {
            return hour + day * 24;
        } else if ("min".equals(type)) {
            return min + day * 24 * 60 + hour * 60;
        }
//        return day + "天" + hour + "小时" + min + "分钟";
        return -1;
    }
}
