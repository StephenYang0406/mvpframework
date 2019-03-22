package com.stephen.demo.network

import com.stephen.demo.model.LoginVo
import com.stephen.demo.model.SampleResponse
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
    //校验token接口
    @RetryAnno
    @POST("/jxc/api/customer/checkToken")
    fun checkToken(@Body post: RequestBody): Observable<SampleResponse<LoginVo>>

    //登陆接口
    @GET("/user/login")
    fun login(@QueryMap map: Map<String, String>): Observable<SampleResponse<String>>

    //获取总标段列表接口
    @GET("/total/getTotalProjectPage")
    fun getTotalTenderList(@QueryMap map: Map<String, String>): Observable<SampleResponse<BaseVo>>

}