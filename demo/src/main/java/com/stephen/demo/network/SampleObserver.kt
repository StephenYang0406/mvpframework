package com.stephen.demo.network

import com.stephen.demo.model.SampleResponse
import com.stephen.mvpframework.network.AbstractObserver
import com.stephen.mvpframework.utils.LogUtil
import io.reactivex.disposables.Disposable

/**
 * 示例项目统一Observer,一般根据项目抽取便于统一处理
 * Created by Stephen on 2018/12/4.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
abstract class SampleObserver<T> : AbstractObserver<SampleResponse<T>>() {
    //开始订阅,主线程
    override fun onSubscribe(d: Disposable) {
        super.onSubscribe(d)

    }

    //完成,主线程
    override fun onComplete() {
        LogUtil.testInfo("请求完成")
    }

    //返回结果,主线程
    override fun onNext(t: SampleResponse<T>) {
        if (t.data != null) {
            success(t.data!!)
        }
    }

    //错误,主线程
    override fun onError(e: Throwable) {
        LogUtil.testError("请求失败${e.message}")
    }

    abstract fun success(data: T)

}