package com.stephen.demo.network

import com.stephen.demo.model.LoginVo
import com.stephen.demo.model.SampleResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 示例项目API接口
 * Created by Stephen on 2018/12/4.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
interface SampleApiService {
    //校验token接口
    @POST("/jxc/api/customer/checkToken")
    fun checkToken(@Body post: RequestBody): Observable<SampleResponse<LoginVo>>

}