package com.stephen.mvpframework.local;

import android.content.Context;

/**
 * Created by Stephen on 2017/10/5.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public class Content {

    //通用参数
    public static final class Params {
        //测试用日志标签
        public static final String TEST_LOG_TAG = "TEST_LOG";
        //日志最大显示数量
        public static final int SIZE = 2000;
        //拍照请求码
        public static final int TAKE_PHOTO_REQUEST_CODE = 666;
    }


    public static final class ConnectionTime {
        //通用超时时间
        public static final long COMMON_TIME_OUT = 10 * 1000;

    }

    //本地文件操作参数
    public static final class SharedPreferencesParam {
        //文件名称
        public static final String FILE_NAME = "CONFIG_FILE";
        //文件存储模式
        public static final int MODE = Context.MODE_PRIVATE;
    }


}
