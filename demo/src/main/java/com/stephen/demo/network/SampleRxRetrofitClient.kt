package com.stephen.demo.network

import com.stephen.demo.model.SampleRequest
import com.stephen.mvpframework.network.AbstractRxRetrofitClient
import com.stephen.mvpframework.network.RxRetrofitBuilder
import retrofit2.Retrofit
import kotlin.reflect.KClass

/**
 * 示例项目请求客户端
 * Created by Stephen on 2018/12/4.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
class SampleRxRetrofitClient : AbstractRxRetrofitClient<SampleRequest>() {
    /**
     * 返回统一请求实体,根据不同项目填充不同请求
     */
//    override fun getRequest(): SampleRequest {
//        val sampleRequest = SampleRequest()
//        sampleRequest.token = ""
//        return sampleRequest
//    }

    /**
     * 返回本项目API接口类
     */
    override fun getApiService(): KClass<*> = SampleApiService::class

    /**
     * 返回自定义Retrofit实体
     * 单例写法
     */
    companion object {
        private var retrofit: Retrofit? = null
    }

    override fun getRetrofit(): Retrofit {
        if (retrofit == null) {
            retrofit = RxRetrofitBuilder()
                    //.baseUrl("http://pspt.sqsm.com")
                    .baseUrl("http://114.115.140.129:8080")
                    .build()
        }
        return retrofit!!
    }
}