package com.stephen.mvpframework.utils

import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * 注解工具类
 * Created by Stephen on 2018/11/15.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
object AnnotationUtil {
    /**
     * 判断对象是否包含特殊注解类
     */
    fun isHaveAnnotation(target: Class<*>, annotationClass: Class<out Annotation>): Boolean {
        return target.isAnnotationPresent(annotationClass)
    }

    /**
     * 判断方法是否包含特殊注解类
     */
    fun isHaveAnnotation(method: Method, annotationClass: Class<out Annotation>): Boolean {
        return method.isAnnotationPresent(annotationClass)
    }

    /**
     * 判断字段是否包含特殊注解类
     */
    fun isHaveAnnotation(field: Field, annotationClass: Class<out Annotation>): Boolean {
        return field.isAnnotationPresent(annotationClass)
    }

    /**
     * 获得对象的注解
     */
    fun <T : Annotation> getAnnotation(target: Class<*>, annotationClass: Class<T>): T {
        return target.getAnnotation(annotationClass)!!
    }

    /**
     * 获得方法的注解
     */
    fun <T : Annotation> getAnnotation(method: Method, annotationClass: Class<T>): T {
        return method.getAnnotation(annotationClass)
    }

    /**
     * 获得字段的注解
     */
    fun <T : Annotation> getAnnotation(field: Field, annotationClass: Class<T>): T {
        return field.getAnnotation(annotationClass)
    }
}