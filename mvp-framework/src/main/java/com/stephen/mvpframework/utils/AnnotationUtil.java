package com.stephen.mvpframework.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 注解工具类
 * Created by Stephen on 2017/10/4.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public class AnnotationUtil {
    /**
     * 判断对象是否包含特殊注解类
     */
    public static boolean isHaveAnnotation(Class target, Class<? extends Annotation> annotationClass) {
        return target.isAnnotationPresent(annotationClass);
    }

    /**
     * 判断方法是否包含特殊注解类
     */
    public static boolean isHaveAnnotation(Method method, Class<? extends Annotation> annotationClass) {
        return method.isAnnotationPresent(annotationClass);
    }

    /**
     * 判断字段是否包含特殊注解类
     */
    public static boolean isHaveAnnotation(Field field, Class<? extends Annotation> annotationClass) {
        return field.isAnnotationPresent(annotationClass);
    }

    /**
     * 获得对象的注解
     */
    public static <T extends Annotation> T getAnnotation(Class target, Class<T> annotationClass) {
        return (T) target.getAnnotation(annotationClass);
    }

    /**
     * 获得方法的注解
     */
    public static <T extends Annotation> T getAnnotation(Method method, Class<T> annotationClass) {
        return method.getAnnotation(annotationClass);
    }

    /**
     * 获得字段的注解
     */
    public static <T extends Annotation> T getAnnotation(Field field, Class<T> annotationClass) {
        return  field.getAnnotation(annotationClass);
    }


}
