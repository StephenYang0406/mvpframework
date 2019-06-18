package com.stephen.demo.network

import com.stephen.demo.model.SampleResponse
import com.stephen.demo.sample.vo.SapmleVo
import com.stephen.mvpframework.annotation.RetryAnno
import com.stephen.mvpframework.model.BaseVo
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

/**
 * 示例项目API接口
 * Created by Stephen on 2018/12/4.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
interface SampleApiService {
    @RetryAnno
    @POST("/sample")
    fun doPost(@Body body: RequestBody): Observable<SampleResponse<SapmleVo>>

    @GET("/sample")
    fun doGet(@QueryMap map: Map<String, String>): Observable<SampleResponse<SapmleVo>>

    @POST("/sample")
    fun doMix(@Body body: RequestBody, @QueryMap map: Map<String, String>): Observable<SampleResponse<SapmleVo>>

}