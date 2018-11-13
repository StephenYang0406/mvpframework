package com.stephen.mvpframework.network;


import com.stephen.mvpframework.context.ContextHandler;
import com.stephen.mvpframework.utils.LogUtil;

import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 基础Observer
 * Created by Stephen on 2018/2/2.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */

public abstract class BaseObserver<T> implements Observer<T> {
    private static HashMap<String, CompositeDisposable> mCompositeDisposableMap = new HashMap<>();

    public static void unSubscribe(String tag) {
        if (mCompositeDisposableMap.containsKey(tag)) {
            LogUtil.testError("unSubscribe--->" + tag);
            mCompositeDisposableMap.get(tag).clear();
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        LogUtil.testError("onSubscribe--->" + ContextHandler.current().getClass().getName());
        if (mCompositeDisposableMap.containsKey(ContextHandler.current().getClass().getName())) {
            CompositeDisposable compositeDisposable = mCompositeDisposableMap.get(ContextHandler.current().getClass().getName());
            compositeDisposable.add(d);
        } else {
            CompositeDisposable compositeDisposable = new CompositeDisposable();
            compositeDisposable.add(d);
            mCompositeDisposableMap.put(ContextHandler.current().getClass().getName(), compositeDisposable);
        }
    }
}
