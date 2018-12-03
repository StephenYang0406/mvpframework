package com.stephen.mvpframework.helper

import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.text.Html
import android.text.Spanned
import com.stephen.mvpframework.handler.ContextHandler

/**
 * 警告帮助类
 * Created by Stephen on 2018/11/19.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
@Suppress("DEPRECATION")
object WarningHelper {
    fun getDrawable(resId: Int): Drawable? {
        return ContextCompat.getDrawable(ContextHandler.currentActivity(), resId)
    }

    fun getColor(resId: Int): Int {
        return ContextCompat.getColor(ContextHandler.currentActivity(), resId)
    }

    fun getString(resId: Int): String {
        return ContextHandler.currentActivity().resources.getString(resId)
    }

    fun fromHtml(source: String): Spanned {
        return Html.fromHtml(source)
    }

}