package com.stephen.demo.sample.presenter

import com.stephen.demo.model.SampleRequest
import com.stephen.demo.model.SampleResponse
import com.stephen.demo.network.SampleObserver
import com.stephen.demo.network.SampleRxRetrofitClient
import com.stephen.demo.sample.activity.SampleActivity
import com.stephen.demo.sample.vo.SapmleVo
import com.stephen.mvpframework.constraint.AbstractPresenter

/**
 * 示例Presenter
 * Created by Stephen on 2018/12/4.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
class SamplePresenter : AbstractPresenter<SampleActivity>() {
    fun doSomething() {
        view.updateView()
    }

    //POST请求示例
    fun doPost() {
        SampleRxRetrofitClient()
                .request<SampleResponse<SapmleVo>>("doPost", SampleRequest())
                ?.subscribe(object : SampleObserver<SapmleVo>() {
                    override fun success(data: SapmleVo) {
                        doSomething()
                    }
                })
    }

    //GET请求示例
    fun doGet() {
        val map = mutableMapOf<String, String>()
        map["loginName"] = "test"
        map["pwd"] = "111"
        SampleRxRetrofitClient()
                .request<SampleResponse<SapmleVo>>("login", map = map)
                ?.subscribe(object : SampleObserver<SapmleVo>() {
                    override fun success(data: SapmleVo) {

                    }
                })

    }

    //混合请求示例
    fun doMix() {
        val map = mutableMapOf<String, Int>()
        map["page"] = 1
        map["size"] = 10
        SampleRxRetrofitClient()
                .request<SampleResponse<SapmleVo>>("getTotalTenderList", SampleRequest(), map)
                ?.subscribe(object : SampleObserver<SapmleVo>() {
                    override fun success(data: SapmleVo) {

                    }
                })
    }
}