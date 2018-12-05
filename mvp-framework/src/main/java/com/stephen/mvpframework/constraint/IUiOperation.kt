package com.stephen.mvpframework.constraint

import java.io.File

/**
 * UI操作约束接口
 * Created by Stephen on 2018/12/4.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
interface IUiOperation {
    //初始方法
    fun onViewInit()

    //刷新方法
    fun onViewRefresh()

    //自动装填方法
    fun autowire()

    //设置刷新模式
    fun setRefreshMode(refreshMode: Boolean)

    //获取刷新模式
    fun getRefreshMode(): Boolean

    //绑定Presenter
    fun bindPresenter()

    //拍照后返回结果
    fun onTakePhotoResult(file: File?)

//    //展示空布局
//    void displayEmptyView();
//
//    //展示网络异常布局
//    void displayNetWorkErrorView();
//
//    //展示服务器异常布局
//    void displayErrorView();
//
//    //展示内容布局
//    void displayContentView();
//
//    //关闭列表刷新加载
//    void stopRefresh();
}