package com.stephen.mvpframework.helper;


import com.stephen.mvpframework.context.ContextHandler;
import com.stephen.mvpframework.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * 重试帮助类
 * Created by Stephen on 2018/2/4.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public class RetryHelper {
    private static HashMap<String, Observable> mObservableMap = new HashMap<>();
    private static HashMap<String, Observer> mObserverMap = new HashMap<>();

    //存放观察者
    public synchronized static void putObserver(String key, Observer observer) {
        LogUtil.testError("putObserver--->" + key);
        mObserverMap.put(key, observer);
    }

    //存放被观察者
    public synchronized static void putObservable(Observable observable) {
        LogUtil.testError("putObservable--->" + ContextHandler.current().getClass().getName());
        mObservableMap.put(ContextHandler.current().getClass().getName(), observable);
    }

    //通过Observable获取key
    public synchronized static String getObservableKey(Observable observable) {
        for (Map.Entry<String, Observable> entry : mObservableMap.entrySet()) {
            if (observable == entry.getValue()) {
                return entry.getKey();
            }
        }
        return null;
    }

    //通过key判断是否包含此Observable
    public synchronized static boolean containsObservable(String key) {
        return mObservableMap.containsKey(key);
    }
    //是否包含此Observer
    public synchronized static boolean containsObserver(Observer observer) {
        return mObserverMap.containsValue(observer);
    }


    //重试
    public synchronized static void retry(String target) {
        if (mObservableMap.containsKey(target) && mObserverMap.containsKey(target)) {
            mObservableMap.get(target).subscribe(mObserverMap.get(target));
        }
    }
}
