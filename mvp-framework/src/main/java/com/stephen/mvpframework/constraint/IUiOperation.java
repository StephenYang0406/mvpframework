package com.stephen.mvpframework.constraint;


import java.io.File;

/**
 * UI操作汇总接口
 * Created by Stephen on 2017/10/20.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public interface IUiOperation {

    //初始方法
    void onViewInit();

    //刷新方法
    void onViewRefresh();

    //自动装填方法
    void autoWire();

    //设置刷新模式
    void setRefreshMode(boolean refreshMode);

    //获取刷新模式
    boolean getRefreshMode();

    //绑定Presenter
    void bindPresenter();

    //拍照后返回结果
    void onTakePhotoResult(File file);

    //展示空布局
    void displayEmptyView();

    //展示网络异常布局
    void displayNetWorkErrorView();

    //展示服务器异常布局
    void displayErrorView();

    //展示内容布局
    void displayContentView();

    //关闭列表刷新加载
    void stopRefresh();
}
