package com.stephen.mvpframework.model

import com.google.gson.Gson
import java.io.Serializable

/**
 * 基础form
 * Created by Stephen on 2018/11/27.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
open class BaseForm : Serializable {
    override fun toString() = Gson().toJson(this)!!
}