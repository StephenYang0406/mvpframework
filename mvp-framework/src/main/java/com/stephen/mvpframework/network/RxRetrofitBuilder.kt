package com.stephen.mvpframework.network

import android.text.TextUtils
import com.google.gson.Gson
import com.stephen.mvpframework.local.Content
import com.stephen.mvpframework.network.interceptor.HttpLoggingInterceptor
import com.stephen.mvpframework.utils.LogUtil
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.NullPointerException
import java.util.concurrent.TimeUnit

/**
 * Retrofit RX格式构建器
 * Created by Stephen on 2018/11/27.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
class RxRetrofitBuilder {
    companion object {
        private lateinit var retrofit: Retrofit
    }

    private var baseUrl: String? = null
    private var gson: Gson = Gson()
    private var writeTimeout: Long = Content.ConnectionTime.COMMON_TIME_OUT
    private var readTimeout: Long = Content.ConnectionTime.COMMON_TIME_OUT
    private var connectTimeout: Long = Content.ConnectionTime.COMMON_TIME_OUT
    private val interceptorList: ArrayList<Interceptor> by lazy {
        val list = ArrayList<Interceptor>()
        list.add(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        list
    }

    /**
     * 统一URL
     */
    fun baseUrl(baseUrl: String): RxRetrofitBuilder {
        this.baseUrl = baseUrl
        return this
    }

    /**
     * 读取时间,单位毫秒 MILLISECONDS
     */
    fun readTimeout(readTimeout: Long): RxRetrofitBuilder {

        return this
    }

    /**
     * 写入时间,单位毫秒 MILLISECONDS
     */
    fun writeTimeout(writeTimeout: Long): RxRetrofitBuilder {
        this.writeTimeout = writeTimeout
        return this
    }

    /**
     * 链接超时时间,单位毫秒 MILLISECONDS
     */
    fun connectTimeout(connectTimeout: Long): RxRetrofitBuilder {
        this.connectTimeout = connectTimeout
        return this
    }

    /**
     * 自定义GSon解析器
     */
    fun gson(gson: Gson): RxRetrofitBuilder {
        this.gson = gson
        return this
    }

    /**
     * 添加OkHttpClient拦截器
     */
    fun addInterceptor(interceptor: Interceptor): RxRetrofitBuilder {
        if (!interceptorList.contains(interceptor))
            interceptorList.add(interceptor)
        return this
    }

    fun build(): Retrofit {
        if (TextUtils.isEmpty(baseUrl))
            throw NullPointerException("baseUrl con not be null !!!")
        val okHttpClient = OkHttpClient.Builder()
                .writeTimeout(this.writeTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(this.readTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(this.connectTimeout, TimeUnit.MILLISECONDS)
                .apply { interceptorList.forEach { addInterceptor(it) } }
                .build()
        retrofit = Retrofit.Builder()
                .baseUrl(this.baseUrl!!)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(this.gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        LogUtil.testInfo("网络请求基础URL地址--->{$baseUrl}---读取阈值--->{$readTimeout}---" +
                "写入阈值--->{$writeTimeout}---连接超时阈值--->{$connectTimeout}" +
                "----构建成功")
        return retrofit
    }

}