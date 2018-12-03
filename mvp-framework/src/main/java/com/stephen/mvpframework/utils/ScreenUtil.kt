package com.stephen.mvpframework.utils

import android.app.Activity
import android.util.DisplayMetrics
import android.util.TypedValue
import com.stephen.mvpframework.handler.ContextHandler

/**
 * 屏幕工具类
 * Created by Stephen on 2018/11/19.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
object ScreenUtil {
    /**
     * 获取手机大小（分辨率）
     */
    fun getScreenPix(activity: Activity): DisplayMetrics {
        val displaysMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displaysMetrics)
        return displaysMetrics
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
    fun dp2px(dpValue: Int): Int {
        val displayMetrics = ContextHandler.currentActivity().resources.displayMetrics
        return (dpValue * displayMetrics.density + 0.5).toInt()
    }


    /**
     * 根据手机的分辨率从px转成为dp
     */
    fun px2dip(pxValue: Float): Int {
        val scale = ContextHandler.currentActivity().resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /*
       获取状态栏高度
     */
    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = ContextHandler.currentActivity().resources
                .getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = ContextHandler.currentActivity().resources.getDimensionPixelSize(resourceId)
        }
        return result
    }


    //获取系统默认Actionbar高度
    fun getActionBarHeight(): Int {
        val tv = TypedValue()
        var actionBarHeight = 0
        if (ContextHandler.currentActivity().theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, ContextHandler.currentActivity().resources.displayMetrics)
        }
        return actionBarHeight
    }
}