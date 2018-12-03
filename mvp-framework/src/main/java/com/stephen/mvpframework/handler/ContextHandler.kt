package com.stephen.mvpframework.handler

import android.app.Application
import android.view.View
import com.stephen.mvpframework.constraint.IUiOperation
import com.stephen.mvpframework.ui.activity.AbstractActivity
import com.stephen.mvpframework.ui.fragment.AbstractFragment
import com.stephen.mvpframework.utils.LogUtil
import java.lang.ref.SoftReference
import java.util.*

/**
 * 上下文控制类
 * Created by Stephen on 2018/11/20.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
object ContextHandler {
    //activity堆栈
    private val activityStack = Stack<AbstractActivity>()


    private var fragment: SoftReference<AbstractFragment>? = null

    //APPLICATION
    private var application: SoftReference<Application>? = null

    /**
     * 保存Application
     */
    fun saveApplication(application: Application) {
        this.application = SoftReference(application)
    }

    /**
     * 获得Application
     */
    fun getApplication(): Application {
        return application!!.get()!!
    }

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: AbstractActivity) {
        fragment = null
        LogUtil.testError(activity.javaClass.name + "加入栈")
        activityStack.push(activity)
    }

    /**
     * 设置Fragment
     */
    fun setFragment(fragment: AbstractFragment) {
        this.fragment = SoftReference(fragment)
    }

    /**
     * 获取当前
     */
    fun current(): IUiOperation {
        return if (fragment != null && fragment!!.get() != null) fragment!!.get()!! else currentActivity()
    }

    /**
     * 获得当前的fragment
     */
    fun currentFragment(): AbstractFragment {
        return fragment!!.get()!!
    }

    /**
     * 获得当前运行的Activity
     */
    fun currentActivity(): AbstractActivity {
        return activityStack.peek()
    }

    /**
     * 移除最后的Activity
     */
    fun removeLast() {
        fragment = null
        LogUtil.testError(activityStack.peek().javaClass.name + "最顶出栈")
        activityStack.pop()
    }

    /**
     * 当前栈是否包含此Activity
     */
    fun containsActivity(activity: AbstractActivity): Boolean {
        return activityStack.contains(activity)
    }

    /**
     * 移除指定Activity
     */
    fun removeTargetActivity(activity: AbstractActivity) {
        LogUtil.testError(activity.javaClass.name + "指定出栈")
        activityStack.remove(activity)
    }

    /**
     * 结束除了指定的所有Activity
     */
    fun finishAllActivity(activity: AbstractActivity) {
        for (activityTemp in activityStack) {
            if (activityTemp.javaClass.name != activity.javaClass.name) {
                activityTemp.finish()
            }
        }
        activityStack.removeAllElements()
        activityStack.add(activity)
    }

    /**
     * 获取当前根视图
     */
    fun getRootView(): View {
        return if (fragment != null && fragment!!.get() != null) fragment!!.get()!!.view!! else activityStack.lastElement().window.decorView
    }

    //获取当前Activity根视图
    fun getActivityRootView(): View {
        return activityStack.lastElement().window.decorView
    }


}