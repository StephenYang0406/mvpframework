package com.stephen.mvpframework.context;

import android.app.Application;
import android.content.Intent;
import android.view.View;

import com.stephen.mvpframework.local.Content;
import com.stephen.mvpframework.ui.IUiOperation;
import com.stephen.mvpframework.ui.activity.AbstractActivity;
import com.stephen.mvpframework.ui.fragment.AbstractFragment;
import com.stephen.mvpframework.utils.BeanUtil;
import com.stephen.mvpframework.utils.LogUtil;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Stack;

/**
 * 上下文控制类
 * Created by Stephen on 2017/10/4.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public class ContextHandler {
    //activity堆栈
    private static Stack<AbstractActivity> activityStack = new Stack<>();


    private static SoftReference<AbstractFragment> fragment;

    //APPLICATION
    private static SoftReference<Application> application;

    /**
     * 保存Application
     */
    public static void saveApplication(Application application) {
        ContextHandler.application = new SoftReference<>(application);
    }

    /**
     * 获得Application
     */
    public static Application getApplication() {
        return application.get();
    }

    /**
     * 添加Activity到堆栈
     */
    public static void addActivity(AbstractActivity activity) {
        fragment = null;
        LogUtil.testError(activity.getClass().getName() + "加入栈");
        activityStack.push(activity);
    }

    /**
     * 设置Fragment
     */
    public static void setFragment(AbstractFragment fragment) {
        ContextHandler.fragment = new SoftReference<>(fragment);
    }

    /**
     * 获取当前
     */
    public static IUiOperation current() {
        if (fragment != null && fragment.get() != null)
            return fragment.get();
        return currentActivity();
    }

    /**
     * 获得当前的fragment
     */
    public static AbstractFragment currentFragment() {
        return fragment.get();
    }

    /**
     * 获得当前运行的Activity
     */
    public static AbstractActivity currentActivity() {
        return activityStack.peek();
    }

    /**
     * 移除最后的Activity
     */
    public static void removeLast() {
        fragment = null;
        LogUtil.testError(activityStack.peek().getClass().getName() + "最顶出栈");
        activityStack.pop();
    }

    /**
     * 当前栈是否包含此Activity
     */
    public static boolean containsActivity(AbstractActivity activity) {
        return activityStack.contains(activity);
    }

    /**
     * 移除指定Activity
     */
    public static void removeTargetActivity(AbstractActivity activity) {
        LogUtil.testError(activity.getClass().getName() + "指定出栈");
        activityStack.remove(activity);
    }

    /**
     * 结束除了指定的所有Activity
     */
    public static void finishAllActivity(AbstractActivity activity) {
        for (AbstractActivity activityTemp : activityStack) {
            if (!activityTemp.getClass().getName().equals(activity.getClass().getName())) {
                activityTemp.finish();
            }
        }
        activityStack.removeAllElements();
        if (activity != null) {
            activityStack.add(activity);
        }
    }

    /**
     * 获取当前根视图
     */
    public static View getRootView() {
        if (fragment != null && fragment.get() != null)
            return fragment.get().getView();
        return activityStack.lastElement().getWindow().getDecorView();
    }

    //获取当前Activity根视图
    public static View getActivityRootView() {
        return activityStack.lastElement().getWindow().getDecorView();
    }

    /**
     * 快速跳转界面
     */
    public static void goForward(Class activityClass, Object... objArr) {
        boolean flag = false;
        int requestCode = -1;
        HashMap params = new HashMap();
        Intent intent = new Intent(currentActivity(), activityClass);
        if (objArr != null && objArr.length > 0) {
            for (Object obj : objArr) {
                if (Integer.class.isAssignableFrom(obj.getClass())) {
                    flag = true;
                    requestCode = (Integer) obj;
                } else if (HashMap.class.isAssignableFrom(obj.getClass())) {
                    params = (HashMap) obj;
                }
            }
        }
        params.put(Content.Params.REQUEST_KEY, requestCode);
        intent.putExtra(Content.Params.PARAMS_KEY, params);
        intent.putExtra(Content.Params.FORM_KEY, BeanUtil.getForm(current()));
        if (flag)
            currentActivity().startActivity(intent);
        else
            currentActivity().startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转响应
     */
    public static void response() {
        HashMap params = (HashMap) currentActivity().getIntent().getSerializableExtra(Content.Params.PARAMS_KEY);


    }

}
