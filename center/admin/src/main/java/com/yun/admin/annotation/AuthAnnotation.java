package com.yun.admin.annotation;

import com.yun.bean.enums.RoleEnum;

import java.lang.annotation.*;

/**
 * 自定义权限校验注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthAnnotation {
    RoleEnum role() default RoleEnum.ROLE1;
}
