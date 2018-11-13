package com.stephen.mvpframework.utils;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import com.stephen.mvpframework.context.ContextHandler;
import com.stephen.mvpframework.local.Content;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 缓存文件操作工具类
 * Created by Stephen on 2017/11/7.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public class SharedPreferenceUtil {
    //获得文件
    private static SharedPreferences open() {
        return ContextHandler.getApplication().getSharedPreferences(Content.SharedPreferencesParam.FILE_NAME, Content.SharedPreferencesParam.MODE);
    }
    //提交操作
    private static void commit(SharedPreferences.Editor editor) {
        //editor.apply();
        editor.commit();
    }

    //添加纪录
    @SuppressLint("CommitPrefEdits")
    public static void push(String key, String value) {
        commit(open().edit().putString(key, value));
    }

    //删除记录
    @SuppressLint("CommitPrefEdits")
    public static void remove(String key) {
        commit(open().edit().remove(key));
    }

    //全部删除
    @SuppressLint("CommitPrefEdits")
    public static void removeAll() {
        commit(open().edit().clear());
    }

    ///根据key值获得存储值
    public static String getValue(String key) {
        return open().getString(key, null);
    }

    //获得全部文件映射关系
    public static Map<String, ?> getAll() {
        return open().getAll();
    }

    //将文件参数填充进对应实体
    public static Object fill(Object obj) {
        Map<String, ?> paramsMap = getAll();
        try {
            for (Field field : obj.getClass().getDeclaredFields()) {
                if (paramsMap.containsKey(field.getName())) {
                    field.setAccessible(true);
                    field.set(obj, paramsMap.get(field.getName()));
                }
            }
        } catch (Exception ignored) {
        }
        return obj;
    }
}
