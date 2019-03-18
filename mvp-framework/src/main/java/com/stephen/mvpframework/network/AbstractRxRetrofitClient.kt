package com.stephen.mvpframework.network

import android.text.TextUtils
import com.stephen.mvpframework.annotation.RetryAnno
import com.stephen.mvpframework.helper.RetryHelper
import com.stephen.mvpframework.model.BaseRequest
import com.stephen.mvpframework.model.BaseResponse
import com.stephen.mvpframework.utils.AnnotationUtil
import com.stephen.mvpframework.utils.LogUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass

/**
 * 抽象RX请求客户端
 * Created by Stephen on 2018/11/28.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
abstract class AbstractRxRetrofitClient<R : BaseRequest> {
    /**
     * 获取请求发送实体
     */
    protected abstract fun getRequest(): R

    /**
     * 获取项目接口API实体类型
     */
    protected abstract fun getApiService(): KClass<*>

    /**
     * 获取项目自定义Retrofit实体
     */
    protected abstract fun getRetrofit(): Retrofit

    /**
     * 填充参数
     */
    @Suppress("UNCHECKED_CAST")
    private fun fillParams(params: Any?): RequestBody {
        val baseRequest = getRequest()
        baseRequest.data = params
        LogUtil.testInfo("criteria---->$baseRequest")
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), baseRequest.toString())
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
    fun <T : BaseResponse<*>> request(methodName: String, params: Any? = null, map: Map<*, *>? = null): Observable<T>? {
        try {
            val apiService = getRetrofit().create(getApiService().java)
            //循环所有方法
            apiService.javaClass.declaredMethods.forEach {
                //匹配方法名
                if (TextUtils.equals(it.name, methodName)) {
                    //如果参数>1个,判断参数注解数量,适配1.8以下
                    val observable = if (it.parameterAnnotations.size > 1) {
                        it.invoke(apiService, fillParams(params), map) as Observable<T>
                    } else {
                        //如果是Body类型
                        if (it.parameterAnnotations[0][0] is Body) {
                            it.invoke(apiService, fillParams(params)) as Observable<T>
                        } else {//QueryMap类型
                            it.invoke(apiService, map) as Observable<T>
                        }
                    }
                    //是否包含重试注解
                    if (AnnotationUtil.isHaveAnnotation(it, RetryAnno::class.java)) {
                        RetryHelper.putObservable(observable)
                    }
                    return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                }
            }
            LogUtil.testError("没有找到该方法--->$methodName")
            return null
//            val executeMethod = apiService.javaClass.getMethod(methodName, RequestBody::class.java)
//            val observable = executeMethod.invoke(apiService, fillParams(params)) as Observable<T>
//            if (AnnotationUtil.isHaveAnnotation(executeMethod, RetryAnno::class.java)) {
//                RetryHelper.putObservable(observable)
//            }
//            return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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