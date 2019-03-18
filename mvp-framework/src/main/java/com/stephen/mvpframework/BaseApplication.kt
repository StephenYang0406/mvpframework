package com.stephen.mvpframework

import android.app.Application
import com.stephen.mvpframework.handler.ContextHandler

/**
 *
 * Created by Stephen on 2018/11/15.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
class BaseApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        //储存Application
        ContextHandler.saveApplication(this)
        //数据库初始化
        //Realm.init(this)
    }
}