package com.stephen.mvpframework.network

import com.stephen.mvpframework.annotation.RetryAnno
import com.stephen.mvpframework.helper.RetryHelper
import com.stephen.mvpframework.model.BaseForm
import com.stephen.mvpframework.model.BaseRequest
import com.stephen.mvpframework.model.BaseResponse
import com.stephen.mvpframework.utils.AnnotationUtil
import com.stephen.mvpframework.utils.LogUtil
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Retrofit
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass

/**
 * 抽象RX请求客户端
 * Created by Stephen on 2018/11/28.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
abstract class AbstractRxHttpClient {
    /**
     * 获取请求发送实体
     */
    abstract fun getRequest(): BaseRequest<*>


    abstract fun getApiService(): KClass<Any>

    abstract fun getRetrofit(): Retrofit

    @Suppress("UNCHECKED_CAST")
    private fun fillParams(params: Any?): RequestBody {
        val baseRequest = getRequest()
        when (params) {
            is String -> {
                baseRequest as BaseRequest<String>
                baseRequest.data = params
            }
            is Map<*, *> -> {
                baseRequest as BaseRequest<Map<*, *>>
                baseRequest.data = params
            }
            null -> {
            }
            else -> {
                baseRequest as BaseRequest<BaseForm>
                baseRequest.data = params as BaseForm
            }
        }
        fillExtras(baseRequest)
        LogUtil.testInfo("criteria---->$baseRequest")
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), baseRequest.toString())
    }

    /**
     * 重写填充和data同级额外字段
     */
    protected fun fillExtras(request: BaseRequest<*>) {

    }

    /**
     * 请求通用方法
     * methodName 调用的接口方法名
     * params 参数,可以是继承BaseForm的实体,可以是字符串,可以为null,可以为map
     * T 泛型,返回的Observable的泛型,必须是BaseResponse的实体
     * 调用示例:
     *         request<BaseResponse<Int>>("")
    .subscribe(object :AbstractObserver<BaseResponse<Int>>(){
    override fun onComplete() {
    }

    override fun onNext(t: BaseResponse<Int>) {
    }

    override fun onError(e: Throwable) {
    }
    })
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : BaseResponse<*>> request(methodName: String, params: Any? = null): Observable<T>? {
        try {
            val apiService = getRetrofit().create(getApiService().java)
            val executeMethod = apiService.javaClass.getMethod(methodName, RequestBody::class.java)
            val observable = executeMethod.invoke(apiService, fillParams(params)) as Observable<T>
            if (AnnotationUtil.isHaveAnnotation(executeMethod, RetryAnno::class.java)) {
                RetryHelper.putObservable(observable)
            }
            return observable
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
            LogUtil.testError("没有找到该方法--->$methodName")
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            LogUtil.testError("调用方法失败")
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
            LogUtil.testError("调用方法失败")
        }
        return null
    }


}