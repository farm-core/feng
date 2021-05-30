package com.yun.idb.annotation;


import java.lang.annotation.*;

/**
 * @author zhanggk
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DBSource {

    DataSourceEnum value() default DataSourceEnum.DB1;
}
