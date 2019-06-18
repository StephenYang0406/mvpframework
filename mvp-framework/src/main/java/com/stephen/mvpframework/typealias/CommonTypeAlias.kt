package cn.xjdeyou.roadconstruction.common.`typealias`

import com.yanzhenjie.permission.RequestExecutor

/**
 * Created by Stephen on 2019/3/25.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
//通用点击事件
typealias OnCommonClick = () -> Unit

//权限请求失败代码块
typealias PermissionFailed = (permissionList: List<String>, failedPermissionStr: String) -> Unit

//权限重试代码块
typealias PermissionRetry = (permissionList: List<String>, failedPermissionStr: String, executor: RequestExecutor) -> Unit