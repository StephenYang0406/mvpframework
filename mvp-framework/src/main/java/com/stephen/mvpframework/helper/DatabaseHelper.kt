package com.stephen.mvpframework.helper

import com.stephen.mvpframework.local.Content
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Realm数据库帮助类
 * Created by Stephen on 2018/12/3.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
class DatabaseHelper {

    //获取默认的配置
    fun getDefaultConfig(): RealmConfiguration {
        return RealmConfiguration.Builder()
                .name(Content.Params.DATABASE_NAME)
                //.schemaVersion(Content.Params.DATABASE_SCHEMA_VERSION)
                //                .migration((realm, oldVersion, newVersion) -> {
                //
                //                })
                .build()
    }

    //获取默认配置数据库
    fun getDefaultRealm(): Realm {
        return Realm.getInstance(getDefaultConfig())
    }
}