package com.stephen.demo.sample.presenter

import com.stephen.demo.model.LoginVo
import com.stephen.demo.model.SampleRequest
import com.stephen.demo.model.SampleResponse
import com.stephen.demo.network.SampleObserver
import com.stephen.demo.network.SampleRxRetrofitClient
import com.stephen.demo.sample.activity.SampleActivity
import com.stephen.mvpframework.constraint.AbstractPresenter
import com.stephen.mvpframework.model.BaseForm
import com.stephen.mvpframework.model.BaseVo

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

    //网络请求
    fun doNetwork() {
        SampleRxRetrofitClient()
                .request<SampleResponse<BaseVo>>("checkToken", SampleRequest())
                ?.subscribe(object : SampleObserver<BaseVo>() {
                    override fun success(data: BaseVo) {

                    }
                })
//        val map = mutableMapOf<String, String>()
//        map["loginName"] = "test"
//        map["pwd"] = "111"
//        SampleRxRetrofitClient()
//                .request<SampleResponse<String>>("login", map = map)
//                ?.subscribe(object : SampleObserver<String>() {
//                    override fun success(data: String) {
//
//                    }
//                })
//        val map = mutableMapOf<String, Int>()
//        map["page"] = 1
//        map["size"] = 10
//        SampleRxRetrofitClient()
//                .request<SampleResponse<BaseVo>>("getTotalTenderList", map = map)
//                ?.subscribe(object : SampleObserver<BaseVo>() {
//                    override fun success(data: BaseVo) {
//
//                    }
//                })
    }
}