package com.stephen.mvpframework.utils;

import android.util.Log;

import com.stephen.mvpframework.local.Content;


/**
 * 日志工具
 * Created by Stephen on 2017/10/5.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public class LogUtil {


    //测试用info
    public static void testInfo(String message) {
        if (message.length() > Content.Params.SIZE) {
            showLongMessage(0, message);
        } else {
            Log.i(Content.Params.TEST_LOG_TAG, message);
        }
    }

    //测试打印错误日志
    public static void testError(String message) {
        Log.e(Content.Params.TEST_LOG_TAG, message);
    }

    //打印长信息
    private static void showLongMessage(int offset, String message) {
        Log.i(Content.Params.TEST_LOG_TAG, message.substring(Content.Params.SIZE * offset, message.length() > Content.Params.SIZE * (offset + 1) ? Content.Params.SIZE * (offset + 1) : message.length()));
        if (message.length() > Content.Params.SIZE * (offset + 1)) {
            showLongMessage(offset + 1, message);
        }
    }


}
