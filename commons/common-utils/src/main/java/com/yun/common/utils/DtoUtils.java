package com.yun.common.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;

/**
 * @program: datacenter
 * @description: 类转换工具，beanUtils拓展类
 * @author: wxf
 * @date: 2020-01-02 11:50
 **/

public class DtoUtils {

    /**
     * 用于获取空值字段
     *
     * @param source Object
     * @return String[]
     * @author zhanggk
     * @date 2020/1/2 11:52
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 复制对象，忽略null值字段
     *
     * @param src    原始对象;
     * @param target 目标对象
     * @author zhanggk
     * @date 2020/1/2 11:55
     */
    public static void copyProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }
}
