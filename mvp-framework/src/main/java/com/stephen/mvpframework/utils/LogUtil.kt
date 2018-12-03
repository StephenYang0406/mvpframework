package com.stephen.mvpframework.utils

import android.util.Log
import com.stephen.mvpframework.local.Content
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.withContext

/**
 * 日志工具类
 * Created by Stephen on 2018/11/16.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
object LogUtil {
    //测试用info
    fun testInfo(message: String) {
        if (message.length > Content.Params.SIZE) {
            showLongMessage(0, message)
        } else {
            Log.i(Content.Params.TEST_LOG_TAG + "[${Thread.currentThread().name}]", message)
        }
    }

    //测试打印错误日志
    fun testError(message: String) {
        Log.e(Content.Params.TEST_LOG_TAG + "[${Thread.currentThread().name}]", message)
    }

    //打印长信息
    private fun showLongMessage(offset: Int, message: String) {
        Log.i(Content.Params.TEST_LOG_TAG + "[${Thread.currentThread().name}]", message.substring(Content.Params.SIZE * offset, if (message.length > Content.Params.SIZE * (offset + 1)) Content.Params.SIZE * (offset + 1) else message.length))
        if (message.length > Content.Params.SIZE * (offset + 1)) {
            showLongMessage(offset + 1, message)
        }
    }
}