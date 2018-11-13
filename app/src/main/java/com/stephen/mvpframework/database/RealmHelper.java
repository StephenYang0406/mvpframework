package com.stephen.mvpframework.database;


import com.stephen.mvpframework.local.Content;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Realm数据库帮助类
 * Created by Stephen on 2018/1/15.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public class RealmHelper {

    //获取默认的配置
    public static RealmConfiguration getDefaultConfig() {
        return new RealmConfiguration.Builder()
                .name(Content.Params.DATABASE_NAME)
                //.schemaVersion(Content.Params.DATABASE_SCHEMA_VERSION)
//                .migration((realm, oldVersion, newVersion) -> {
//
//                })
                .build();
    }

    //获取默认配置数据库
    public static Realm getDefaultRealm() {
        return Realm.getInstance(getDefaultConfig());
    }
}
