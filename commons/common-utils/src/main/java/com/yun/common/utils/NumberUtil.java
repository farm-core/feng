package com.yun.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * Created by changchen on 18/01/2018.
 */
@Slf4j
public class NumberUtil {

    /**
     * 判断是数字
     *
     * @param str
     * @return
     */
    public static boolean isLegal(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    /**
     * 字符串转long
     *
     * @param str
     * @return
     */
    public static long convert2long(String str) {
        long value = -999;
        try {
            value = Long.parseLong(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 字符串转long
     *
     * @param str
     * @return
     */
    public static double convert2double(String str) {
        double value = -999;
        try {
            value = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 字符串转float
     *
     * @param str
     * @return
     */
    public static float convert2float(String str) {
        float value = -999;
        try {
            value = Float.parseFloat(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 字符串转int
     *
     * @param str
     * @return
     */
    public static int convert2int(String str) {
        int value = -999;
        try {
            value = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 判断是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
