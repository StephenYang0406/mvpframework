package com.stephen.mvpframework.constraint

/**
 * Presenter接口
 * Created by Stephen on 2018/12/3.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
interface IPresenter {
    //绑定View方法
    fun bindView(view: IView)

    //通用回收方法
    fun recycle()
}