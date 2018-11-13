package com.stephen.mvpframework.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 临时储存
 * Created by Stephen on 2018/1/26.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public class MemoryShareDataUtil {
    private final static Map<String, Object> dataMap = new HashMap<>();

    //清空内存
    public static void clear() {
        dataMap.clear();
    }

    public static boolean isHasData(String code) {
        return dataMap.size() > 0 && dataMap.containsKey(code);
    }

    public static Object getObj(String code) {
        return dataMap.get(code);
    }

    public static void setObj(Object obj) {
        dataMap.put("", obj);
    }

    public static void setObj(String code, Object obj) {
        dataMap.put(code, obj);
    }
}
