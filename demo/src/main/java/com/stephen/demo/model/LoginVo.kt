package com.stephen.demo.model

import com.stephen.mvpframework.model.BaseVo

/**
 * Created by Stephen on 2018/12/4.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
class LoginVo :BaseVo() {
    //用户信息VO
    var customer: CustomerVo? = null
    //唯一token
    var token: String? = null
}