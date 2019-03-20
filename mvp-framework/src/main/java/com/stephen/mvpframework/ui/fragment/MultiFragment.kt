package com.stephen.mvpframework.ui.fragment

import com.stephen.mvpframework.handler.ContextHandler
import com.stephen.mvpframework.ui.activity.FragmentContainerActivity

/**
 * 多Fragment单Activity模式适用
 * Created by Stephen on 2019/3/1.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
abstract class MultiFragment : AbstractFragment() {

    private val containerActivity: FragmentContainerActivity = ContextHandler.currentActivity() as FragmentContainerActivity

    fun route(fragment: MultiFragment) {
        containerActivity.route(fragment)
    }

    fun back() {
        containerActivity.back()
    }
}