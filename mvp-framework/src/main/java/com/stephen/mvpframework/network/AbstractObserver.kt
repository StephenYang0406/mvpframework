package com.stephen.mvpframework.network

import com.stephen.mvpframework.handler.ContextHandler
import com.stephen.mvpframework.helper.RetryHelper
import com.stephen.mvpframework.model.BaseResponse
import com.stephen.mvpframework.utils.LogUtil
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.HashMap

/**
 * 抽象Observer,用于框架统一管理请求订阅的生命周期
 * 泛型T为 BaseResponse<M>的子类,泛型M为具体返回data
 * Created by Stephen on 2018/11/28.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
abstract class AbstractObserver<T : BaseResponse<*>> : Observer<T> {
    companion object {
        //订阅容器
        private val compositeDisposableMap = HashMap<String, CompositeDisposable>()

        //取消一个界面的所有订阅
        fun unSubscribe(tag: String) {
            if (compositeDisposableMap.containsKey(tag)) {
                LogUtil.testInfo("unSubscribe--->$tag")
                compositeDisposableMap[tag]?.clear()
            }
        }
    }

    init {
        if (RetryHelper.containsObservable(ContextHandler.current()::class.java.name)) {
            @Suppress("LeakingThis")
            RetryHelper.putObserver(this)
        }
    }


    override fun onSubscribe(d: Disposable) {
        LogUtil.testInfo("onSubscribe--->" + ContextHandler.current().javaClass.name)
        if (compositeDisposableMap.containsKey(ContextHandler.current().javaClass.name)) {
            val compositeDisposable = compositeDisposableMap[ContextHandler.current().javaClass.name]
            compositeDisposable?.add(d)
        } else {
            val compositeDisposable = CompositeDisposable()
            compositeDisposable.add(d)
            compositeDisposableMap[ContextHandler.current().javaClass.name] = compositeDisposable
        }
    }
}