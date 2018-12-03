package com.stephen.mvpframework.utils

import java.util.HashMap

/**
 * 临时储存工具类
 * Created by Stephen on 2018/11/16.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
object MemoryShareDataUtil {
    private val dataMap = HashMap<String, Any>()

    //清空内存
    fun clear() {
        dataMap.clear()
    }

    fun isHasData(code: String): Boolean {
        return dataMap.size > 0 && dataMap.containsKey(code)
    }

    fun getObj(code: String): Any? {
        return dataMap[code]
    }

    fun setObj(obj: Any) {
        dataMap[""] = obj
    }

    fun setObj(code: String, obj: Any) {
        dataMap[code] = obj
    }
}