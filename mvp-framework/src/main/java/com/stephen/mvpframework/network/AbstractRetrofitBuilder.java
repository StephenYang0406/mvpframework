package com.stephen.mvpframework.network;

import com.google.gson.Gson;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 抽象Retrofit获取类
 * Created by Stephen on 2017/10/10.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public abstract class AbstractRetrofitBuilder {

    private static Retrofit mRetrofit;
    protected static AbstractRetrofitBuilder mInstance;

    public Retrofit getRetrofit() {
        synchronized (AbstractRetrofitBuilder.class) {
            if (mRetrofit == null) {
                mRetrofit = new Retrofit.Builder()
                        .baseUrl(getBaseUrl())
                        .client(OkHttpFactory.getInstance())
                        .addConverterFactory(GsonConverterFactory.create(getGson()))
                        //Retrofit的BUG，和RX搭配要这样才能切换到非主线程
                        //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                        .build();
            }
            return mRetrofit;
        }
    }

    protected Gson getGson() {
        return new Gson();
    }

    protected abstract String getBaseUrl();
}
