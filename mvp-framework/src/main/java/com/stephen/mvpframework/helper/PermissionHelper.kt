package com.stephen.mvpframework.helper

import cn.xjdeyou.roadconstruction.common.`typealias`.PermissionFailed
import cn.xjdeyou.roadconstruction.common.`typealias`.PermissionRetry
import com.stephen.mvpframework.handler.ContextHandler
import com.stephen.mvpframework.ui.activity.AbstractActivity
import com.stephen.mvpframework.ui.fragment.AbstractFragment
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import com.yanzhenjie.permission.Request

/**
 * 权限帮助类
 * Created by Stephen on 2018/11/16.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
object PermissionHelper {

    /**
     * 权限构造参数类
     */
    class Builder {
        var permissionArray: Array<String> = arrayOf()
        var permissionGroupArray: Array<Array<String>> = arrayOf()
        var successBlock: () -> Unit = {}
        var failedBlock: PermissionFailed = { _, _ -> }
        var alwaysFailedBlock: PermissionFailed = { _, _ -> }
        var tryAgainBlock: PermissionRetry = { _, _, _ -> }

        fun permission(permissionArray: Array<String>): Builder {
            this.permissionArray = permissionArray
            return this
        }

        fun permissionGroup(permissionGroupArray: Array<Array<String>>): Builder {
            this.permissionGroupArray = permissionGroupArray
            return this
        }

        fun success(successBlock: () -> Unit): Builder {
            this.successBlock = successBlock
            return this
        }

        fun failed(failedBlock: PermissionFailed): Builder {
            this.failedBlock = failedBlock
            return this
        }

        fun alwaysFailed(alwaysFailedBlock: PermissionFailed): Builder {
            this.alwaysFailedBlock = alwaysFailedBlock
            return this
        }

        fun tryAgain(tryAgainBlock: PermissionRetry): Builder {
            this.tryAgainBlock = tryAgainBlock
            return this
        }

    }

    /*
        请求权限(可多条权限)
        successBlock 请求成功代码块
        failedBlock  请求失败代码块
        alwaysFailedBlock 一直请求失败的权限代码块
        tryAgainBlock 再次请求代码块
     */
    fun requestPermission(builder: PermissionHelper.Builder) {
        val request: Request = if (ContextHandler.current() is AbstractActivity) {
            AndPermission.with(ContextHandler.current() as AbstractActivity)
        } else {
            AndPermission.with(ContextHandler.current() as AbstractFragment)
        }
        request.permission(builder.permissionArray)
                //通过的权限回调
                .onGranted { builder.successBlock() }
                //拒绝的权限回调
                .onDenied { permissionList ->
                    // 这些权限被用户总是拒绝。
                    if (AndPermission.hasAlwaysDeniedPermission(ContextHandler.currentActivity(), permissionList)) {
                        builder.alwaysFailedBlock(permissionList, translatePermissionText(permissionList))
                    } else {
                        builder.failedBlock(permissionList, translatePermissionText(permissionList))
                    }
                }
                //可再次申请
                .rationale { _, permissionList, executor ->
                    builder.tryAgainBlock(permissionList, translatePermissionText(permissionList), executor)
                }.start()
    }

    /*
        请求权限组(可多条权限组)
        successBlock 请求成功代码块
        failedBlock  请求失败代码块
        alwaysFailedBlock 一直请求失败的权限代码块
        tryAgainBlock 再次请求代码块
     */
    fun requestPermissionGroup(builder: PermissionHelper.Builder) {
        val request: Request = if (ContextHandler.current() is AbstractActivity) {
            AndPermission.with(ContextHandler.current() as AbstractActivity)
        } else {
            AndPermission.with(ContextHandler.current() as AbstractFragment)
        }
        request.permission(*builder.permissionGroupArray)
                .onGranted { builder.successBlock() }
                .onDenied { permissionList ->
                    // 这些权限被用户总是拒绝。
                    if (AndPermission.hasAlwaysDeniedPermission(ContextHandler.currentActivity(), permissionList)) {
                        builder.alwaysFailedBlock(permissionList, translatePermissionText(permissionList))
                    } else {
                        builder.failedBlock(permissionList, translatePermissionText(permissionList))
                    }

                }
                .rationale { _, permissionList, executor ->
                    builder.tryAgainBlock(permissionList, translatePermissionText(permissionList), executor)
                }.start()
    }

    /*
        翻译权限文字,返回格式:
        权限1,权限2,权限3...
     */
    private fun translatePermissionText(permissionList: List<String>): String {
        val sb = StringBuilder()
        for (permissionName in Permission.transformText(ContextHandler.currentActivity(), permissionList)) {
            sb.append(permissionName).append(",")
        }
        sb.deleteCharAt(sb.length - 1)
        return sb.toString()
    }

}