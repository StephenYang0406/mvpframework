package com.stephen.mvpframework;

import android.app.Application;

import com.stephen.mvpframework.autolayout.RudenessScreenHelper;
import com.stephen.mvpframework.context.ContextHandler;
import com.stephen.mvpframework.local.Content;

import io.realm.Realm;

/**
 * 基础Application
 * Created by Stephen on 2017/10/31.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //储存Application
        ContextHandler.saveApplication(this);
        //数据库初始化
        Realm.init(this);
        //适配布局初始化
        new RudenessScreenHelper(this, Content.Params.DESIGN_WIDTH).activate();
    }



}
