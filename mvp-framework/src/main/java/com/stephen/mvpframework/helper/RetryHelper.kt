package com.stephen.mvpframework.helper

import com.stephen.mvpframework.handler.ContextHandler
import com.stephen.mvpframework.model.BaseResponse
import com.stephen.mvpframework.network.AbstractObserver
import com.stephen.mvpframework.utils.LogUtil
import io.reactivex.Observable
import java.util.*

/**
 * 网络请求重试帮助类
 * Created by Stephen on 2018/11/19.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
object RetryHelper {
    private val mObservableMap = HashMap<String, Observable<BaseResponse<Any>>>()
    private val mObserverMap = HashMap<String, AbstractObserver<BaseResponse<Any>>>()

    //存放观察者
    @Synchronized
    fun putObserver(observer: AbstractObserver<*>) {
        LogUtil.testInfo("putObserver--->${ContextHandler.current()::class.java.name}")
        @Suppress("UNCHECKED_CAST")
        mObserverMap[ContextHandler.current()::class.java.name] = observer as AbstractObserver<BaseResponse<Any>>
    }

    //存放被观察者
    @Synchronized
    fun putObservable(observable: Observable<*>) {
        LogUtil.testInfo("putObservable--->${ContextHandler.current().javaClass.name}")
        @Suppress("UNCHECKED_CAST")
        mObservableMap[ContextHandler.current().javaClass.name] = observable as Observable<BaseResponse<Any>>
    }

    //通过Observable获取key
    @Synchronized
    fun getObservableKey(observable: Observable<*>): String? {
        for ((key, value) in mObservableMap) {
            if (observable === value) {
                return key
            }
        }
        return null
    }

    //通过key判断是否包含此Observable
    @Synchronized
    fun containsObservable(key: String): Boolean {
        return mObservableMap.containsKey(key)
    }

    //是否包含此Observer
    @Synchronized
    fun containsObserver(observer: AbstractObserver<*>): Boolean {
        return mObserverMap.containsValue(observer)
    }


    //重试
    @Synchronized
    fun retry(target: String) {
        if (mObservableMap.containsKey(target) && mObserverMap.containsKey(target)) {
            mObservableMap[target]?.subscribe(mObserverMap[target]!!)
            LogUtil.testInfo("retry--->$target")
        }
    }

}