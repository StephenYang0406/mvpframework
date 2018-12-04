package com.stephen.demo.model

import com.stephen.mvpframework.model.BaseResponse

/**
 * 示例项目统一返回实体
 * Created by Stephen on 2018/12/4.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
class SampleResponse<T> : BaseResponse<T>() {
    var code: String? = null
    var msg: String? = null
}