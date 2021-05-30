package com.yun.common.utils;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 * 给对象属性赋默认值
 */
public class Evaluat {
    @SneakyThrows
    public static Object getQueryCriteria1(Object fullProcessDetail) {
        Class cls = fullProcessDetail.getClass();
        Field[] fields = fullProcessDetail.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            String type = f.getGenericType().toString();
            String name = f.getName();    //获取属性的名字

            name = name.substring(0, 1).toUpperCase() + name.substring(1); //将属性的首字符大写，方便构造get，set方法
            f.setAccessible(true);
            if ("class java.lang.String".equals(type)) {
                String value = (String) f.get(fullProcessDetail);
                if (value != null && !"".equals(value)) {
                    f.set(fullProcessDetail, ((String) f.get(fullProcessDetail)).trim());
                } else {
                    f.set(fullProcessDetail, "-999");
                }
            }

            if ("class java.lang.Double".equals(type)) {
                Double value = (Double) f.get(fullProcessDetail);
                if (value != null && !"".equals(value)) {
                    f.set(fullProcessDetail, (Double) f.get(fullProcessDetail));
                } else {
                    f.set(fullProcessDetail, -999d);
                }
            }

            if ("class java.lang.Float".equals(type)) {
                Float value = (Float) f.get(fullProcessDetail);
                if (value != null && !"".equals(value)) {
                    f.set(fullProcessDetail, (Float) f.get(fullProcessDetail));
                } else {
                    f.set(fullProcessDetail, -999f);
                }
            }

            if ("class java.lang.Integer".equals(type)) {
                Integer value = (Integer) f.get(fullProcessDetail);
                if (value != null && !"".equals(value)) {
                    f.set(fullProcessDetail, (Integer) f.get(fullProcessDetail));
                } else {
                    f.set(fullProcessDetail, -999);
                }
            }

            if ("class java.math.BigDecimal".equals(type)) {
                Method m = cls.getClass().getMethod("get" + name);
                BigDecimal value = (BigDecimal) m.invoke(cls);    //调用getter方法获取属性值
                if (value != null && !"".equals(value)) {
                    f.set(fullProcessDetail, f.get(fullProcessDetail).toString());
                }
            }

        }

        return fullProcessDetail;
    }
}
