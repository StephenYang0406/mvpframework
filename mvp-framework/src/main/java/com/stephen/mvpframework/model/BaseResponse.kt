package com.stephen.mvpframework.model

/**
 * 基础网络请求返回
 * Created by Stephen on 2018/11/27.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
open class BaseResponse<T> : BaseVo() {
    var data: T? = null
}