package com.yun.admin.utils;

import com.yun.admin.context.UserContext;
import com.yun.bean.admin.YunUser;
import com.yun.bean.base.BasePO;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

public class ProjectUtil {

    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final String SET_TYPE = "save";

    /**
     * 更新操作人信息
     *
     * @param entity
     * @param <T>
     */
    public static <T extends BasePO> void setOperator(T entity) {
        setOperator(entity, null);
    }

    public static <T extends BasePO> void setOperator(T entity, String type) {
        YunUser yunUser = UserContext.getInstance().get();
        // 当前时间
        Date nowDate = new Date();
        // 当前用户
        String userName = yunUser.getUserName();
        if (SET_TYPE.equals(type)) {
            entity.setCreatedBy(userName);
            entity.setCreatedTime(nowDate);
        }
        entity.setUpdatedBy(userName);
        entity.setUpdatedTime(nowDate);
    }

    /**
     * 获取指定长度序号
     *
     * @param count  原序号
     * @param length 期望序号长度
     * @return
     */
    public static String getNum(String count, int length) {
        int f = length - count.length();
        if (f <= 0) {
            return count;
        }
        String num = "";
        for (int i = 0; i < f; i++) {
            num = "0" + num;
        }
        num += count;
        return num;
    }

    /**
     * 包含大小写字母及数字且在8-20位
     * 是否包含
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigit(String str) {
//        String regex = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[._~!@#$^&*])[A-Za-z0-9._~!@#$^&*]{8,20}$";
        String regex = "^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9]{8,20}$";
        return str.matches(regex);
    }

    public static boolean isNumber(String str) {
        String reg = "^[0-9]+(.[0-9]+)?$";
        return str.matches(reg);
    }

    public static String getRowNum(String num) {
        if (StringUtils.isBlank(num) || !isNumber(num)) {
            return num;
        }
        Integer rowNum = Integer.valueOf(num);
        rowNum += 1;
        return String.valueOf(rowNum);
    }
}
