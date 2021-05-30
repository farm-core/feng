package com.yun.gateway.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: datacenter
 * @description: 时间范围工具类
 * @author: zhanggk
 * @create: 2020-05-21 15:11
 **/

public class ObsTimeUtil {
    private static final SimpleDateFormat FY = new SimpleDateFormat("yyyy");
    private static final SimpleDateFormat FM = new SimpleDateFormat("yyyyMM");
    private static final SimpleDateFormat FD = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat FH = new SimpleDateFormat("yyyyMMddhh");
    private static final SimpleDateFormat FF = new SimpleDateFormat("yyyyMMddhhmm");
    private static final SimpleDateFormat FS = new SimpleDateFormat("yyyyMMddhhmmss");

    public static Date parseDate(String value) {
        Date time;
        try {
            if (value.length() == 4) {
                time = FY.parse(value);
            }
            if (value.length() == 6) {
                time = FM.parse(value);
            } else if (value.length() == 8) {
                time = FD.parse(value);
            }else if (value.length() == 10) {
                time = FH.parse(value);
            }else if (value.length() == 12) {
                time = FF.parse(value);
            } else if (value.length() == 14) {
                time = FS.parse(value);
            } else {
                time = FS.parse(value);
            }

        } catch (ParseException e) {
            return null;
        }

        return time;
    }
}
