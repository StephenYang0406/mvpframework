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
        //跳转传递params的key值
        public static final String PARAMS_KEY = "PARAMS_KEY";
        //跳转传递form的key值
        public static final String FORM_KEY = "FORM_KEY";
        //跳转传递请求码的key值
        public static final String REQUEST_KEY = "REQUEST_KEY";
        //和js交互绑定对象通用名
        public static final String JS_CALL_NAME = "stephen";
        //拍照请求码
        public static final int TAKE_PHOTO_REQUEST_CODE = 666;
        //拍照请求码
        public static final int PICK_PHOTO_REQUEST_CODE = 667;

        public static final int CARD_PHOTO_REQUEST_CODE = 668;
        //项目文件根目录名称
        public static final String FOLDER_NAME = "stephen";
        //文件目录
        public static final String FILE_FOLDER_NAME = "file";
        //图片目录
        public static final String PICTURE_FOLDER_NAME = "picture";
        //数据库名称
        public static final String DATABASE_NAME = "database";
        //数据库版本
        public static final int DATABASE_SCHEMA_VERSION = 1;
        //UI图设计宽度
        public static final int DESIGN_WIDTH = 750;
    }


    public static final class ConnectionTime {
        //通用超时时间
        public static final int COMMON_TIME_OUT = 10 * 1000;

    }

    //本地文件操作参数
    public static final class SharedPreferencesParam {
        //文件名称
        public static final String FILE_NAME = "CONFIG_FILE";
        //文件存储模式
        public static final int MODE = Context.MODE_PRIVATE;
    }

    //权限相关参数
    public static final class PermissionParams {
        //不再提示并拒绝权限,跳转Setting请求码
        public static final int ALWAYS_DENIED_REQUEST_CODE = 100;
        //拨打电话权限
        public static final int CALL_PHONE_REQUEST_CODE = 11;
        //拍照权限
        public static final int CAMERA_REQUEST_CODE = 22;
        //定位权限
        public static final int LOCATION_REQUEST_CODE = 33;
    }

}
