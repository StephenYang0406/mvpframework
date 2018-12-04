package com.stephen.demo.sample.presenter

import com.stephen.demo.model.LoginVo
import com.stephen.demo.model.SampleResponse
import com.stephen.demo.network.SampleObserver
import com.stephen.demo.network.SampleRxRetrofitClient
import com.stephen.demo.sample.activity.SampleActivity
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

    //网络请求
    fun doNetwork() {
        SampleRxRetrofitClient()
                .request<SampleResponse<LoginVo>>("checkToken")
                ?.subscribe(object : SampleObserver<LoginVo>() {
                    override fun success(data: LoginVo) {

                    }
                })
    }
}