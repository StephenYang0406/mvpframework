package com.stephen.mvpframework.network;


import com.stephen.mvpframework.annotation.IgnoreAnno;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * RX+Retro形式网络请求基类
 * Created by Stephen on 2017/10/31.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public abstract class AbstractRxHttpPost {

    //填充数据集合
    protected void putCriteria(Object o) {
        getCriteria().clear();
        for (Field formField : o.getClass().getDeclaredFields()) {
            formField.setAccessible(true);
            if (formField.isAnnotationPresent(IgnoreAnno.class))
                continue;
            try {
                if (formField.get(o) == null)
                    continue;
                getCriteria().put(formField.getName(), formField.get(o));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    protected abstract Map<String, Object> getCriteria();

}
