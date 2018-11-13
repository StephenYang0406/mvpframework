package com.stephen.mvpframework.utils;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.stephen.mvpframework.context.ContextHandler;

/**
 * 屏幕相关工具类
 * Created by Stephen on 2017/11/1.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public class ScreenUtil {

    /**
     * 获取手机大小（分辨率）
     */
    public static DisplayMetrics getScreenPix(Activity activity) {
        DisplayMetrics displaysMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);
        return displaysMetrics;
    }
    /*
        //方法一：已过时，可使用，但不建议使用
    public static void getResolution1(Context mContext) {
        Display mDisplay = ((Activity) mContext).getWindowManager()
                .getDefaultDisplay();
        int W = mDisplay.getWidth();
        int H = mDisplay.getHeight();
    }
    //方法二：通过getWindowManager来获取屏幕尺寸的
    public static void getResolution2(Context mContext) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay()
                .getMetrics(mDisplayMetrics);
        int W = mDisplayMetrics.widthPixels;
        int H = mDisplayMetrics.heightPixels;
        // 屏幕密度（0.75 / 1.0 / 1.5）
        float density = mDisplayMetrics.density;
        // 就是屏幕密度 * 160而已,屏幕密度DPI（120 / 160 / 240）
        int densityDpi = mDisplayMetrics.densityDpi;
    }
    //方法三：通过getResources来获取屏幕尺寸的，大部分用这个
    public static void getResolution3(Context mContext) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        mDisplayMetrics = mContext.getResources().getDisplayMetrics();
        int W = mDisplayMetrics.widthPixels;
        int H = mDisplayMetrics.heightPixels;
        float density = mDisplayMetrics.density;
        int densityDpi = mDisplayMetrics.densityDpi;
    }
     */

    /**
     * 根据手机的分辨率从dp的单位转成为px
     */
    public static int dp2px(int dpValue) {
        DisplayMetrics displayMetrics = ContextHandler.currentActivity().getResources().getDisplayMetrics();
        return (int) ((dpValue * displayMetrics.density) + 0.5);
    }


    /**
     * 根据手机的分辨率从px转成为dp
     */
    public static int px2dip(float pxValue) {
        final float scale = ContextHandler.currentActivity().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取不同系统状态栏条高度
     */
//    @SuppressLint("PrivateApi")
//    public static int getStatusBarHeight() {
//        int statusBarHeight = 0;
//        try {
//            Class<?> c = Class.forName("com.android.internal.R$dimen");
//            Object obj = c.newInstance();
//            Field field = c.getField("status_bar_height");
//            int x = Integer.parseInt(field.get(obj).toString());
//            statusBarHeight = ContextHandler.currentActivity().getResources().getDimensionPixelSize(x);
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//        return statusBarHeight;
//    }
    /*
       获取状态栏高度
     */
    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = ContextHandler.currentActivity().getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = ContextHandler.currentActivity().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    //获取系统默认Actionbar高度
    public static int getActionBarHeight() {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (ContextHandler.currentActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, ContextHandler.currentActivity().getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }
}
