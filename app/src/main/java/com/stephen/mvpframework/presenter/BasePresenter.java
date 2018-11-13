package com.stephen.mvpframework.presenter;

import android.support.annotation.NonNull;

import com.stephen.mvpframework.view.BaseView;

/**
 * presenter接口类
 * Created by Stephen on 2017/10/11.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public interface BasePresenter {
    //绑定View方法
    void bindView(@NonNull BaseView view);

    //通用回收方法
    void recycle();
}
