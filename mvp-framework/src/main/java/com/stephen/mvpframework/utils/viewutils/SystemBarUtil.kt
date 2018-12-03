package com.stephen.mvpframework.utils.viewutils

import android.graphics.Color
import android.os.Build
import android.support.annotation.ColorInt
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.stephen.mvpframework.handler.ContextHandler
import com.stephen.mvpframework.utils.ScreenUtil

/**
 * 状态栏和导航栏工具类
 * Created by Stephen on 2018/11/5.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
object SystemBarUtil {
    /*
    设置状态栏颜色
     */
    fun setStatusBarColor(@ColorInt color: Int) {
        ContextHandler.currentActivity().run {
            window.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //清除透明状态栏FLAG,否则设置颜色不生效
                    it.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    //添加状态栏绘制FLAG,否则设置颜色不生效
                    it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    //设置状态栏颜色
                    it.statusBarColor = color
                    //设置保留状态栏和导航栏位置
                    setFitsSystemWindows(true)
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //添加状态栏和导航条透明FLAG
                    it.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    //设置内容布局顶到状态栏下
                    setFitsSystemWindows(false)
                    //添加StubView,高度为StatusBar高度,添加到跟布局第一位,设置颜色
                    val stubView = View(this)
                    val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ScreenUtil.getStatusBarHeight())
                    stubView.layoutParams = layoutParams
                    stubView.setBackgroundColor(color)
                    ((it.decorView as FrameLayout).getChildAt(0) as LinearLayout)
                            .addView(stubView, 0)
                }
            }
        }
    }

    /*
     设置状态栏透明
     */
    fun setStatusBarTranslucent() {
        ContextHandler.currentActivity().run {
            window.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //设置内容布局扩展到状态栏后
                    it.decorView.systemUiVisibility = it.decorView.systemUiVisibility or
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    //添加状态栏绘制FLAG,否则设置颜色不生效
                    it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    //设置状态栏颜色
                    it.statusBarColor = Color.TRANSPARENT
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //添加状态栏透明FLAG并将内容布局扩展到状态栏后
                    it.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                }
            }
        }
    }

    /*
    设置导航条透明
     */
    fun setNavigationBarTranslucent() {
        ContextHandler.currentActivity().run {
            window.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //设置内容布局扩展到导航栏后
                    it.decorView.systemUiVisibility = it.decorView.systemUiVisibility or
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    //添加状态栏绘制FLAG,否则设置颜色不生效
                    it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    //设置导航栏颜色
                    it.navigationBarColor = Color.TRANSPARENT
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //添加导航栏透明FLAG
                    it.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                }
            }
        }
    }

    /*
    设置全透明模式
     */
    fun setTranslucentMode() {
        ContextHandler.currentActivity().run {
            window.let {
                //设置布局在状态栏和导航栏后面
                it.decorView.systemUiVisibility = it.decorView.systemUiVisibility or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //添加状态栏绘制FLAG,否则设置颜色不生效
                    it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    //设置状态栏和导航栏颜色
                    it.statusBarColor = Color.TRANSPARENT
                    it.navigationBarColor = Color.TRANSPARENT
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //添加状态栏和导航栏透明FLAG
                    it.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION or
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                }
            }
        }
    }


    /*
      设置状态栏与导航栏是否适合系统窗口,true将保持状态栏和导航栏位置
     */
    fun setFitsSystemWindows(flag: Boolean) {
        ContextHandler.currentActivity().findViewById<View>(android.R.id.content).fitsSystemWindows = flag
    }

}