package com.stephen.mvpframework.utils;


import com.stephen.mvpframework.annotation.Autowire;
import com.stephen.mvpframework.annotation.IgnoreAnno;
import com.stephen.mvpframework.annotation.InjectForm;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * 实体操作工具
 * Created by Stephen on 2017/11/2.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public class BeanUtil {

    /**
     * form拷贝方法
     */
    public static void beanCopy(Object objOrig, Object objNew) {
        if (objOrig == null || objNew == null) {
            return;
        }
        for (Field fieldOrig : objOrig.getClass().getDeclaredFields()) {
            fieldOrig.setAccessible(true);
            try {
                Field fieldNew = objNew.getClass().getDeclaredField(fieldOrig.getName());
                //不含IgnoreAnno注解的可以进行拷贝
                if (!fieldNew.isAnnotationPresent(IgnoreAnno.class)) {
                    fieldNew.setAccessible(true);
                    fieldNew.set(objNew, fieldOrig.get(objOrig));
                }
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
    }

    /**
     * form拷贝Vo方法
     */
    public static void beanVoCopy(Object vo, Object objNew) {
        if (vo == null || objNew == null) {
            return;
        }
        for (Field fieldOrig : vo.getClass().getFields()) {
            try {
                Field fieldNew = objNew.getClass().getDeclaredField(fieldOrig.getName());
                //新实体中不含IgnoreAnno注解的可以进行拷贝
                if (!fieldNew.isAnnotationPresent(IgnoreAnno.class)) {
                    fieldNew.setAccessible(true);
                    fieldNew.set(objNew, fieldOrig.get(vo));
                }
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * form赋值填充vo方法
     */
    public static void beanVoCopyFill(Object vo, Object objNew) {
        if (vo == null || objNew == null) {
            return;
        }
        for (Field fieldOrig : vo.getClass().getFields()) {
            try {
                Field fieldNew = objNew.getClass().getDeclaredField(fieldOrig.getName());
                //新实体中不含IgnoreAnno注解的可以进行拷贝
                if (!fieldNew.isAnnotationPresent(IgnoreAnno.class)) {
                    fieldNew.setAccessible(true);
                    fieldNew.set(objNew, fieldOrig.get(vo));
                }
            } catch (Exception ignored) {
            }
        }
    }


    /**
     * 将Map填充进实体
     */
    public static void beanMapCopy(Map map, Object objNew) {
        if (map == null || objNew == null) {
            return;
        }
        for (Field field : objNew.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                field.set(objNew, map.get(field.getName()));
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * 获得当前对象中的form，需要序列化
     */
    public static Serializable getForm(Object obj) {
        if (obj == null) return null;
        Serializable formObj = null;
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            //实例化form
            if (field.isAnnotationPresent(InjectForm.class)) {
                try {
                    formObj = (Serializable) field.get(obj);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return formObj;
    }


    /**
     * 自动注入方法
     */
    public static void autowire(Object obj) {
        /* 字段注入 */
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                //自动载入
                if (field.isAnnotationPresent(Autowire.class)) {
                    field.set(obj, field.getType().newInstance());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
