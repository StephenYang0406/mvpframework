package com.stephen.mvpframework.helper

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.stephen.mvpframework.handler.ContextHandler
import com.stephen.mvpframework.local.Content

/**
 * 缓存文件操作帮助类
 * Created by Stephen on 2018/11/19.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
object SharedPreferenceHelper {
    //获得文件
    private fun open(): SharedPreferences {
        return ContextHandler.getApplication().getSharedPreferences(Content.SharedPreferencesParam.FILE_NAME, Content.SharedPreferencesParam.MODE)
    }

    //提交操作
    private fun commit(editor: SharedPreferences.Editor) {
        //editor.apply();
        editor.commit()
    }

    //添加纪录
    @SuppressLint("CommitPrefEdits")
    fun push(key: String, value: String) {
        commit(open().edit().putString(key, value))
    }

    //删除记录
    @SuppressLint("CommitPrefEdits")
    fun remove(key: String) {
        commit(open().edit().remove(key))
    }

    //全部删除
    @SuppressLint("CommitPrefEdits")
    fun removeAll() {
        commit(open().edit().clear())
    }

    ///根据key值获得存储值
    fun getValue(key: String): String? {
        return open().getString(key, null)
    }

    //获得全部文件映射关系
    fun getAll(): Map<String, *> {
        return open().all
    }

    //将文件参数填充进对应实体
    fun fill(obj: Any): Any {
        val paramsMap = getAll()
        try {
            for (field in obj.javaClass.declaredFields) {
                if (paramsMap.containsKey(field.name)) {
                    field.isAccessible = true
                    field.set(obj, paramsMap[field.name])
                }
            }
        } catch (ignored: Exception) {
        }
        return obj
    }
}