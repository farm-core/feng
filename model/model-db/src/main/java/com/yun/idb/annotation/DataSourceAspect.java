package com.yun.idb.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Order(-1)
public class DataSourceAspect {

    @Pointcut("@within(com.yun.idb.annotation.DBSource) || @annotation(com.yun.idb.annotation.DBSource)")
    public void pointcutConfig(){

    }

    @Before("pointcutConfig()")
    public void before(JoinPoint joinPoint){
        //获得当前访问的class
        Class<?> className = joinPoint.getTarget().getClass();
        //获得访问的方法名
        String methodName = joinPoint.getSignature().getName();
        //得到方法的参数的类型
        Class[] argClass = ((MethodSignature)joinPoint.getSignature()).getParameterTypes();

        DataSourceEnum dataSource = null;
        try {
            // 得到访问的方法对象
            Method method = className.getMethod(methodName, argClass);
            // 判断是否存在@DS注解
            if (method.isAnnotationPresent(DBSource.class)) {
                DBSource annotation = method.getAnnotation(DBSource.class);
                // 取出注解中的数据源名
                dataSource = annotation.value();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 切换数据源
        DataSourceContextHolder.setDataSource(dataSource.getValue());
    }

    @After("pointcutConfig()")
    public void after(JoinPoint joinPoint){
        DataSourceContextHolder.clearDataSource();
    }
}
