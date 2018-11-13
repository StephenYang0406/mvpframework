package com.stephen.mvpframework.network;


import com.stephen.mvpframework.local.Content;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * OkHttpClient工厂类
 * Created by Stephen on 2017/10/10.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

class OkHttpFactory {
    private static OkHttpClient mOkHttpClient;

    private OkHttpFactory() {
    }

    static OkHttpClient getInstance() {
        synchronized (OkHttpFactory.class) {
            if (mOkHttpClient == null) {
                mOkHttpClient = new OkHttpClient.Builder()
                        .writeTimeout(Content.ConnectionTime.COMMON_TIME_OUT, TimeUnit.MILLISECONDS)
                        .readTimeout(Content.ConnectionTime.COMMON_TIME_OUT, TimeUnit.MILLISECONDS)
                        .connectTimeout(Content.ConnectionTime.COMMON_TIME_OUT, TimeUnit.MILLISECONDS)
                        //.protocols(Collections.singletonList(Protocol.HTTP_1_1))
//                        .addInterceptor(chain -> {
//                            Request exRequest = chain.request();
//                            //添加token
//                            Request tokenRequest = exRequest.newBuilder()
//                                    .header("token", SharedPreferenceUtil.getValue(Content.SharedPreferencesParam.ConfigKey.TOKEN) == null ?
//                                            ""
//                                            : SharedPreferenceUtil.getValue(Content.SharedPreferencesParam.ConfigKey.TOKEN))
//                                    .build();
//                            try {
//                                return chain.proceed(tokenRequest);
//                            } catch (IOException e) {
//                                return null;
//                            }
//                        })
                        //添加日志拦截器
                        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .build();
            }
            return mOkHttpClient;
        }
    }

}
