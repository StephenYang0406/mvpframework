package com.stephen.mvpframework.helper;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;

import com.stephen.mvpframework.context.ContextHandler;


/**
 * 警告帮助类
 * Created by Stephen on 2018/2/5.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public class WarningHelper {
    public static Drawable getDrawable(int resId) {
        return ContextCompat.getDrawable(ContextHandler.currentActivity(), resId);
    }

    public static int getColor(int resId) {
        return ContextCompat.getColor(ContextHandler.currentActivity(), resId);
    }

    public static String getString(int resId){
        return ContextHandler.currentActivity().getResources().getString(resId);
    }

    public static Spanned fromHtml(String source) {
        return Html.fromHtml(source);
    }

}
