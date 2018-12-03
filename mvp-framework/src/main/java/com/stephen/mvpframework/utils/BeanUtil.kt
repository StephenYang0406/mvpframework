package com.stephen.mvpframework.utils

import com.stephen.mvpframework.annotation.Autowire
import com.stephen.mvpframework.annotation.IgnoreAnno
import com.stephen.mvpframework.annotation.InjectForm
import java.io.Serializable

/**
 * 实体操作工具
 * Created by Stephen on 2018/11/15.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
object BeanUtil {

    /**
     * form拷贝方法
     */
    fun beanCopy(objOrig: Any, objNew: Any) {
        for (fieldOrig in objOrig.javaClass.declaredFields) {
            fieldOrig.isAccessible = true
            try {
                val fieldNew = objNew.javaClass.getDeclaredField(fieldOrig.name)
                //不含IgnoreAnno注解的可以进行拷贝
                if (!fieldNew.isAnnotationPresent(IgnoreAnno::class.java)) {
                    fieldNew.isAccessible = true
                    fieldNew.set(objNew, fieldOrig.get(objOrig))
                }
            } catch (ignored: Exception) {
                ignored.printStackTrace()
            }
        }
    }

    /**
     * form拷贝Vo方法
     */
    fun beanVoCopy(vo: Any, objNew: Any) {
        for (fieldOrig in vo.javaClass.fields) {
            try {
                val fieldNew = objNew.javaClass.getDeclaredField(fieldOrig.name)
                //新实体中不含IgnoreAnno注解的可以进行拷贝
                if (!fieldNew.isAnnotationPresent(IgnoreAnno::class.java)) {
                    fieldNew.isAccessible = true
                    fieldNew.set(objNew, fieldOrig.get(vo))
                }
            } catch (ignored: Exception) {
            }

        }
    }

    /**
     * form赋值填充vo方法
     */
    fun beanVoCopyFill(vo: Any, objNew: Any) {
        for (fieldOrig in vo.javaClass.fields) {
            try {
                val fieldNew = objNew.javaClass.getDeclaredField(fieldOrig.name)
                //新实体中不含IgnoreAnno注解的可以进行拷贝
                if (!fieldNew.isAnnotationPresent(IgnoreAnno::class.java)) {
                    fieldNew.isAccessible = true
                    fieldNew.set(objNew, fieldOrig.get(vo))
                }
            } catch (ignored: Exception) {
            }

        }
    }


    /**
     * 将Map填充进实体
     */
    fun beanMapCopy(map: Map<*, *>, objNew: Any) {
        for (field in objNew.javaClass.declaredFields) {
            try {
                field.isAccessible = true
                field.set(objNew, map[field.name])
            } catch (ignored: Exception) {
            }

        }
    }

    /**
     * 获得当前对象中的form，需要序列化
     */
    fun getForm(obj: Any): Serializable? {
        var formObj: Serializable? = null
        for (field in obj.javaClass.declaredFields) {
            field.isAccessible = true
            //实例化form
            if (field.isAnnotationPresent(InjectForm::class.java)) {
                try {
                    formObj = field.get(obj) as Serializable
                    break
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        return formObj
    }


    /**
     * 自动注入方法
     */
    fun autowire(obj: Any) {
        /* 字段注入 */
        for (field in obj.javaClass.declaredFields) {
            field.isAccessible = true
            try {
                //自动载入
                if (field.isAnnotationPresent(Autowire::class.java)) {
                    field.set(obj, field.type.newInstance())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

}