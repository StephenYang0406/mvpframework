package com.stephen.mvpframework.utils;

import android.view.View;

import com.stephen.mvpframework.annotation.InjectView;
import com.stephen.mvpframework.annotation.InjectViewHolder;
import com.stephen.mvpframework.context.ContextHandler;
import com.stephen.mvpframework.local.Content;

import java.lang.reflect.Field;

/**
 * UI工具类
 * Created by Stephen on 2017/10/17.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public class UiUtil {
    //捆绑控件到ViewHolder
    public static void bindViewHolder(View rootView, Object obj) {
        if (obj == null) return;
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.isAnnotationPresent(InjectView.class)) {
                    View targetView = rootView.findViewById(field.getAnnotation(InjectView.class).id());
                    if (targetView != null) {
                        field.set(obj, field.getType().cast(targetView));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 填充Activity的form
     */
    public static void fillActivityForm(Object obj) {
        if (ContextHandler.currentActivity().getIntent() != null && ContextHandler.currentActivity().getIntent().getExtras() != null) {
            Object formObj = ContextHandler.currentActivity().getIntent().
                    getSerializableExtra(Content.Params.FORM_KEY);
            if (formObj != null) {
                //将form进行拷贝
                BeanUtil.beanCopy(formObj, BeanUtil.getForm(obj));
            }
        }
    }

    /**
     * 根据传递的form进行本界面form填充
     */
    public static void fillFragmentForm(Object obj) {
        //将form进行拷贝
        BeanUtil.beanCopy(BeanUtil.getForm(ContextHandler.currentActivity()), BeanUtil.getForm(obj));
    }

    /**
     * 获得当前对象中的viewHolder
     */
    public static Object getViewHolder(Object obj) {
        if (obj == null) return null;
        Object viewHolderObj = null;
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            //实例化viewHolder
            if (field.isAnnotationPresent(InjectViewHolder.class)) {
                try {
                    viewHolderObj = field.get(obj);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return viewHolderObj;
    }

}
